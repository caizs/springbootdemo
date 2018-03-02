package org.caizs.project.common.utils;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * 
 * @ClassName: RedisUtil
 * @Description: redisTemplate部分方法封装，可以持续扩展
 * 
 * @param <T>
 */
@Service
public class RedisUtil<T> {

    @Autowired
    @Qualifier("jedisTemplate")
    public RedisTemplate redisTemplate;


    /**
     * 缓存基本的对象，可过期
     * 
     * @param key
     * @param value
     * @param seconds
     *            null 缓存的时间，单位秒，传null则不过期
     * @return
     */
    public <T> ValueOperations<String, T> setObject(String key, T value, Long seconds) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        operation.set(key, value);
        if (seconds != null && seconds.compareTo(0L) > 0) {
            redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
        }
        return operation;
    }

    /**
     * 获得缓存的基本对象。
     * 
     * @param key
     *            缓存键值
     * @param operation
     * @return 缓存键值对应的数据
     */
    public <T> T getObject(String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 缓存List数据
     * 
     * @param key
     *            缓存的键值
     * @param dataList
     *            待缓存的List数据
     * @return 缓存的对象
     */
    public <T> ListOperations<String, T> setList(String key, List<T> dataList, Long seconds) {
        ListOperations listOperation = redisTemplate.opsForList();
        if (null != dataList) {
            int size = dataList.size();
            for (int i = 0; i < size; i++) {
                listOperation.rightPush(key, dataList.get(i));
            }
            if (seconds != null && seconds.compareTo(0L) > 0) {
                redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
            }
        }

        return listOperation;
    }

    /**
     * 获得缓存的list对象
     * 
     * @param key
     *            缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getList(String key) {
        List<T> dataList = new ArrayList<T>();
        ListOperations<String, T> listOperation = redisTemplate.opsForList();
        Long size = listOperation.size(key);

        for (int i = 0; i < size; i++) {
            dataList.add((T) listOperation.leftPop(key));
        }

        return dataList;
    }

    /**
     * 缓存Set
     * 
     * @param key
     *            缓存键值
     * @param dataSet
     *            缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setSet(String key, Set<T> dataSet, Long seconds) {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext()) {
            setOperation.add(it.next());
        }
        if (seconds != null && seconds.compareTo(0L) > 0) {
            redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     * 
     * @param key
     * @param operation
     * @return
     */
    public Set<T> getSet(String key) {
        Set<T> dataSet = new HashSet<T>();
        BoundSetOperations<String, T> operation = redisTemplate.boundSetOps(key);
        Long size = operation.size();
        for (int i = 0; i < size; i++) {
            dataSet.add(operation.pop());
        }
        return dataSet;
    }

    /**
     * 缓存Map
     * 
     * @param key
     * @param dataMap
     * @return
     */
    public <T> HashOperations<String, String, T> setMap(String key, Map<String, T> dataMap, Long seconds) {

        HashOperations hashOperations = redisTemplate.opsForHash();
        if (null != dataMap) {
            for (Map.Entry<String, T> entry : dataMap.entrySet()) {
                hashOperations.put(key, entry.getKey(), entry.getValue());
            }
            if (seconds != null && seconds.compareTo(0L) > 0) {
                redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
            }
        }

        return hashOperations;
    }

    /**
     * 获得缓存的Map
     * 
     * @param key
     * @param hashOperation
     * @return
     */
    public <T> Map<String, T> getMap(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 缓存Map
     * 
     * @param key
     * @param dataMap
     * @return
     */
    public <T> HashOperations<String, Integer, T> setIntegerMap(String key, Map<Integer, T> dataMap, Long seconds) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        if (null != dataMap) {
            for (Map.Entry<Integer, T> entry : dataMap.entrySet()) {
                hashOperations.put(key, entry.getKey(), entry.getValue());
            }
            if (seconds != null && seconds.compareTo(0L) > 0) {
                redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
            }
        }
        return hashOperations;
    }

    /**
     * 获得缓存的Map
     * 
     * @param key
     * @param hashOperation
     * @return
     */
    public <T> Map<Integer, T> getIntegerMap(String key) {
        Map<Integer, T> map = redisTemplate.opsForHash().entries(key);
        return map;
    }

    /**
     * 根据key获得缓存剩余有有效时间，默认单位秒
     * @param key
     * @param timeUnit 返回有效时间单位，
     * @return
     */
    public Long getExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit == null ? TimeUnit.SECONDS : timeUnit);
    }

}