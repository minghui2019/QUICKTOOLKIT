package com.eplugger.test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.filechooser.FileSystemView;

import bsh.EvalError;
import bsh.Interpreter;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.UUID;
import cn.hutool.http.HttpUtil;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import top.tobak.common.io.FileUtils;
import top.tobak.poi.excel.ExcelReader;
import top.tobak.poi.excel.ExcelWriter;
import top.tobak.thread.utils.ThreadPoolManager;
import top.tobak.utils.ExcelUtils;


@Slf4j
public class Test3 {
	@Test
	public void testName() throws Exception {
		File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
	    String desktopPath = desktopDir.getAbsolutePath();
	    log.debug("desktopPath = " + desktopPath + File.separator);
	}

	@Test
	public void testName1() {
		for (int i = 0; i < 6; i++) {
			System.out.println(2 * (i % 2));
			System.out.println(2 * (i % 2) + 1);
		}
	}

	@Test
	public void testName2() {
		String[] str = new String[8];
		for (int i = 0; i < 6; i++) {
			str[4 * (i % 2)] = "meta.getMeaning()";
			str[4 * (i % 2) + 1] = "valueStr";
			if (i % 2 == 1 || i == 6 - 1) {
				System.out.println(Arrays.toString(str));
			}
		}
	}

	@Test
	public void testName3() throws IOException, InterruptedException {
		WebClient webClient;
		webClient = new WebClient(BrowserVersion.FIREFOX_38);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getCookieManager().setCookiesEnabled(true);
		HtmlPage curpage = webClient.getPage("https://blog.sina.com.cn/s/articlelist_1766037260_0_1.html");
		Thread.sleep(3000);
		HtmlDivision column_2 = (HtmlDivision) curpage.getElementById("column_2");
		System.out.println(column_2);
	}

