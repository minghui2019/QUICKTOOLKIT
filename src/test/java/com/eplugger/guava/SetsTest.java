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
	
	@Test
	public void testDifference1() {
		Set<String> newSets = Sets.newHashSet("王者荣耀", "英雄联盟", "地下城与勇士", "魔兽世界", "魔兽世界1", "魔兽世界2");
		Set<String> delSets = Sets.newHashSet("穿越火线");
		Set<String> oldSets = Sets.newHashSet("王者荣耀", "英雄联盟", "地下城与勇士", "穿越火线");
		Set<String> setView = Sets.difference(newSets, Sets.difference(oldSets, delSets));
		log.debug(setView.toString());
	}
	
	/**
	 * 并集
	 * @throws Exception
	 */
	@Test
	public void testUnion() {
		Set<String> setView = Sets.union(set1, set2);
		log.debug(setView.toString());
	}
}
