package com.eplugger.main;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private String name;
    private int price;
	@Override
	public String toString() {
		return "Book [name=" + name + ", price=" + price + "]";
	}
} 