package org.caizs.project.config.cache;

import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisOperations;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 自定义的redis缓存管理器
 */
public class LiveRedisCacheManager extends RedisCacheManager {

  private Collection<String> cacheNames = new ArrayList<>();

  public LiveRedisCacheManager(RedisOperations redisOperations) {
    super(redisOperations);
  }

  public LiveRedisCacheManager(RedisOperations redisOperations,
      Collection<String> cacheNames) {
    super(redisOperations, cacheNames);
    super.setCacheNames(cacheNames);
    this.cacheNames = cacheNames;
  }

  public LiveRedisCacheManager(RedisOperations redisOperations,
      Collection<String> cacheNames, boolean cacheNullValues) {
    super(redisOperations, cacheNames, cacheNullValues);
    super.setCacheNames(cacheNames);
    this.cacheNames = cacheNames;
  }

  @Override
  protected Cache getMissingCache(String cacheName) {
    if (!cacheNames.contains(cacheName)) {
      return null;
    }

    return cacheNames.isEmpty() ? createCache(cacheName) : null;
  }

  @Override
  protected RedisCache createCache(String cacheName) {
    long expiration = computeExpiration(cacheName);
    return new LiveRedisCache(cacheName,
        (this.isUsePrefix() ? this.getCachePrefix().prefix(cacheName) : null),
        this.getRedisOperations(),
        expiration);
  }
}