package com.eplugger.commons.collections;

/**
 * 数据同步接口
 *
 * @param <T> 目标类型
 * @param <S> 源类型
 */
interface Synchronizer<T, S> {
    void sync(T dist, S src);
}