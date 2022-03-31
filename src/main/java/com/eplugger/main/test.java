package com.eplugger.main;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class test {
//	public static void main(String[] args) {
////		long time1 = System.currentTimeMillis();
////		long time3 = 30l * 24 * 60 * 60 * 1000;
////		long time2 = time1 + time3;
////		java.sql.Date createDate = new java.sql.Date(time1);
////		long time4 = createDate.getTime();
////		java.sql.Date nextDate = new java.sql.Date(time2);
////        System.out.println(time1 + "--" + time2 + "--" + time3 + "--" + time4);
////        System.out.println(createDate + "--" + nextDate);
////		List<E> list = new ArrayList<E>();
////		int target = 2;
////		boolean flag = true;
////		switch (target) {
////		case 1:
////			System.out.println(1);
////			flag = false;
////		case 2:
////			if (flag) {
////				System.out.println(2);
////			}
////		case 3:
////			System.out.println(3);
////		case 4:
////			System.out.println(4);
////		case 5:
////			System.out.println(5);
////			break;
////		default:
////			break;
////		}
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(2020, 5, 1, 0,0,0);
//		System.out.println(calendar.getTime());
//		System.out.println(calendar.getTimeInMillis());
//		System.out.println(new Date().after(new Date(1634486400000l)));
//		System.out.println(!new Date(1634486400001l).after(new Date(1634486400000l)));
//	}
	public static void main(String[] args) {
        Set<String> result = new HashSet<String>();
        Set<String> set1 = new HashSet<String>() {
            {
                add("王者荣耀");
                add("英雄联盟");
                add("穿越火线");
                add("地下城与勇士");
            }   
        };

        Set<String> set2 = new HashSet<String>() {
            {
                add("王者荣耀");
                add("地下城与勇士");
                add("魔兽世界");
            }
        };

        result.clear();
        result.addAll(set1);
        result.retainAll(set2);
        System.out.println("交集：" + result);

        result.clear();
        result.addAll(set1);
        result.removeAll(set2);
        System.out.println("差集：" + result);

        result.clear();
        result.addAll(set1);
        result.addAll(set2);
        System.out.println("并集：" + result);
    }

	/**
	 * 正则表达式替换字符串
	 * @param s
	 * @param map
	 * @return
	 */
	static Pattern mPattern = Pattern.compile("\\{(.*?)\\}");
	public static String repeat(String s, Map<String, String> map) {
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			s = s.replace("{" + key + "}", map.get(key));
		}
        return s;
    }
	
	public static String doubleToString(double num, int key) {
	  String result = "0";
	  switch (key) {
	  case 6:
	    DecimalFormat df = new DecimalFormat("#.######");
	    result = df.format(num);
	    break;
	  case 5:
	    DecimalFormat df2 = new DecimalFormat("#.#####");
	    result = df2.format(num);
	    break;
	  case 4:
	    DecimalFormat df3 = new DecimalFormat("#.####");
	    result = df3.format(num);
	    break;
	  case 2:
	    DecimalFormat df4 = new DecimalFormat("#,##0.00");
	    result = df4.format(num);
	    break;
	  default:
	    DecimalFormat df5 = new DecimalFormat("#.######");
	    result = df5.format(num);
	    break;
	  }
	  return result;
	}
	
	public void randomDouble() {
		String str1 = "";
		int ii = 3, jj = 8;
		for (int i = 0; i < ii; i++) {
			String str2 = "[";
			for (int j = 0; j < jj; j++) {
				int ma = (int) (Math.random() * 300);
				if (ma < 100) {
					ma += 100;
				}
				str2 += String.valueOf(ma);
				if (j < jj - 1) {
					str2 += ",";
				}
			}
			str2 += "]";
			str1 += str2;
			if (i < ii - 1) {
				str1 += ",";
			}
		}
		System.out.println(str1);
	}
}
