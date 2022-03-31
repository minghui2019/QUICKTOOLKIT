package com.eplugger.main;

import org.junit.Test;

public class BookTest {
	@Test
	public void testNewBook() {
		Book book = new Book();
		book.setName("计算机入门");
		book.setPrice(50);
		System.out.println(book.getName());
		System.out.println(book);
	}
}
