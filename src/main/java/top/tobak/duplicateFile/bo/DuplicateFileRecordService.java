package top.tobak.duplicateFile.bo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.csv.CSVFormat;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import top.tobak.common.io.FileUtils;
import top.tobak.csv.CSVReader;
import top.tobak.csv.CSVUtil;
import top.tobak.duplicateFile.entity.DuplicateFileRecord;
import top.tobak.poi.excel.ExcelReader;
import top.tobak.poi.excel.ExcelWriter;
import top.tobak.utils.ExcelUtils;

public class DuplicateFileRecordService {

    private DuplicateFileRecordService() {}

    private static volatile DuplicateFileRecordService instance;

    public static DuplicateFileRecordService getInstance() {
        if (instance == null) {
            synchronized (DuplicateFileRecordService.class) {
                if (instance == null) {
                    instance = new DuplicateFileRecordService();
                }
            }
        }
        return instance;
    }

    public void processDuplicateFileRecords1(List<String> keywords, String inputFile, String outputFile) {
        List<DuplicateFileRecord> data;
        if (inputFile.contains(".xls")) {
            // 读取Excel文件中的数据
            ExcelReader reader = ExcelUtils.getReader(inputFile);
            data = reader.readAll(DuplicateFileRecord.class);
        } else {
            CSVReader reader = CSVUtil.getReader(inputFile, null, "utf-16");
            data = reader.readAll(DuplicateFileRecord.class);
        }

        // 按分组编号对数据进行分组
        Map<Integer, List<DuplicateFileRecord>> groupedData = groupDataByGroup(data);

        // 处理每个分组的数据
        // 创建要删除和要保留的记录列表
        List<DuplicateFileRecord> recordsToSave = Lists.newArrayList();
        List<DuplicateFileRecord> recordsToDelete = Lists.newArrayList();
        processGroupedData(keywords, groupedData, recordsToSave, recordsToDelete);

        Set<String> toBeDeleted = recordsToDelete.stream().map(DuplicateFileRecord::getFile).collect(Collectors.toSet());
        markRecordsInExcel(inputFile, outputFile, toBeDeleted);
    }

    public void processDuplicateFileRecords(List<String> keywords, String inputFile, String outputFile, String outputFileTxt) {
        List<DuplicateFileRecord> data;
        if (inputFile.contains(".xls")) {
            // 读取Excel文件中的数据
            ExcelReader reader = ExcelUtils.getReader(inputFile);
            data = reader.readAll(DuplicateFileRecord.class);
        } else {
            CSVReader reader = CSVUtil.getReader(inputFile, null, "utf-16");
            reader.setFormat(CSVFormat.DEFAULT.builder().setDelimiter('\t')
                                 .setHeader().setAllowMissingColumnNames(true).setSkipHeaderRecord(true).setTrim(true).build());
            data = reader.readAll(DuplicateFileRecord.class);
        }

        // 按分组编号对数据进行分组
        Map<Integer, List<DuplicateFileRecord>> groupedData = groupDataByGroup(data);

        // 处理每个分组的数据
        // 创建要删除和要保留的记录列表
        List<DuplicateFileRecord> recordsToSave = Lists.newArrayList();
        List<DuplicateFileRecord> recordsToDelete = Lists.newArrayList();
        processGroupedData(keywords, groupedData, recordsToSave, recordsToDelete);

        // 将记录写入到Excel文件
        writeRecordsToExcel(outputFile, recordsToDelete, recordsToSave);

        // 将记录写入到Excel文件
        writeRecordsToTxt(outputFileTxt, recordsToDelete);
    }

    private Map<Integer, List<DuplicateFileRecord>> groupDataByGroup(List<DuplicateFileRecord> data) {
        Map<Integer, List<DuplicateFileRecord>> groupedData = Maps.newHashMap();
        for (DuplicateFileRecord duplicateFileRecord : data) {
            int groupId = duplicateFileRecord.getGroup();
            if (!groupedData.containsKey(groupId)) {
                groupedData.put(groupId, Lists.newArrayList());
            }
            groupedData.get(groupId).add(duplicateFileRecord);
        }
        return groupedData;
    }

