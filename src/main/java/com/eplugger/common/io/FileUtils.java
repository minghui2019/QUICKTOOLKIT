package com.eplugger.common.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.eplugger.common.lang.StringUtils;
import com.eplugger.utils.DateUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件工具
 */
@Slf4j
public class FileUtils {

	/**
	 * 创建文件所在目录
	 *
	 * @param file 文件对象
	 * @return 创建成功返回true, 否则返回false
	 */
	public static boolean createFileParentDir(File file) {
		File dir = file.getParentFile();
		if (!dir.exists())
			return dir.mkdirs();
		return false;
	}

	/**
	 * 创建文件, 已存在就删除
	 *
	 * @param file 目标文件
	 * @return 创建成功返回true, 否则返回false
	 */
	public static boolean createFile(File file) {
		createFileParentDir(file);
		if (file.exists() && file.delete())
			log.debug("删除已存在文件");

		try {
			return file.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 列出目录中所有文件
	 *
	 * @param dir 目录
	 * @return 文件映射, Key-文件名, Value-文件
	 */
	public static Map<String, File> mapDir(File dir) {
		if (null == dir || dir.isFile())
			return null;
		Map<String, File> map = Maps.newLinkedHashMap();
		File[] files = dir.listFiles();
		if (null == files)
			return map;
		for (File file : files)
			map.put(file.getName(), file);
		return map;
	}

	/**
	 * 读取文件内容
	 *
	 * @param file    文件
	 * @param charSet 字符集
	 * @return 内容列表
	 */
	public static List<String> readLines(File file, String charSet) {
		try {
			return Files.readLines(file, Charset.forName(charSet));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取文件名后缀, 不包含点号(.)
	 *
	 * @param fileName 文件名
	 * @return 文件名后缀
	 */
	private static String getSuffix(String fileName) {
		fileName = Strings.nullToEmpty(fileName);

		int index = fileName.lastIndexOf('.');
		return index == -1 ? StringUtils.EMPTY : fileName.substring(index + 1);
	}
	
	/**
	 * 获取文件名, 不包含点号(.)和后缀(文件类型)
	 * @param fileName
	 * @return
	 */
	private static String getPrefix(String fileName) {
		fileName = Strings.nullToEmpty(fileName);

		int index = fileName.lastIndexOf('.');
		return index == -1 ? StringUtils.EMPTY : fileName.substring(0, index);
	}

	/**
	 * 从文件末尾开始读取文本行
	 * 
	 * <pre>
	 * 1. 如果0>=lineTotal总是返回空列表;
	 * 2. 如果lineTotal>maxLines列表长度为文件总行数;
	 * 3. 否则列表长度和lineTotal相等;
	 * </pre>
	 *
	 * @param file      文件对象
	 * @param lineTotal 期望读取的总行数
	 * @return 文本信息列表, 顺序与文件内容一致
	 */
	public static List<String> reverseReadLines(File file, int lineTotal) {
		List<String> lines = Lists.newArrayList();
		int surplus = lineTotal;
		if (0 >= surplus)
			return lines;

		try (RandomAccessFile rf = new RandomAccessFile(file, "r")) {
			long len = rf.length();
			long start = rf.getFilePointer();

			// 从最后一个字节开始读取
			long nextEnd = start + len - 1;
			rf.seek(nextEnd);

			int c;
			while (nextEnd > start) {

				// 当前读取的字节正好是换行符
				// 读取这一行
				c = rf.read();
				if (c == '\n' || c == '\r') {
					String line = rf.readLine();
					if (null != line) {
						lines.add(line);
						if (0 >= --surplus)
							break;
					}

					nextEnd--;
				}

				nextEnd--;
				rf.seek(nextEnd);

				// 当文件指针退至文件开始处，输出第一行
				if (nextEnd == 0) {
					String line = rf.readLine();
					if (null != line) {
						lines.add(line);
						if (0 >= --surplus)
							break;
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		Collections.reverse(lines);
		List<String> ret = Lists.newArrayList();
		for (String s : lines)
			ret.add(StringUtils.encode(s, "ISO-8859-1", StandardCharsets.UTF_8.name()));

		return ret;
	}

	/**
	 * <pre>
	 * 向硬盘输出文件
	 * 注意：
	 * 默认文件编码格式为：UTF-8
	 * 源文件存在则备份源文件，否则创建新文件
	 * </pre>
	 * @param pathname
	 * @param data
	 */
	public static void writeAndBackupSrcFile(String pathname, CharSequence data) {
		try {
			write(new File(pathname), data, StandardCharsets.UTF_8, false, true);
			log.debug("文件 (" + pathname + ") 创建成功！");
		} catch (IOException e) {
			log.error(e.getMessage() + "文件无法输出: {}", new Object[] {pathname} );
		}
	}
	
	/**
	 * <pre>
	 * 向硬盘输出文件
	 * 注意：
	 * 默认文件编码格式为：UTF-8
	 * 源文件存在则覆盖源文件，否则创建新文件
	 * </pre>
	 * @param pathname
	 * @param data
	 */
	public static void write(String pathname, CharSequence data) {
		try {
			write(new File(pathname), data, StandardCharsets.UTF_8, false, false);
			log.debug("文件 (" + pathname + ") 创建成功！");
		} catch (IOException e) {
			log.error(e.getMessage() + "文件无法输出: {}", new Object[] {pathname} );
		}
	}
	
	/**
	 * <pre>
	 * 向硬盘输出文件
	 * 注意：
	 * 默认文件编码格式为：UTF-8
	 * 源文件存在则在源文件末尾追加新内容，否则创建新文件
	 * </pre>
	 * @param pathname
	 * @param data
	 * @param append
	 * @throws IOException
	 */
	public static void write(String pathname, CharSequence data, boolean append) throws IOException {
		write(new File(pathname), data, StandardCharsets.UTF_8, append, false);
	}
	
	/**
	 * 向硬盘输出文件
	 * @param file 文件的路径
	 * @param data 文件输出内容
	 * @param charset 字符编码
	 * @param append 是否在文件末尾追加
	 * @throws IOException
	 */
	public static void write(File file, CharSequence data, Charset charset, boolean append) throws IOException {
		write(file, data, charset, append, false);
	}
	
	/**
	 * 向硬盘输出文件，选择是否要备份源文件
	 * @param file 文件的路径
	 * @param data 文件输出内容
	 * @param charset 字符编码
	 * @param append 是否在文件末尾追加
	 * @param isBackupSrcFile 如果文件已存在，是否要备份。<code>true</code>: 备份; <code>false</code>: 不备份，直接删除
	 * @throws IOException
	 */
	public static void write(File file, CharSequence data, Charset charset, boolean append, boolean isBackupSrcFile) throws IOException {
		if (data == null || data.length() == 0)
			return;
		
		createFileParentDir(file);
		
		if (file.exists()) {
			if (isBackupSrcFile) {
				String canonicalPath = file.getCanonicalPath();
				String prefix = getPrefix(canonicalPath);
				String suffix = getSuffix(canonicalPath);
				File newFile = new File(prefix + DateUtils.formatTimeNoSeparator() + "." + suffix);
				Files.copy(file, newFile);
				log.debug("文件 (" + canonicalPath + ") 已存在，执行备份！");
			} else {
				log.debug("文件 (" + file.getCanonicalPath() + ") 已存在，执行删除！");
			}
			file.delete();
		}
		
		if (append) {
			Files.asCharSink(file, charset, FileWriteMode.APPEND).write(data);
		} else {
			Files.asCharSink(file, charset).write(data);
		}
	}
	
	/**
	 * 获取File文件对象
	 * 检查path是否为空
	 *
	 * @return File File文件对象
	 */
	public static File getFile(String path) {
		if (Strings.isNullOrEmpty(path)) {
			throw new RuntimeException("路径无效[path=" + path + "]");
		}
		return new File(path);
	}
}
