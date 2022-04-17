package com.eplugger.guava;

import java.nio.charset.Charset;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

/**
 * <pre>
 * 布隆过滤器
 *
 * 布隆过滤器可以插入元素，但不可以删除已有元素。其中的元素越多，
 * false positive rate(误报率)越大，但是false negative(漏报)是不可能的。
 * 为了add一个元素，用k个hash function将它hash得到bloom filter中k个bit位，将这k个bit位置1。
 *
 * 为了query一个元素，即判断它是否在集合中，用k个hash function将它hash得到k个bit位。若这k bits全为1，
 * 则此元素在集合中；若其中任一位不为1，则此元素不在集合中（因为如果在，则在add时已经把对应的k个bits位置为1）。
 *
 * 不允许remove元素，因为那样的话会把相应的k个bits位置为0，而其中很有可能有其他元素对应的位。因此
 * remove会引入false negative，这是绝对不被允许的。
 * </pre>
 */
public class BloomTest {
    // 预计元素个数
    private long size = 1024 * 1024;

    private BloomFilter<String> bloom = BloomFilter.create(new Funnel<String>() {
        private static final long serialVersionUID = 3814804907566346698L;

        @Override
        public void funnel(String from, PrimitiveSink into) {
            // 自定义过滤条件 此处不做任何过滤
            into.putString((CharSequence) from, Charset.forName("UTF-8"));
        }
    }, size);

    public void fun() {
        // 往过滤器中添加元素
        bloom.put("布隆过滤器");
        // 查询
        boolean mightContain = bloom.mightContain("布隆过滤器");
        System.out.println(mightContain);
    }

    public static void main(String[] args) {
        BloomTest blBoolmTest = new BloomTest();
        blBoolmTest.fun();
    }
}