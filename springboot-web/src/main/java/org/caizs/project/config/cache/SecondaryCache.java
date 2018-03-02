package org.caizs.project.config.cache;

import org.caizs.project.Application;
import org.caizs.project.configs.RedisBusConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.core.RedisOperations;

import java.util.concurrent.Callable;

/**
 * 自定义缓存策略,二级缓存（Caffeine + Redis）
 *
 * @author bjf
 * @date 2018/1/5
 */
@Slf4j
public class SecondaryCache extends AbstractValueAdaptingCache {


  private final String cacheName;

  /**
   * redis缓存
   */
  private final LiveRedisCache redisCache;

  /**
   * Caffeine缓存
   */
  private final CaffeineCache caffeineCache;

  private final RedisOperations redisOperations;


  public SecondaryCache(String cacheName, CaffeineCache caffeineCache, LiveRedisCache redisCache,
      RedisOperations redisOperations, boolean allowNullValues) {

    super(allowNullValues);
    this.redisOperations = redisOperations;
    this.cacheName = cacheName;
    this.redisCache = redisCache;
    this.caffeineCache = caffeineCache;
  }


  @Override
  protected Object lookup(Object key) {
    ValueWrapper val = caffeineCache.get(key);
    if (val == null) {
      val = redisCache.get(key);
    }
    return val;
  }

  @Override
  public String getName() {
    return this.cacheName;
  }

  @Override
  public Object getNativeCache() {
    return this;
  }

  @Override
  public ValueWrapper get(Object key) {
    ValueWrapper wrapper = caffeineCache.get(key);
    if (wrapper == null) {
      wrapper = redisCache.get(key);
    }

    return wrapper;
  }

  @Override
  public <T> T get(Object key, Class<T> type) {
    T value = caffeineCache.get(key, type);
    if (value == null) {
      value = redisCache.get(key, type);
    }

    return value;
  }

  @Override
  public <T> T get(Object key, Callable<T> valueLoader) {

    //===1.查询一级缓存caffeine
    T value = caffeineCache.get(key, valueLoader);
    if (value == null) {
      //===2.查询二级缓存redis
      value = redisCache.get(key, valueLoader);
    }

    return value;
  }

  @Override
  public void put(Object key, Object value) {
    caffeineCache.put(key, value);
    redisCache.put(key, value);
  }

  @Override
  public ValueWrapper putIfAbsent(Object key, Object value) {
    caffeineCache.putIfAbsent(key, value);
    return redisCache.putIfAbsent(key, value);
  }

  @Override
  public void evict(Object key) {
    redisCache.evict(key);
    caffeineCache.evict(key);

    //发送redis消息
    RedisSender redisSender = Application.sContext.getBean(RedisSender.class);
    redisSender.sendLocalCacheMessageAsync(RedisBusConfig.LOCAL_CACHE, cacheName,key);
  }

  @Override
  public void clear() {
    redisCache.clear();
    caffeineCache.clear();
  }

  public CaffeineCache getCaffeineCache() {
    return caffeineCache;
  }

  public LiveRedisCache getRedisCache() {
    return redisCache;
  }
}