    private void processGroupedData(List<String> keywords, Map<Integer, List<DuplicateFileRecord>> groupedData, List<DuplicateFileRecord> recordsToSave, List<DuplicateFileRecord> recordsToDelete) {
        // 处理每个分组的数据
        for (List<DuplicateFileRecord> duplicateFileRecords : groupedData.values()) {
            // 检查是否有包含关键字的记录
            boolean hasKeywordRecord = false;
            boolean hasNonKeywordRecord = false;
            List<DuplicateFileRecord> recordsToDeleteInGroup = Lists.newArrayList();
            for (DuplicateFileRecord duplicateFileRecord : duplicateFileRecords) {
                String filePath = duplicateFileRecord.getFile();
                if (isContainKeyword(filePath, keywords)) {
                    hasKeywordRecord = true;
                } else {
                    hasNonKeywordRecord = true;
                    recordsToDeleteInGroup.add(duplicateFileRecord);
                }
            }

            if (hasKeywordRecord && hasNonKeywordRecord) {
                // 如果这组数据部分包含关键字，部分不包含关键字，则不包含关键字的部分的最后一个记录添加到要保留的列表中，其他记录添加到要删除的列表中；
                // 包含关键字的部分全部添加到要删除的列表中
                for (DuplicateFileRecord duplicateFileRecord : duplicateFileRecords) {
                    String filePath = duplicateFileRecord.getFile();
                    if (isContainKeyword(filePath, keywords)) {
                        recordsToDelete.add(duplicateFileRecord);
                    }
                }
                if (!recordsToDeleteInGroup.isEmpty()) {
                    addRecordsToList(recordsToDeleteInGroup, recordsToDelete, recordsToSave);
                }
            } else {
                // 如果这组数据完全包含关键字，添加最后一个记录到要保留的列表中，其他记录添加到要删除的列表中
                // 如果这组数据完全不包含关键字，添加最后一个记录到要保留的列表中，其他记录添加到要删除的列表中
                addRecordsToList(duplicateFileRecords, recordsToDelete, recordsToSave);
            }
        }
    }

    private boolean isContainKeyword(String filePath, List<String> keywords) {
        for (String keyword : keywords) {
            if (filePath.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private void addRecordsToList(List<DuplicateFileRecord> duplicateFileRecords, List<DuplicateFileRecord> recordsToDelete, List<DuplicateFileRecord> recordsToSave) {
        DuplicateFileRecord earliestEditDuplicateFileRecord = duplicateFileRecords.get(0); // 初始化最早编辑记录为第一条记录
        for (DuplicateFileRecord duplicateFileRecord : duplicateFileRecords) {
            // 找到最早编辑日期的记录
            if (duplicateFileRecord.getModifiedTime().compareTo(earliestEditDuplicateFileRecord.getModifiedTime()) < 0) {
                earliestEditDuplicateFileRecord = duplicateFileRecord;
            }
        }

        // 将最早编辑日期的记录添加到要保留的列表中
        recordsToSave.add(earliestEditDuplicateFileRecord);

        // 将其他记录添加到要删除的列表中
        for (DuplicateFileRecord duplicateFileRecord : duplicateFileRecords) {
            if (!duplicateFileRecord.equals(earliestEditDuplicateFileRecord)) { // 排除最早编辑日期的记录
                recordsToDelete.add(duplicateFileRecord);
            }
        }
    }

    private void writeRecordsToExcel(String outputFile, List<DuplicateFileRecord> recordsToDelete, List<DuplicateFileRecord> recordsToSave) {
        ExcelWriter writer = ExcelUtils.getWriter(false, "To Be Deleted");
        writer.write(DuplicateFileRecord.class, recordsToDelete);

        writer.setSheet("To Be Saved");
        writer.write(DuplicateFileRecord.class, recordsToSave);

        // 将数据写入到 Excel 文件
        writer.flush(FileUtils.getFile(outputFile));
        writer.close();
    }

    private void writeRecordsToTxt(String outputFileTxt, List<DuplicateFileRecord> recordsToDelete) {
        List<String> list = recordsToDelete.stream().map(DuplicateFileRecord::getFile).collect(Collectors.toList());
        try {
            FileUtils.writeLines(FileUtils.getFile(outputFileTxt), "utf-8", list, "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void markRecordsInExcel(String filePath, String outFilePath, Set<String> toBeDeleted) {
        try (FileInputStream fis = new FileInputStream(filePath);
            Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheetAt(0); // 假设数据在第一个工作表中

            // 获取字体样式
            Font font = workbook.createFont();
            font.setColor(IndexedColors.RED.getIndex());

            // 设置单元格样式
            CellStyle style = workbook.createCellStyle();
            style.setFont(font);

            for (Row row : sheet) {
                // 忽略标题行
                if (row.getRowNum() == 0) {
                    continue;
                }
                String file = row.getCell(2).getStringCellValue();
                if (toBeDeleted.contains(file)) {
                    for (int i = 0; i < row.getLastCellNum(); i++) {
                        Cell cell = row.getCell(i);
                        if (cell == null) {
                            cell = row.createCell(i);
                        }
                        cell.setCellStyle(style); // 将匹配的单元格设置为红色字体
                    }
                    Cell cell = row.createCell(row.getLastCellNum());
                    cell.setCellStyle(style);
                    cell.setCellValue("Delete");
                }
            }

            // 保存修改后的Excel文件
            try (FileOutputStream fos = new FileOutputStream(outFilePath)) {
                workbook.write(fos);
            }
        } catch (IOException | EncryptedDocumentException e) {
            e.printStackTrace();
        }
    }
}
