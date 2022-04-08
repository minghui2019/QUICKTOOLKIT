package com.eplugger.main;

import com.eplugger.commons.beanutils.ConvertUtils;

public class Test2 {
	public static void main(String[] args) {
		Object convert = ConvertUtils.convert("true", Boolean.class);
		System.out.println(convert);
    }
}
