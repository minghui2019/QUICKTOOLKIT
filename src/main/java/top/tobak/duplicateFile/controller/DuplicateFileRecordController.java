package top.tobak.duplicateFile.controller;

import java.util.List;

import com.google.common.collect.Lists;
import top.tobak.duplicateFile.bo.DuplicateFileRecordService;

public class DuplicateFileRecordController {

    /**
     * 分别处理要删除和保留的重复文件记录到excel
     */
    public void processDuplicateFileRecords(String inputFile, List<String> keywords) {
        String outputFile = "C:\\Users\\ningm\\Documents\\群晖图片文件去重\\duplicate_file_after.xls"; // 需要删除的数据输出文件路径
        String outputFileTxt = "C:\\Users\\ningm\\Documents\\群晖图片文件去重\\duplicate_file_after.txt"; // 需要删除的数据输出文件路径
        DuplicateFileRecordService.getInstance().processDuplicateFileRecords(keywords, inputFile, outputFile, outputFileTxt);
    }

    public void processDuplicateFileRecords1() {
        List<String> keywords = Lists.newArrayList("Synology Drive ShareSync");
        String inputFile = "C:\\Users\\ningm\\Documents\\群晖图片文件去重\\duplicate_file.xls";
        String outputFile = "C:\\Users\\ningm\\Documents\\群晖图片文件去重\\duplicate_file_after1.xls"; // 需要删除的数据输出文件路径
        DuplicateFileRecordService.getInstance().processDuplicateFileRecords1(keywords, inputFile, outputFile);
    }
}
