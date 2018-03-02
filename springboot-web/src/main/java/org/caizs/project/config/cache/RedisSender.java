package org.caizs.project.config.cache;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * redis 消息发送
 */
@Service
@Slf4j
public class RedisSender {

  @Autowired
  protected RedisTemplate redisTemplate;

  @Async
  public void sendMessageAsync(String topic, String message) {
    try {
      log.info("send redis massage,topic:{},message:{}", topic, message);
      redisTemplate.convertAndSend(topic, message);
    } catch (Exception e) {
      log.error("redis send message fail!", e);
    }
  }
  @Async
  public void sendLocalCacheMessageAsync(String topic, String cacheName,Object cacheKey) {
    try {
      log.info("send redis massage,topic:{},cacheName:{},cacheKey:{}", topic, cacheName,cacheKey);
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("cacheName", cacheName);
      jsonObject.put("cacheKey", cacheKey);
      String message = jsonObject.toJSONString();

      redisTemplate.convertAndSend(topic, message);
    } catch (Exception e) {
      log.error("redis send message fail!", e);
    }
  }
}