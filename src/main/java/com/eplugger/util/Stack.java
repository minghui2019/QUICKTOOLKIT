package com.eplugger.util;

import java.util.LinkedList;
 
/**
 * LinkedList实现栈
 * @param <T>
 */
public class Stack<T> {
	private LinkedList<T> storage = new LinkedList<T>();
	
	/**
	 * 入栈
	 * @param v
	 */
	public void push(T v) {
		storage.addFirst(v);
	}
	
	/**
	 * 检查栈首元素
	 * @return
	 */
	public T peek() {
		return storage.getFirst();
	}
	
	/**
	 * 出栈
	 * @return
	 */
	public T pop() {
		return storage.removeFirst();
	}
	
	/**
	 * 检查栈是否已空
	 * @return
	 */
	public boolean empty() {
		return storage.isEmpty();
	}
	
	@Override
	public String toString() {
		return storage.toString();
	}
}