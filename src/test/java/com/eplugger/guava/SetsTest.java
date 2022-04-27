package com.eplugger.guava;

import static org.junit.Assert.assertNotNull;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SetsTest {
	private Set<String> set1;
	private Set<String> set2;
	
	@Before
	public void Before() {
		set1 = Sets.newHashSet("王者荣耀", "英雄联盟", "穿越火线", "地下城与勇士");
		set2 = Sets.newHashSet("王者荣耀", "地下城与勇士", "魔兽世界");
	}
	
	/**
	 * 交集
	 * @throws Exception
	 */
	@Test
	public void testIntersection() {
		Set<String> setView = Sets.intersection(set1, set2);
		assertNotNull(setView);
		log.debug(setView.toString());
	}
	
	/**
	 * 差集
	 * @throws Exception
	 */
	@Test
	public void testDifference() {
		Set<String> setView = Sets.difference(set1, set2);
		log.debug(setView.toString());
	}
	
	/**
	 * 并集
	 * @throws Exception
	 */
	@Test
	public void testName() {
		Set<String> setView = Sets.union(set1, set2);
		log.debug(setView.toString());
	}
}
