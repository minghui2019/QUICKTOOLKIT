package com.eplugger.guava;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;

public class Main {
    public static void main(String[] args) {
        // 创建一个 List
        List<String> myList = new ArrayList<>();
        myList.add("Apple");
        myList.add("Banana");
        myList.add("Orange");
        myList.add("Grape");
        myList.add("Cherry");
        myList.add("Apple");
        myList.add("Banana");
        myList.add("Orange");
        myList.add("Grape");
        myList.add("Cherry");
        myList.add("Apple");
        myList.add("Banana");
        myList.add("Orange");
        myList.add("Grape");
        myList.add("Cherry");

        // 获取 List 的 Spliterator
        Spliterator<String> spliterator = myList.spliterator();

//        // 使用 tryAdvance() 遍历元素
//        System.out.println("Traversing elements using tryAdvance():");
//        while (spliterator.tryAdvance(System.out::println)) {
//            // 继续遍历
//        }

        // 拆分前检查估计大小是否大于阈值
        if (spliterator.estimateSize() > 10) {
            // 创建一个新的 Spliterator，拆分一半元素
            Spliterator<String> secondHalf = spliterator.trySplit();

            // 使用 forEachRemaining() 遍历剩余元素
            System.out.println("Traversing remaining elements using forEachRemaining():");
            secondHalf.forEachRemaining(System.out::println);
        } else {
            System.out.println("List size is too small to split.");
        }
    }
}
