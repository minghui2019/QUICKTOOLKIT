package com.eplugger.pdf;

import java.io.File;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;
import org.junit.Test;
import top.tobak.common.io.FileUtils;
import top.tobak.utils.pdf.PdfUtils;

public class MergePdfTest {

    @Test
    public void testName() {
        // 获取C盘目录下所有文件
        File file = FileUtils.getFile("E:\\广东财经大学科研管理系统验收文档电子版\\");
        boolean directory = file.isDirectory();
        if (!directory) {
            return;
        }
        File[] files = file.listFiles();
        List<String> fileList = Lists.newArrayList();
        for (File f : files) {
            if (f.isDirectory()) {
                continue;
            }
            if (f.getName().endsWith(".pdf")) {
                fileList.add(f.getAbsolutePath());
            }
        }
        // 按文件名顺序排序
        fileList.sort(Comparator.comparing(o -> o.substring(o.lastIndexOf("\\") + 1)));
        PdfUtils.mergePdfFiles(fileList, "E:\\广东财经大学科研管理系统验收文档电子版\\验收文档.pdf");
        System.out.println("合并完成");
    }
}
