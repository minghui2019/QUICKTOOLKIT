package top.tobak.duplicateFile.controller;

import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import org.apache.commons.beanutils.ConvertUtils;
import org.junit.Test;

public class DuplicateFileRecordControllerTest {

    @Test
    public void processDuplicateFileRecords() {
        DuplicateFileRecordController controller = new DuplicateFileRecordController();
        List<String> keywords = Lists.newArrayList("音乐临时");
        String inputFile = "C:\\Users\\ningm\\Documents\\群晖图片文件去重\\duplicate_file.csv";
        ConvertUtils.register((type, value) -> {
            if (!type.equals(Date.class)) {
                return value;
            }
            return DateUtil.parse(value.toString().trim());
        }, java.util.Date.class);
        controller.processDuplicateFileRecords(inputFile, keywords);
    }

    @Test
    public void processDuplicateFileRecords1() {
        DuplicateFileRecordController controller = new DuplicateFileRecordController();
        controller.processDuplicateFileRecords1();
    }
}