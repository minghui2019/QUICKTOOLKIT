package com.eplugger.commons.collections;

/**
 * 对象转Map接口
 *
 * @param <I> 输入对象
 * @param <V> Map.Value
 * @param <K> Map.Key
 */
abstract class MapConverter<I, V, K> {
    protected K toKey(I data) {
        throw new UnsupportedOperationException();
    }

    protected V toValue(I in) {
        throw new UnsupportedOperationException();
    }

    protected V toValue(I bean, V old) {
        return toValue(bean);
    }
}