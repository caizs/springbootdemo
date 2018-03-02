package org.caizs.project.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.data.redis.core.RedisOperations;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author bjf
 * @date 2018/1/8
 */
public class SecondaryCacheManager implements CacheManager {

  private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);
  private final RedisOperations redisOperations;

  public SecondaryCacheManager(RedisOperations redisOperations) {
    this.redisOperations = redisOperations;
    initCacheMap();
  }

  private void initCacheMap() {
    for (CacheNameConfig.SecondaryCacheEnum cache : CacheNameConfig.SecondaryCacheEnum.values()) {
      cacheMap.put(cache.name, this.createCache(cache));
    }
  }

  private Cache createCache(CacheNameConfig.SecondaryCacheEnum cache) {

    //===1.创建本地缓存，比redis缓存多2秒
    CaffeineCache caffeineCache = new CaffeineCache(cache.name,
        Caffeine.newBuilder().recordStats()
                .expireAfterWrite(cache.timeout + 2, TimeUnit.SECONDS)
                .maximumSize(cache.capacity)
                .build());

    //===2.创建redis缓存
    LiveRedisCache redisCache = new LiveRedisCache(cache.name, null, redisOperations,
        cache.timeout);

    //===3.返回二级缓存对象
    return new SecondaryCache(cache.name, caffeineCache, redisCache, redisOperations,
        Boolean.FALSE);
  }


  @Override
  public Cache getCache(String name) {
    Cache cache = this.cacheMap.get(name);
    return cache;
  }

  @Override
  public Collection<String> getCacheNames() {
    return Collections.unmodifiableSet(this.cacheMap.keySet());
  }
}
