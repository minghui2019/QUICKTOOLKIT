package com.eplugger.commons.collections;

/**
 * 属性合并处理接口
 */
interface AttributeMergeHandler<T> {
    /**
     * 属性合并
     *
     * @param a 对象A
     * @param b 对象B
     */
    void merge(T a, T b);
}