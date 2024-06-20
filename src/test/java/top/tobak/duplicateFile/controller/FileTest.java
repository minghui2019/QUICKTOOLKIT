package top.tobak.duplicateFile.controller;

import java.io.File;
import java.util.Date;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import top.tobak.common.io.FileUtils;

@Slf4j
public class FileTest {

    @Test
    public void testName() {
        //文件目录
        String filePath = "G:\\shaomei\\Photos\\MobileBackup\\OPPO A30\\DCIM\\Camera";
        File directory = FileUtils.getFile(filePath);
        if (!FileUtils.isDirectory(directory)) {
            return;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            // 匹配文件名类似：IMG20180203155718.jpg或IMG20180203155718.jpeg或IMG20180203155718_1.jpg
            String fileName = file.getName();
            if (fileName.matches("IMG[\\d]{14}[_]?\\d?\\.jp[e]?g")) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("name", fileName);
                jsonObject.addProperty("absolutePath", file.getAbsolutePath());
                String lastModified = DateUtil.formatDateTime(new Date(file.lastModified()));
                jsonObject.addProperty("lastModified", lastModified);
                DateTime dateTime = DateUtil.parse(fileName.substring(3, 17), "yyyyMMddHHmmss");
                file.setLastModified(dateTime.getTime());
                jsonObject.addProperty("lastModified_after", dateTime.toString());

                log.debug(jsonObject.toString());
            }
//            else {
//                log.error(fileName);
//            }
        }
    }
}
