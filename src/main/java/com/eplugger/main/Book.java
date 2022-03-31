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
	public String getName() {
		System.out.println("Book.getName()");
		return name;
	}
	public void setName(String name) {
		System.out.println("Book.setName()");
		this.name = name;
	}
	@Override
	public String toString() {
		return "Book [name=" + name + ", price=" + price + "]";
	}
//    @Override
//    public boolean equals(final Object obj) {
//      if (obj == null) {
//         return false;
//      }
//      final Book book = (Book) obj;
//      if (this == book) {
//         return true;
//      } else {
//         return (this.name.equals(book.name) && this.price == book.price);
//      }
//    }
//    @Override
//    public int hashCode() {
//      int hashno = 7;
//      hashno = 13 * hashno + (name == null ? 0 : name.hashCode());
//      return hashno;
//    }
} 