package com.eplugger.onekey.autoScene;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class SceneSQLFileMergeTest {

    @Test
    public void testName() throws IOException {
        reprocess();
        merge();
    }

    @Test
    public void reprocess() throws IOException {
        SceneSQLFileMerge.reprocess();
    }

    @Test
    public void merge() throws IOException {
        SceneSQLFileMerge.merge();
    }

    @Test
    public void reprocessFile() throws IOException {
        SceneSQLFileMerge.reprocessFile(new File("C:\\Users\\ningm\\Downloads\\整体模块信息配置SQL (7).sql"));
    }
}