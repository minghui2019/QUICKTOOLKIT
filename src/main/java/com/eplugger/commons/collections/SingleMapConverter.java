package com.eplugger.commons.collections;

import java.util.Map;

/**
 * 对象转Map接口
 *
 * @param <I> 输入对象
 * @param <V> Map.Value
 * @param <K> Map.Key
 */
class SingleMapConverter<I extends Map<K, V>, V, K> extends MapConverter<I, V, K> {
    @Override
    public K toKey(I in) {
        return in.keySet().iterator().next();
    }

    @Override
    public V toValue(I in) {
        return in.values().iterator().next();
    }
}