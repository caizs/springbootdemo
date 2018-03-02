package org.caizs.project.config.cache;

import com.alibaba.fastjson.JSONObject;
import org.caizs.project.configs.RedisBusConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * redis订阅消息
 * 使caffeine本地一级和二级缓存失效
 */
@Service
@Slf4j
public class RedisReceiver {

  @Resource
  private SimpleCacheManager caffeineCacheManager;
  @Resource
  private SecondaryCacheManager secondaryCacheManager;

  public void handleMessage(String message, String topic) {
    log.info("receive redis massage,topic:{},message:{}", topic, message);
    if (RedisBusConfig.LOCAL_CACHE.equals(topic)) {
      // 取出消息中的key
      Object parse = JSONObject.parse(message);
      JSONObject json = JSONObject.parseObject(parse.toString());
      String cacheName = json.getString("cacheName");
      String cacheKey = json.getString("cacheKey");

      // 删除二级中的caffeine缓存
      Cache secondaryCacheName = secondaryCacheManager.getCache(cacheName);
      if (secondaryCacheName != null) {
        SecondaryCache secondaryCache = (SecondaryCache) secondaryCacheName;
        secondaryCache.getCaffeineCache().getNativeCache().invalidate(cacheKey);
      }

      // 删除caffeine缓存
      Cache caffeineCacheName = caffeineCacheManager.getCache(cacheName);
      if (caffeineCacheName != null) {
        CaffeineCache caffeineCache = (CaffeineCache) caffeineCacheName;
        caffeineCache.getNativeCache().invalidate(cacheKey);
      }
    }
  }
}