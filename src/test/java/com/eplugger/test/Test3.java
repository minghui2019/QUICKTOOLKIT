package com.eplugger.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import javax.swing.filechooser.FileSystemView;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;


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

	@Test
	public void testName4() throws IOException {
		Document document = Jsoup.parse(new URL("https://blog.sina.com.cn/s/articlelist_1766037260_0_1.html"), 3000);
		Element column_2 = document.getElementById("column_2");
		Elements articleList = column_2.getElementsByClass("articleList");
		Element element = articleList.get(0);
		Elements a = element.getElementsByTag("a");
		int i = 0;
		for (Element element1 : a) {
			String href = element1.attr("href");
			Document document1 = Jsoup.parse(new URL("https:" + href), 3000);
			Elements articalTitle = document1.getElementsByClass("articalTitle");
			System.out.println(articalTitle.first().getElementsByTag("h2").text());
			Element sina_keyword_ad_area2 = document1.getElementById("sina_keyword_ad_area2");
			Elements divs = sina_keyword_ad_area2.getElementsByTag("div");
			for (Element div : divs) {
				Elements a1 = div.getElementsByTag("a");
				if (a1.isEmpty()) {
					continue;
				}
				Element first = a1.first();
				String href1 = first.attr("href");
				if (href1.indexOf("ctfile") != -1) {
					System.out.println(href1);
				}
			}
			if (i == 2) {
				break;
			}
			i++;
		}
	}
}
