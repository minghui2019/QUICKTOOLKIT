package com.eplugger.java8;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 去重：根据javaBean的某个属性去重
 * @author minghui
 *
 */
@Slf4j
public class DistinctByPropertyTest {
	@Test
	public void testDistinctByProperty() throws Exception {
		List<Book> list = Lists.newArrayList();
		list.add(new Book("Core Java", 200));
		list.add(new Book("Core Java", 300));
		list.add(new Book("Learning Freemarker", 150));
		list.add(new Book("Spring MVC", 200));
		list.add(new Book("Hibernate", 300));
		list.stream().filter(distinctByKey(Book::getName)).forEach(a -> log.debug(a.toString()));
	}
	
	private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = Maps.newConcurrentMap();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
}

@Data
@AllArgsConstructor
class Book {
	private String name;
	private int price;
}