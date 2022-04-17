package com.eplugger.commons.collections;

/**
 * 断言接口
 *
 * @param <E>
 */
interface Predicate<E> {
    /**
     * @param data 数据
     * @return <code>true</code>: 通过, <code>false</code>: 不通过
     */
    boolean apply(E data);
}