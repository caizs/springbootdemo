package org.caizs.project.configs;

import org.caizs.project.config.cache.RedisReceiver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@Slf4j
public class RedisBusConfig {

  public static final String LOCAL_CACHE = "localCache";

  /**
   * 将Receiver注册为一个消息监听器，并指定消息接收的方法（handleMessage）
   *
   * 如果不指定消息接收的方法，消息监听器会默认的寻找Receiver中的handleMessage这个方法作为消息接收的方法
   */
  @Bean
  MessageListenerAdapter messageListenerAdapter(RedisReceiver receiver) {

    MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver,
        "handleMessage");
    return messageListenerAdapter;
  }

  /**
   * Redis消息监听器容器
   */
  @Bean
  RedisMessageListenerContainer container(RedisConnectionFactory factory,
      MessageListenerAdapter listenerAdapter) {

    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(factory);
    container.addMessageListener(listenerAdapter, new PatternTopic(LOCAL_CACHE));

    return container;

  }


}
