package org.caizs.project.config.cache;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheElement;
import org.springframework.data.redis.cache.RedisCacheKey;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.Assert;

/**
 * 自定义的redis缓存
 *
 * 1.获取缓存在大并发下的一个bug
 *
 * 2.redis不可用情况下返回空,继续走业务逻辑（DB）取数据
 */
@Slf4j
public class LiveRedisCache extends RedisCache {

  private static final Logger logEmail = LoggerFactory.getLogger("email");

  private final RedisOperations redisOperations;


  public LiveRedisCache(String name, byte[] keyPrefix,
      RedisOperations<? extends Object, ? extends Object> redisOperations, long expiration) {
    super(name, keyPrefix, redisOperations, expiration);
    this.redisOperations = redisOperations;
  }

  public LiveRedisCache(String name, byte[] keyPrefix,
      RedisOperations<? extends Object, ? extends Object> redisOperations, long expiration,
      boolean allowNullValues) {
    super(name, keyPrefix, redisOperations, expiration, allowNullValues);
    this.redisOperations = redisOperations;
  }

  /**
   * 重写父类的get函数。 父类的get方法，是先使用exists判断key是否存在，不存在返回null，存在再到redis缓存中去取值。这样会导致并发问题，
   * 假如有一个请求调用了exists函数判断key存在，但是在下一时刻这个缓存过期了，或者被删掉了。 这时候再去缓存中获取值的时候返回的就是null了。
   * 可以先获取缓存的值，再去判断key是否存在。
   *
   * redis不可用的时候返回空，直接查找数据库
   */
  @Override
  public RedisCacheElement get(final RedisCacheKey cacheKey) {

    Assert.notNull(cacheKey, "CacheKey must not be null!");

    try {
      RedisCacheElement redisCacheElement = new RedisCacheElement(cacheKey,
          fromStoreValue(lookup(cacheKey)));
      Boolean exists = (Boolean) redisOperations.execute(new RedisCallback<Boolean>() {

        @Override
        public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
          return connection.exists(cacheKey.getKeyBytes());
        }
      });

      if (!exists.booleanValue()) {
        return null;
      }

      return redisCacheElement;
    } catch (Exception e) {
      logEmail.error("redis get 连接异常，发起数据库查询", e);
      log.error("redis get 连接异常，发起数据库查询", e);
      return null;
    }
  }

  @Override
  public void put(RedisCacheElement element) {
    try {
      super.put(element);
    } catch (Exception e) {
      log.error("redis put 连接异常", e);
    }
  }

  @Override
  public void evict(Object key) {
    try {
      super.evict(key);
    } catch (Exception e) {
      log.error("redis evict 连接异常", e);
    }
  }

  @Override
  public void clear() {
    try {
      super.clear();
    } catch (Exception e) {
      log.error("redis clear 连接异常", e);
    }
  }
}