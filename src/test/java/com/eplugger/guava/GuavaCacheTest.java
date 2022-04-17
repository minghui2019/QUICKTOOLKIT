package com.eplugger.guava;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * <pre>
 * ImmutableMap<K, V> getAllPresent(Iterable<?> keys) 一次获得多个键的缓存值
 * putAll方法向缓存中添加一个或者多个缓存项
 * invalidateAll方法从缓存中移除缓存项
 * ConcurrentMap<K, V>快照
 * refresh(Key) 刷新缓存，即重新取缓存数据，更新缓存
 * </pre>
 */
public class GuavaCacheTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 缓存接口这里是LoadingCache，LoadingCache在缓存项不存在时可以自动加载缓存
        LoadingCache<Integer, Student> studentCache
        // CacheBuilder的构造函数是私有的，只能通过其静态方法newBuilder()来获得CacheBuilder的实例
                = CacheBuilder.newBuilder()
                        // 设置并发级别为8，并发级别是指可以同时写缓存的线程数
                        .concurrencyLevel(8)
                        // 设置写缓存后8秒钟过期
                        .expireAfterWrite(8, TimeUnit.SECONDS)
                        // 设置缓存容器的初始容量为10
                        .initialCapacity(10)
                        // 设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
                        .maximumSize(100)
                        // 设置要统计缓存的命中率
                        .recordStats()
                        // 设置缓存的移除通知
                        .removalListener(new RemovalListener<Object, Object>() {
                            @Override
                            public void onRemoval(RemovalNotification<Object, Object> notification) {
                                System.out.println(notification.getKey() + " was removed, cause is " + notification.getCause());
                            }
                        })
                        // build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                        .build(new CacheLoader<Integer, Student>() {
                            @Override
                            public Student load(Integer key) throws Exception {
                                // 这里可以设置数据库查询，放入缓存中
                                System.out.println("load student " + key);
                                Student student = new Student();
                                student.setId(key);
                                student.setName("name " + key);
                                return student;
                            }
                        });

        for (int i = 0; i < 20; i++) {
            // 从缓存中得到数据，由于我们没有设置过缓存，所以需要通过CacheLoader加载缓存数据
            // 缓存没有数据会调用load(Integer key)，并把查询到的数据放入缓存中
            Student student = studentCache.get(1);
            System.out.println(student);
            // java.util.concurrent.TimeUnit 休眠1秒
            TimeUnit.SECONDS.sleep(1);
        }

        System.out.println("cache stats:");
        // 最后打印缓存的命中率等 情况
        System.out.println(studentCache.stats().toString());
    }
}