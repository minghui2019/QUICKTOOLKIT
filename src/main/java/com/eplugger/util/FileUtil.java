package com.eplugger.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class FileUtil {
	/**
	 * <p>判断目录是否存在（不存在则创建）</p>
	 * @param destDirName 路径
	 */
    public static void isDir(String destDirName) {
    	File file = new File(destDirName);
        if (file.exists()) {
        	return;
        }
        file.mkdirs();
    }
    
    /**
     * <p>写出文件</p>
     * @param str 代输出内容，为空则return
     * @param filePath 路径
     * @param fileName 文件名，包括后缀名
     */
    public static void outFile(String str, String filePath, String fileName) {
    	if (StringUtils.isBlank(str)) {
    		return;
    	}
    	FileUtil.isDir(filePath);
    	File file = new File(filePath + File.separator + fileName);
		if (file.exists()) {
			System.out.println("文件" + fileName + "已存在，执行删除！");
			file.delete();
		}
		try {
			file.createNewFile();
			FileWriter fw = new FileWriter(file);
			fw.write(str);
			fw.flush();
			fw.close();
			System.out.println("文件" + fileName + "创建成功！");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	/**
	 * 读取xml文件
	 * @param filePath 文件路径
	 * @return
	 * @throws DocumentException
	 */
	public static Document readXmlFile(String filePath) throws DocumentException {
		//1.创建Reader对象
        SAXReader reader = new SAXReader();
        //2.加载xml
        return reader.read(new File(filePath));
	}
}
