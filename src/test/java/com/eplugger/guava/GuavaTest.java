package com.eplugger.guava;

import java.util.Map;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

/**
 * Guava API使用教程
 */
public class GuavaTest {

    /**
     * <pre>
     * Immutable特点
     * 1.在多线程操作下，是线程安全的。
     * 2.所有不可变集合会比可变集合更有效的利用资源。
     * 3.中途不可改变
     * 输出： iList: [a, b, c] iSet: [e1, e2] iMap: {k1=v1, k2=v2}
     * </pre>
     */
    @Test
    public void testGuavaCollection() {
        ImmutableList<String> iList = ImmutableList.of("a", "b", "c");
        ImmutableSet<String> iSet = ImmutableSet.of("e1", "e2");
        ImmutableMap<String, String> iMap = ImmutableMap.of("k1", "v1", "k2", "v2");
        System.out.println("iList: " + iList.toString());
        System.out.println("iSet: " + iSet.toString());
        System.out.println("iMap: " + iMap.toString());
    }

    /**
     * 输出：[1, 2]
     */
    @Test
    public void testGuavaMap() {
        Multimap<String, Integer> mapM = ArrayListMultimap.create();
        mapM.put("test", 1);
        mapM.put("test", 2);
        System.out.println(mapM.get("test"));

    }

    /**
     * <pre>
     * Guava 字符串连接器Joiner
     * 输出：嗨，jim|jack|kevin
     * </pre>
     */
    @Test
    public void testGuavaJoiner() {
        StringBuilder stringBuilder = new StringBuilder("嗨，");
        // 字符串连接器，以|为分隔符，同时去掉null元素
        Joiner joiner1 = Joiner.on("|").skipNulls();
        // 构成一个字符串jim|jack|kevin并添加到stringBuilder
        stringBuilder = joiner1.appendTo(stringBuilder, "jim", "jack", null, "kevin");
        System.out.println(stringBuilder);
    }

    /**
     * <pre>
     * Guava MapToString
     * 输出：Cookies:12332#Content-Length:30000#Date:2018.07.04#Mime:text/html
     * </pre>
     */
    @Test
    public void testMapToString() {
        Map<String, String> testMap = Maps.newLinkedHashMap();
        testMap.put("Cookies", "12332");
        testMap.put("Content-Length", "30000");
        testMap.put("Date", "2018.07.04");
        testMap.put("Mime", "text/html");
        // 用:分割键值对，并用#分割每个元素，返回字符串
        String returnedString = Joiner.on("#").withKeyValueSeparator(":").join(testMap);
        System.out.println(returnedString);
    }

    /**
     * <pre>
     * Guava MapToString 
     * 输出：
     * Cookies -> 12332
     * Content-Length -> 30000
     * Date -> 2018.07.04
     * Mime -> text/html
     * </pre>
     */
    @Test
    public void testStringToMap() {
        // 接上一个，内部类的引用，得到分割器，将字符串解析为map
        Splitter.MapSplitter ms = Splitter.on("#").withKeyValueSeparator(':');
        Map<String, String> ret = ms.split("Cookies:12332#Content-Length:30000#Date:2018.07.04#Mime:text/html");
        for (String it2 : ret.keySet()) {
            System.out.println(it2 + " -> " + ret.get(it2));
        }
    }

    /**
     * Guava 字符串工具类Strings 输出： true true false helloTTT
     */
    @Test
    public void testStrings() {
        System.out.println(Strings.isNullOrEmpty("")); // true
        System.out.println(Strings.isNullOrEmpty(null)); // true
        System.out.println(Strings.isNullOrEmpty("hello")); // false
        // 将null转化为""
        System.out.println(Strings.nullToEmpty(null)); // ""

        // 从尾部不断补充T只到总共8个字符，如果源字符串已经达到或操作，则原样返回。类似的有padStart
        System.out.println(Strings.padEnd("hello", 8, 'T')); // helloTTT
    }

    /**
     * Guava 字符匹配器CharMatcher
     */
    @Test
    public void testCharMatcher() {
        // 空白回车换行对应换成一个#，一对一换
        String stringWithLinebreaks = "hello world\r\r\ryou are here\n\ntake it\t\t\teasy";
        String s6 = CharMatcher.breakingWhitespace().replaceFrom(stringWithLinebreaks, '#');
        // hello#world###you#are#here##take#it###easy
        System.out.println(s6);

        // 将所有连在一起的空白回车换行字符换成一个#，倒塌
        String tabString = "  hello   \n\t\tworld   you\r\nare             here  ";
        String tabRet = CharMatcher.whitespace().collapseFrom(tabString, '#');
        // #hello#world#you#are#here#
        System.out.println(tabRet);

        // 在前面的基础上去掉字符串的前后空白，并将空白换成一个#
        String trimRet = CharMatcher.whitespace().trimAndCollapseFrom(tabString, '#');
        // hello#world#you#are#here
        System.out.println(trimRet);

        String letterAndNumber = "1234abcdABCD56789";
        // 保留数字
        String number = CharMatcher.javaDigit().retainFrom(letterAndNumber);
        // 123456789
        System.out.println(number);
    }
}