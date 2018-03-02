package org.caizs.project.config.cache;

import com.github.benmanes.caffeine.cache.Cache;
import org.caizs.project.Application;
import org.caizs.project.configs.RedisBusConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.caffeine.CaffeineCache;

/**
 * 自定义的Caffeine缓存
 */
@Slf4j
public class LiveCaffeineCache extends CaffeineCache {


  public LiveCaffeineCache(String name,
      Cache<Object, Object> cache) {
    super(name, cache);
  }

  public LiveCaffeineCache(String name,
      Cache<Object, Object> cache, boolean allowNullValues) {
    super(name, cache, allowNullValues);
  }

  @Override
  public void evict(Object key) {
    super.evict(key);

    //发送redis消息
    RedisSender redisSender = Application.sContext.getBean(RedisSender.class);
    redisSender.sendLocalCacheMessageAsync(RedisBusConfig.LOCAL_CACHE, this.getName(), key);
  }
}