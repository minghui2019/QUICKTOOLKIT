package com.eplugger.test;

import java.io.File;
import java.util.Arrays;

import javax.swing.filechooser.FileSystemView;

import lombok.extern.slf4j.Slf4j;
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
}