	/**
	 * @return java.lang.String
	 * @author
	 * @description 从网络URL中下载文件
	 * @date 15:33 2021/11/2
	 * @params [fileUrl, diskPath]
	 */
	public static Long downLoadFromUrl(String fileUrl, String diskPath) {
		if (fileUrl == null) {
			return 0l;
		}
		//文件后缀
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("."));
		fileName = Strings.isNullOrEmpty(fileName) ? ".jpg" : fileName;
		try {
			String uuidName = UUID.randomUUID().toString();
			File file = new File(diskPath);
			if (!file.exists()) {
				//创建文件夹
				boolean mkdir = file.mkdir();
				if (!mkdir) {
					throw new RuntimeException("创建文件夹失败，路径为：" + diskPath);
				}
			}
			String path = diskPath + File.separator + uuidName + fileName;
			Stopwatch watch = Stopwatch.createUnstarted();
			//带进度显示的文件下载
			return HttpUtil.downloadFile(fileUrl, FileUtil.file(path), new StreamProgress() {
				@Override
				public void start() {
					watch.start();
					Console.log("开始下载");
				}

				@Override
				public void progress(long progressSize) {
					Console.log("已下载：{}", FileUtil.readableFileSize(progressSize));
				}

				@Override
				public void finish() {
					watch.stop();
					Console.log("下载完成，用时：" + watch.elapsed(TimeUnit.MILLISECONDS));
				}
			});
		} catch (Exception e) {
			Console.log("下载异常，异常信息为：" + e.getMessage());
		}
		return 0l;
	}

	private List<Map<String, String>> parseHtml(int i) throws InterruptedException, IOException {
		Document document;
		List<Map<String, String>> data = Lists.newArrayList();
		String url = "https://blog.sina.com.cn/s/articlelist_1766037260_0_" + i + ".html";
		log.debug("正在爬虫页面，url: {}", url);
		try {
			document = Jsoup.parse(new URL(url), 5000);
		} catch (Exception e) {
			log.error("爬虫失败，url: {}", url);
			Thread.sleep(3000);
			document = Jsoup.parse(new URL(url), 5000);
		}
		Thread.sleep(300);
		Element column_2 = document.getElementById("column_2");
		Elements articleList = column_2.getElementsByClass("articleList");
		Elements a = articleList.first().getElementsByTag("a");
		for (Element element1 : a) {
			Map<String, String> map = Maps.newLinkedHashMap();
			String href = element1.attr("href");
			href = "https:" + href;
			log.debug("正在爬虫页面，href: {}", href);
			Document document1;
			Thread.sleep(300);
			try {
				document1 = Jsoup.parse(new URL(href), 5000);
			} catch (Exception e) {
				log.error("爬虫失败，第二次，href: {}", href);
				Thread.sleep(3000);
				document1 = Jsoup.parse(new URL(href), 5000);
			}

			Elements articalTitle = document1.getElementsByClass("articalTitle");
			String h2Text = articalTitle.first().getElementsByTag("h2").text();
			try {
				h2Text = h2Text.trim();
				h2Text = h2Text.replace("－", "-");
				int index1 = h2Text.indexOf(".");
				index1 = index1 < 0 ? 0 : index1;
				int index2 = h2Text.indexOf("-", index1);
				index2 = index2 < 0 ? 1 : index2;
				int index3 = index2 + 1;
				if (index1 > index2) {
					int index11;
					if ((index11 = h2Text.indexOf("-")) != -1) {
						index1 = index11;
						index3 = index2 = index1 + 1;
					} else {
						if ((index11 = h2Text.indexOf(".")) != -1) {
							index1 = index11;
						} else {
							index1 = 0;
						}
						index3 = index2 = index1 + 5;
					}
				}
				String artist = h2Text.substring(0, index1);
				String date = h2Text.substring(index1 + 1, index2);
				String title = h2Text.substring(index3);
				map.put("艺术家", artist);
				map.put("日期", date);
				map.put("标题", title);
			} catch (NullPointerException e) {
				map.put("艺术家", "");
				map.put("日期", "");
				map.put("标题", h2Text);
			}
			Element sina_keyword_ad_area2 = document1.getElementById("sina_keyword_ad_area2");
			Elements divs = sina_keyword_ad_area2.children();
			String prevText = null;
			for (Element div : divs) {
				Elements a1 = div.getElementsByTag("a");
				if (a1.isEmpty()) {
					prevText = div.text();
					continue;
				}
				Element first = a1.first();
				String href1 = first.attr("href");
				if (href1.indexOf("ctfile") != -1) {
					Map<String, String> map1 = Maps.newLinkedHashMap();
					map1.putAll(map);
					map1.put("小标题", prevText);
					map1.put("网盘地址", href1);
					data.add(map1);
				}
			}
		}
		return data;
	}

	@Test
	public void testName4() throws IOException, InterruptedException {
		List<Map<String, String>> data = Lists.newArrayList();
		for (int i = 117; i < 120; i++) {
			data.addAll(parseHtml(i));
			if (i % 2 == 0 || i == 119) {
				outExcel(data, i);
				data.clear();
			}
		}
	}

	private void outExcel(List<Map<String, String>> data, int index) throws IOException {
		ExcelWriter writer = ExcelUtils.getWriter(true);
		writer.setColumnWidth(0, 10);
		writer.setColumnWidth(1, 10);
		writer.setColumnWidth(2, 50);
		writer.setColumnWidth(3, 50);
		writer.setColumnWidth(4, 50);
		Workbook workbook = writer.write(data).getWorkbook();
		ExcelUtils.outExcel(workbook, new File("./xlsx/", "test" + index + ".xlsx").getCanonicalFile());
	}

	private void outExcel(List<Map<String, String>> data, String fileName) throws IOException {
		ExcelWriter writer = ExcelUtils.getWriter(true);
		writer.setColumnWidth(0, 10);
		writer.setColumnWidth(1, 10);
		writer.setColumnWidth(2, 50);
		writer.setColumnWidth(3, 50);
		writer.setColumnWidth(4, 50);
		writer.setColumnWidth(5, 50);
		writer.setColumnWidth(6, 50);
		Workbook workbook = writer.write(data).getWorkbook();
		ExcelUtils.outExcel(workbook, new File("./xlsx/", fileName).getCanonicalFile());
	}

	@Test
	public void testName5() {
		String h2Text = "陈小霞-大脚姐仔【百佳唱片NO.16】【EMI百代】【WAV+CUE】";
		int index1 = h2Text.indexOf(".");
		index1 = index1 < 0 ? 0 : index1;
		int index2 = h2Text.indexOf("-", index1);
		index2 = index2 < 0 ? 1 : index2;
		int index3 = index2 + 1;
		if (index1 > index2) {
			int index11 = h2Text.indexOf("-");
			if (index11 != -1) {
				index1 = index11;
				index3 = index2 = index1 + 1;
			} else {
				index11 = h2Text.indexOf(".");
				if (index11 != -1) {
					index1 = index11;
				} else {
					index1 = 0;
				}
				index3 = index2 = index1 + 5;
			}
		}
		String artist = h2Text.substring(0, index1);
		String date = h2Text.substring(index1 + 1, index2);
		String title = h2Text.substring(index3);
		System.out.println(artist);
		System.out.println(date);
		System.out.println(title);

	}

	@Test
	public void testName6() throws IOException {
		File file = FileUtils.getFile("./xlsx/test.txt");
		List<String> lines = FileUtils.readLines(file, "utf-8");
		Set<String> sets = Sets.newHashSet();
		for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();){
			String line = iterator.next();
			if (sets.contains(line)) {
				iterator.remove();
				continue;
			}
			sets.add(line);
		}

		File filecopy = FileUtils.getFile("./xlsx/test_copy.txt");

		FileUtils.writeLines(filecopy, "utf-8", lines);
	}

	@Test
	public void testName7() throws IOException, InterruptedException {
		// 已爬取的链接
		File file = FileUtils.getFile("./xlsx/links.txt");
		List<String> lines = FileUtils.readLines(file, "utf-8");
		Set<String> sets = Sets.newHashSet(lines);
		// 检查网站还有哪些没爬取
		List<Map<String, String>> data = Lists.newArrayList();
		for (int i = 1; i < 120; i++) {
			data.addAll(parseHtml(i, sets));
		}
		outExcel(data, "albums_last.xlsx");
		FileUtils.writeLines(file, "utf-8", Lists.newArrayList(sets));
		data.clear();
		sets.clear();
		lines.clear();
	}

	@Test
	public void testName71() throws IOException, InterruptedException {
		// 已爬取的链接
		File file = FileUtils.getFile("./xlsx/links.txt");
		List<String> lines = FileUtils.readLines(file, "utf-8");
		Set<String> sets = Sets.newHashSet(lines);
		ThreadPoolManager manager = ThreadPoolManager.getManager();
//		manager.submitTask()
		// 检查网站还有哪些没爬取
		List<Map<String, String>> data = Lists.newArrayList();
		for (int i = 1; i < 120; i++) {
			data.addAll(parseHtml(i, sets));
		}
		outExcel(data, "albums_last.xlsx");
		FileUtils.writeLines(file, "utf-8", Lists.newArrayList(sets));
		data.clear();
		sets.clear();
		lines.clear();
	}

	private List<Map<String, String>> parseHtml(int i, Set<String> sets) throws InterruptedException {
		List<Map<String, String>> data = Lists.newArrayList();
		String url = "https://blog.sina.com.cn/s/articlelist_1766037260_0_" + i + ".html";
		Document document = parse(url, 1);
		Thread.sleep(500);
		Element column_2 = document.getElementById("column_2");
		Elements articleList = column_2.getElementsByClass("articleList");
		// 最后一页
		if (articleList.size() == 0) {
			return data;
		}
		Elements a = articleList.first().getElementsByTag("a");
		for (Iterator<Element> it = a.iterator(); it.hasNext(); ) {
			Map<String, String> map = Maps.newLinkedHashMap();
			Element element = it.next();
			String href = element.attr("href");
			href = "https:" + href;
			if (checkHref(href, sets)) {
				continue;
			}
			Document document1 = parse(href, 1);
			Thread.sleep(300);
			Elements articalTitle = document1.getElementsByClass("articalTitle");
			String h2Text = articalTitle.first().getElementsByTag("h2").text();
			map.putAll(parse(h2Text));

			Element sina_keyword_ad_area2 = document1.getElementById("sina_keyword_ad_area2");
			Elements divs = sina_keyword_ad_area2.children();
			String prevText = null;
			for (Element div : divs) {
				Elements a1 = div.getElementsByTag("a");
				if (a1.isEmpty()) {
					prevText = div.text();
					continue;
				}
				Element first = a1.first();
				String href1 = first.attr("href");
				if (href1.indexOf("ctfile") != -1 || href1.indexOf("545c") != -1 || href1.indexOf("t00y") != -1 || href1.indexOf("sn9") != -1 || href1.indexOf("pipipan") != -1) {
					Map<String, String> map1 = Maps.newLinkedHashMap();
					map1.putAll(map);
					map1.put("小标题", prevText);
					map1.put("网盘地址", href1);
					map1.put("原链接地址", href);
					data.add(map1);
				} else if (href1.indexOf("sina") == -1) {
					Map<String, String> map1 = Maps.newLinkedHashMap();
					map1.putAll(map);
					map1.put("小标题", "");
					map1.put("网盘地址", "");
					map1.put("原链接地址", href);
					data.add(map1);
				}
			}
			sets.add(href);
		}
		return data;
	}

	private Map<String, String> parse(String text) {
		if (Strings.isNullOrEmpty(text)) {
			return Maps.newHashMap();
		}
		Map<String, String> map = Maps.newLinkedHashMap();
		text = text.trim().replace("－", "-");
		int index11 = text.indexOf(".");
		int index22 = text.indexOf("-");
		try {
			if (index22 - index11 == 5 && index22 != -1 && index11 != -1) { // 艺术家.年度-专辑名...
				int index21 = index11 + 1;
				int index31 = index22 + 1;
				String artist = text.substring(0, index11);
				String date = text.substring(index21, index22);
				String title = text.substring(index31);
				map.put("艺术家", artist);
				map.put("日期", date);
				map.put("专辑名", title);
				map.put("标题", text);
			} else if (index11 == -1 && index22 > 5) { // 艺术家年度-专辑名...
				int index21 = index11 = index22 - 4;
				int index31 = index22 + 1;
				String artist = text.substring(0, index11);
				String date = text.substring(index21, index22);
				String title = text.substring(index31);
				map.put("艺术家", artist);
				map.put("日期", date);
				map.put("专辑名", title);
				map.put("标题", text);
			} else {
				map.put("艺术家", "");
				map.put("日期", "");
				map.put("专辑名", "");
				map.put("标题", text);
			}
		} catch (Exception e) {
			map.put("艺术家", "");
			map.put("日期", "");
			map.put("专辑名", "");
			map.put("标题", text);
		}
		return map;
	}

	/**
	 * true跳过不爬虫
	 * @param href
	 * @param sets
	 * @return
	 */
	private boolean checkHref(String href, Set<String> sets) {
		if (Strings.isNullOrEmpty(href)) {
			return true;
		}
		if (sets.isEmpty()) {
			return false;
		}

 		return sets.contains(href);
	}

	@Test
	public void testName8() {
		String href = "林美英1995-我在你左右·林美英珍藏";
		Assert.assertTrue(href.indexOf("-") == 7);
		Assert.assertTrue(href.substring(0, href.indexOf("-") - 4).equals("林美英"));
		Assert.assertTrue(href.substring(href.indexOf("-") - 4, href.indexOf("-")).equals("1995"));
		Assert.assertTrue(href.substring(href.indexOf("-") + 1).equals("我在你左右·林美英珍藏"));
	}

	private Document parse(String url, int count) {
		if (count == 1) {
			log.debug("正在爬虫页面，url: {}", url);
		} else {
			log.error("爬虫失败，重试第" + count + "次，url: {}", url);
			try {
				Thread.sleep(1000 * (count - 1));
			} catch (InterruptedException e) {
			}
		}
		try {
			return Jsoup.parse(new URL(url), 5000);
		} catch (Exception e) {
			return parse(url, count + 1);
		}
	}

	@Test
	public void testName9() throws IOException {
		File dir = FileUtils.getFile("xlsx/");
		if (!dir.isDirectory()) {
			return;
		}
		File[] files = dir.listFiles();
		Set<String> sets = Sets.newHashSet();
		List<Map<String, Object>> maps = Lists.newArrayList();
		for (File file : files) {
			if (file.getName().indexOf("xlsx") == -1) {
				continue;
			}
			ExcelReader reader = ExcelUtils.getReader(file);
			List<Map<String, Object>> maps_ = reader.readAll();
			for (Iterator<Map<String, Object>> iterator = maps_.iterator(); iterator.hasNext();) {
				Map<String, Object> next = iterator.next();
				String value = next.get("网盘地址").toString();
				if (sets.contains(value)) {
					log.error("网盘地址：{}", value);
					iterator.remove();
					continue;
				}
				sets.add(value);
			}
			maps.addAll(maps_);
		}
		outExcel1(maps, "albums.xlsx");
	}

	private void outExcel1(List<Map<String, Object>> data, String filename) throws IOException {
		ExcelWriter writer = ExcelUtils.getWriter(true);
		writer.setColumnWidth(0, 10);
		writer.setColumnWidth(1, 10);
		writer.setColumnWidth(2, 50);
		writer.setColumnWidth(3, 50);
		writer.setColumnWidth(4, 50);
		Workbook workbook = writer.write(data).getWorkbook();
		ExcelUtils.outExcel(workbook, new File("xlsx/", filename).getCanonicalFile());
	}

	@Test
	public void testName10() {
		String str1 = "V8.5.6.1_update1.0.sql";
		String str2 = "V8.5.6.1_update9.9.sql";
		String str3 = "V8.5.6.1_update10.0.sql";

		boolean isStr1Newer = isNewerVersion(str1, str2);
		boolean isStr3Newer = isNewerVersion(str3, str2);

		System.out.println("str1 是否比 str2 新：" + isStr1Newer);
		System.out.println("str3 是否比 str2 新：" + isStr3Newer);

		String str4 = "V8.5.6.1_update1_0.sql";
		String str5 = "V8.5.6.1_update9_9.sql";
		String str6 = "V8.5.6.1_update10_0.sql";


	}

	public static boolean isNewerVersion(String str1, String str2) {
		String update = str1.substring(0, str1.lastIndexOf("_update") + "_update".length());
		str1 = str1.replace(update, "");
		str2 = str2.replace(update, "");
		// 提取版本号部分
		String version1 = str1.replaceAll("[^\\d.]", "");
		String version2 = str2.replaceAll("[^\\d.]", "");

		// 比较版本号
		String[] arr1 = version1.split("\\.");
		String[] arr2 = version2.split("\\.");

		for (int i = 0; i < Math.min(arr1.length, arr2.length); i++) {
			int num1 = Integer.parseInt(arr1[i]);
			int num2 = Integer.parseInt(arr2[i]);
			if (num1 != num2) {
				return num1 > num2; // 如果num1大于num2，则str1更新
			}
		}

		// 如果版本号长度不同，长度更长的版本更新
		return arr1.length > arr2.length;
	}

	@Test
	public void testName11() throws EvalError {
		Interpreter interpreter = new Interpreter();
		double value = (double) interpreter.eval("Math.round(1/6.0 * 100)/ 100.0");
		System.out.println(value);
		System.out.println(BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP));
		System.out.println(Math.round(value * 100) / 100.0);
	}

	@Test
	public void testName12() {
		Integer i1 = 1000;
		Double d1 = 1000d;
		System.out.println(new BigDecimal(i1));
		System.out.println(new BigDecimal(d1));
	}
}
