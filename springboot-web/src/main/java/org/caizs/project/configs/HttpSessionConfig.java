package org.caizs.project.configs;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 通过该注解使http session被spring session替换，并存在redis里
 */
@EnableRedisHttpSession()
public class HttpSessionConfig {

    // 按文档的说法只需配置server.session.timeout就行，但是不生效；
    // 这里通过这种方式使之生效；
    // 同理spring.session.redis.namespace
    @Value("${server.session.timeout}")
    private Integer maxInactiveIntervalInSeconds;

    @Value("${spring.session.redis.namespace}")
    private String redisKeyNamespace;

    @Autowired
    private RedisOperationsSessionRepository sessionRepository;

    @PostConstruct
    private void afterPropertiesSet() {
        sessionRepository.setDefaultMaxInactiveInterval(maxInactiveIntervalInSeconds);
        sessionRepository.setRedisKeyNamespace(redisKeyNamespace);
    }
}