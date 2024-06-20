package com.eplugger.onekey.autoScene;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import top.tobak.common.io.FileUtils;

public class SceneSQLFileMerge {
    private static final String parentPath = "C:\\Users\\ningm\\Documents\\场景语句";

    private static final Pattern tableP = Pattern.compile("-- 表\\[(SYS_CFG_[A-Z_]+)\\]的数据如下:");
    private static final Pattern idP = Pattern.compile("^insert into SYS_CFG_SCENE\\([A-Z,_]+\\) values\\(('[a-z\\d]{32}').*$");

    public static void reprocess() throws IOException {
        File file = FileUtils.getFile(parentPath);
        boolean directory = file.isDirectory();

        if (!directory) {
            return;
        }
        File[] files = file.listFiles();
        for (File file1 : files) {
            reprocessFile(file1);
        }
    }

    public static void reprocessFile(File file1) throws IOException {
        String name = file1.getName();
        String delete1 = "", delete2 = "", delete3 = "";
        ImmutableList<String> fileLines = Files.asCharSource(file1, Charset.forName("GBK")).readLines();
        boolean flag = false;
        List<String> objects = Lists.newArrayList();
        String lastLine = "";
        for (int i = fileLines.size() - 1; i >= 0 ; i--) {
            String line = fileLines.get(i);
            if (line.startsWith("delete from")) {
                if (line.startsWith("delete from DM_QUICK_SEARCH")) {
                    objects.add(0, line);
                }
                continue;
            }
            if (lastLine.equals(line)) {
                lastLine = line;
                continue;
            }
            lastLine = line;
            Matcher matcher = tableP.matcher(line);
            if (matcher.find()) {
                String group = matcher.group(1);
                if (!"SYS_CFG_SCENE".equals(group)) {
                    if (Strings.isNullOrEmpty(delete1)) {
                        delete1 = "delete from " + group + " where sceneid=%s;";
                    } else {
                        delete2 = "delete from " + group + " where sceneid=%s;";
                    }
                } else {
                    flag = true;
                    delete3 = "delete from " + group + " where id=%s;";
                }
            }
            if (i + 1 < fileLines.size()) {
                String nextLine = fileLines.get(i + 1);
                Matcher matcher1 = idP.matcher(nextLine);
                if (matcher1.find()) {
                    delete1 = String.format(delete1, matcher1.group(1));
                    delete2 = String.format(delete2, matcher1.group(1));
                    delete3 = String.format(delete3, matcher1.group(1));
                }
            }
            objects.add(0, line);
            if (flag) {
                objects.add(0, delete3);
                objects.add(0, delete2);
                objects.add(0, "\n\r" + delete1);
                flag = false;
                delete1 = "";
                delete2 = "";
                delete3 = "";
            }
        }
        File nfile = new File(file1.getParent(), name.substring(0, name.indexOf(".")) + "副本" + ".sql");
        CharSink nfileSink = Files.asCharSink(nfile, StandardCharsets.UTF_8, FileWriteMode.APPEND);
        for (String line : objects) {
            if ("".equals(line)) {
                continue;
            }
            if (line.startsWith("-- 表")) {
                continue;
            }
            nfileSink.write(line + "\n\r");
        }
    }

    public static void merge(String parent, String child, String exclusionCriteria) throws IOException {
        File parentFile = FileUtils.getFile(parent);
        File nfile = new File(parentFile, child);
        boolean directory = parentFile.isDirectory();
        if (!directory) {
            return;
        }
        File[] files = parentFile.listFiles();
        for (File file : files) {
            if (file.length() == 0 || file.getName().indexOf(exclusionCriteria) == -1) {
                continue;
            }
            CharSink charSink = Files.asCharSink(nfile, StandardCharsets.UTF_8, FileWriteMode.APPEND);
            charSink.write("-- " + file.getName() + "\n\r");
            charSink.write("truncate table " + file.getName().replace(exclusionCriteria, "") + ";\n\r");
            charSink.writeLines(Files.asCharSource(file, Charset.forName("UTF-8")).readLines(new LineProcessor<List<String>> () {
                private final List<String> lines = Lists.newArrayList();
                @Override
                public boolean processLine(String line) throws IOException {
                    if (line.contains("; go") || line.contains("; GO")) {
                        lines.add(line.replace("; go", ";")
                                      .replace("; GO", ";")
                                      .replace("[", "")
                                      .replace("]", ""));
                    }
                    return true;
                }

                @Override
                public List<String> getResult() {
                    return lines;
                }
            }));
            charSink.write("\n\r\n\r");
        }
    }

    public static void merge() throws IOException {
        File file = FileUtils.getFile(parentPath);
        File nfile = new File(parentPath, "V8.5.6_update4.2.sql");
        boolean directory = file.isDirectory();
        if (!directory) {
            return;
        }
        File[] files = file.listFiles();
        for (File file1 : files) {
            if (file1.getName().indexOf("副本") == -1) {
                continue;
            }
            CharSink charSink = Files.asCharSink(nfile, StandardCharsets.UTF_8, FileWriteMode.APPEND);
            charSink.write("-- " + file1.getName().replace("副本", "") + "\n\r");
            charSink.write(Files.asCharSource(file1, Charset.forName("UTF-8")).read());
            charSink.write("\n\r\n\r");
        }
    }
}
