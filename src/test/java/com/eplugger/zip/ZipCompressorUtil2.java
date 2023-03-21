package com.eplugger.zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * java实现zip压缩文件（同一文件夹下的多个文件夹打成一个zip包）
 *
 * @author gblfy
 * @date 2020-07-02
 */
public class ZipCompressorUtil2 {
    static final int BUFFER = 8192;

    /**
     * 判断传参类型:是目录还是文件
     * <p>
     * 1.如果是文件,则调用压缩文件方法
     * 2.如果是目录,则调用压缩目录方法
     * </p>
     *
     * @param file
     * @param out
     * @param basedir
     */
    private void compress(File file, ZipOutputStream out, String basedir) {
        if (file.isDirectory()) {
            System.out.println("压缩：" + basedir + file.getName());
            //调用压缩目录方法
            this.compressDirectory(file, out, basedir);
        } else {
            System.out.println("压缩：" + basedir + file.getName());
            //调用压缩文件方法
            this.compressFile(file, out, basedir);
        }
    }

    /**
     * 压缩一个目录
     *
     * @param dir     目录
     * @param out     zip输出流
     * @param basedir 基础路径前缀  例如: 第一层 “” 第二层 /
     */
    private void compressDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists()) {
            System.out.println("压缩目录不存在,请核实！");
            return;
        }
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            /* 递归 */
            compress(files[i], out, basedir + dir.getName() + "/");
        }
    }

    /**
     * 压缩一个文件
     *
     * @param file    文件
     * @param out     zip输出流
     * @param basedir 基础路径前缀  例如: 第一层 “” 第二层 /
     */
    private void compressFile(File file, ZipOutputStream out, String basedir) {
        if (!file.exists()) {
            System.out.println("压缩文件不存在,请核实！");
            return;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(basedir + file.getName());
            out.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * /**
     * 压缩指定文件或者文件夹
     * <p>
     * 压缩文件夹，压缩包中包含自己本身这一级文件夹
     * 例如:
     * 压缩后的zip包名: a.zip
     * 压缩目录: D:\1\
     * 压缩完成后:a.zip 包中不包含1这一级文件夹
     * </p>
     *
     * @param srcPathNameOrFileName
     */
    public void compressNotContainShell(String srcPathNameOrFileName, String zipFilePath) {
        ZipOutputStream out = null;
        try {
            File zipFile = getZipName(zipFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
            out = new ZipOutputStream(cos);
            String basedir = "";
            //循环遍历传入的文件或者文件夹的绝对路径的 可变参数
            File dir = new File(srcPathNameOrFileName);
            if (!dir.exists()) {
                System.out.println("压缩目录不存在,请核实！");
                return;
            }
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                compress(new File(String.valueOf(files[i])), out, basedir);
            }
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 以
     * @param pathName
     * @return
     */
    public File getZipName(String pathName) {
        return new File(pathName);
    }

    //--------------------------------------------单元测试--------------------------------------------
    public static void main(String[] args) throws IOException {
//        ZipCompressorUtil2 zc = new ZipCompressorUtil2();
//        String docPath = "C:\\Users\\ningm\\Desktop\\20221210\\";
//        String zipFileName = "C:\\Users\\ningm\\Desktop\\20221210.zip";
//        zc.compressNotContainShell(docPath, zipFileName);

        File file = new File("C:\\Users\\ningm\\Desktop\\科研项目归档信息\\attach");
        System.out.println(file.getParentFile().getAbsolutePath());
        File[] files = file.listFiles();
        for (File file1 : files) {
            System.out.println(file1.getCanonicalPath());
            System.out.println(file1.getAbsolutePath());
        }

        //        File from1 = new File("C:\\Users\\ningm\\Downloads\\项目结算表4088033534358231360.pdf");
//        File from2 = new File("C:\\Users\\ningm\\Downloads\\经费报销明细数据导入模版 (3).xls");
//        File from3 = new File("C:\\Users\\ningm\\Downloads\\Google Guava 官方教程 - v1.1.pdf");
//        File to1 = new File("C:\\Users\\ningm\\Desktop\\科研项目归档信息\\attach\\附件1.pdf");
//        File to2 = new File("C:\\Users\\ningm\\Desktop\\科研项目归档信息\\attach\\附件2.xls");
//        File to3 = new File("C:\\Users\\ningm\\Desktop\\科研项目归档信息\\attach\\附件3.pdf");
//        Files.createParentDirs(to1);
//        Files.copy(from1, to1);
//        Files.copy(from2, to2);
//        Files.copy(from3, to3);
//        File file = new File("C:\\Users\\ningm\\Desktop\\科研项目归档信息\\科研项目归档信息.xml");
//        Files.asCharSink(file, StandardCharsets.UTF_8).write("<data></data>");
//
//        File zip = ZipUtil.zip("C:\\Users\\ningm\\Desktop\\科研项目归档信息\\","C:\\Users\\ningm\\Desktop\\科研项目归档信息.zip", StandardCharsets.UTF_8, true);
    }
}
