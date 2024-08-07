package com.eplugger.guava;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class JoinerTest {
	/**
	 * 常规的分割器
	 * print: a;b;c
	 * @throws Exception
	 */
	@Test
	public void testJoin() throws Exception {
		Joiner joiner = Joiner.on(";");
		log.debug(joiner.join(new String[] { "a", "b", "c" }));
	}
	
	/**
	 * print: a;c
	 * @throws Exception
	 */
	@Test
	public void testSkipNulls() throws Exception {
		Joiner joiner = Joiner.on(";").skipNulls();
		log.debug(joiner.join(new String[] { "a", null, "c" }));
	}
	
	/**
	 * print: a;!;c
	 * @throws Exception
	 */
	@Test
	public void testUseForNull() throws Exception {
		Joiner joiner = Joiner.on(";").useForNull("!");
		log.debug(joiner.join(new String[] { "a", null, "c" }));
	}
	
	/**
	 * print: start: a;b;c
	 * @throws Exception
	 */
	@Test
	public void testAppendTo() throws Exception {
		Joiner joiner = Joiner.on(";");
		StringBuilder ab = new StringBuilder("start: ");
		log.debug(joiner.appendTo(ab, new String[]{"a","b","c"}).toString());
	}

	/**
	 * 1->a;2->b
	 * @throws Exception
	 */
	@Test
	public void testMapJoiner() throws Exception {
		Map<Integer, String> map = Maps.newHashMap();
		map.put(1, "a");
		map.put(2, "b");
		MapJoiner joiner = Joiner.on(";").withKeyValueSeparator("->");
		log.debug(joiner.join(map));
	}

	/**
	 * print: start: a;b;c
	 * @throws Exception
	 */
	@Test
	public void testAppendTo2() throws Exception {
		String[] array = { "a", "b", "c" };
		String collect = Arrays.stream(array).map(s -> "'" + s + "'").collect(Collectors.joining(","));
		log.debug(collect);
	}
}
