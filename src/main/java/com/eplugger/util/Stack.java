package com.eplugger.util;

import java.util.LinkedList;

import com.google.common.collect.Lists;
 
/**
 * LinkedList实现栈
 * @param <T>
 */
public class Stack<T> {
	private LinkedList<T> storage = Lists.newLinkedList();
	
	/**
	 * 添加一个新元素到栈顶位置
	 * @param v
	 */
	public void push(T t) {
		storage.addFirst(t);
	}
	
	/**
	 * 返回栈顶的元素，不对栈做任何修改
	 * @return
	 */
	public T peek() {
		return storage.getFirst();
	}
	
	/**
	 * 移除栈顶的元素，同时返回被移除的元素
	 * @return
	 */
	public T pop() {
		return storage.removeFirst();
	}
	
	/**
	 * 如果栈里没有任何元素就返回true，否则返回false。
	 * @return
	 */
	public boolean isEmpty() {
		return storage.isEmpty();
	}
	
	/**
	 * 返回栈里的元素个数
	 * @return
	 */
	public int size() {
		return storage.size();
	}
	
	/**
	 * 移除栈里的所有元素
	 */
	public void clear() {
		storage.clear();
	}
	
	@Override
	public String toString() {
		return storage.toString();
	}
}