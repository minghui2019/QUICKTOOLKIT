package com.eplugger.test;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Test3 {
	@Test
	public void testName() throws Exception {
		File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
	    String desktopPath = desktopDir.getAbsolutePath();
	    log.debug("desktopPath = " + desktopPath + File.separator);
	}
}
