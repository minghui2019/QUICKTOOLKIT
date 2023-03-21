package com.eplugger.guava;

import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class SplitterTest {
	/**
	 * print: a;b;c
	 * @throws Exception
	 */
	@Test
	public void testSplit() throws Exception {
		Splitter splitter = Splitter.on(";");
		log.debug(splitter.splitToList("a;b;c").toString());
	}
}
