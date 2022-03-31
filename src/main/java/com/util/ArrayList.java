package com.util;

import java.util.Iterator;

public class ArrayList<E> extends java.util.ArrayList<E> {
	private static final long serialVersionUID = 7349374581443298524L;
	@Override
	public String toString() {
		Iterator<E> it = iterator();
        if (! it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (! it.hasNext())
                return sb.append(']').toString();
            sb.append(',').append(' ').append('\n');
        }
	}

}
