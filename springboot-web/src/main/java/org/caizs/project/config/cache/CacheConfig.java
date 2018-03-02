package org.caizs.project.config.cache;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching(proxyTargetClass = true)
public class CacheConfig extends CachingConfigurerSupport {

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(":").append(method.getName());
                for (Object obj : params) {
                    sb.append(":").append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

    private ObjectMapper buildObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        // 只序列化属性，忽略setter,getter,is_getter和creater
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        // 让所有的非final类型对象持久化时都存储类型信息,准确的反序列多态类型的数据
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        // 设置忽略不存在的字段
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许出现特殊字符和转义符
        om.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        return om;
    }

    @Bean("jedisTemplate")
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        // 定义value的序列化方式
        Jackson2JsonRedisSerializer jsonSerializer = new Jackson2JsonRedisSerializer(Object.class);
        jsonSerializer.setObjectMapper(buildObjectMapper());

        RedisTemplate template = new RedisTemplate();
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        template.setConnectionFactory(factory);

       // template.setKeySerializer(template.getStringSerializer());
        template.setKeySerializer(serializer);
        template.setValueSerializer(jsonSerializer);

        //template.setHashKeySerializer(template.getStringSerializer());
        template.setHashKeySerializer(serializer);
        template.setHashValueSerializer(jsonSerializer);
        template.afterPropertiesSet();

        return template;
    }


    /**
     * 本地缓存 Caffeine Cache
     */
    @Bean
    public CacheManager caffeineCacheManager() {
        ArrayList<CaffeineCache> caches = new ArrayList<>();
        for (CacheNameConfig.LocalCacheEnum cache : CacheNameConfig.LocalCacheEnum.values()) {
            LiveCaffeineCache liveCaffeineCache = new LiveCaffeineCache(cache.name,
                    Caffeine.newBuilder().recordStats()
                            .expireAfterWrite(cache.getTimeout(), TimeUnit.SECONDS)
                            .maximumSize(cache.getCapacity())
                            .build());
            caches.add(liveCaffeineCache);
        }
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caches);
        cacheManager.afterPropertiesSet();
        return cacheManager;
    }

    /**
     * redis cache
     */
    @Bean
    public CacheManager redisCacheManager(RedisTemplate redisTemplate) {

        //取自定义的缓存值
        List<String> cacheNames = new ArrayList<>();
        Map<String, Long> expires = new HashMap<>();
        for (CacheNameConfig.RedisCacheEnum cache : CacheNameConfig.RedisCacheEnum.values()) {
            cacheNames.add(cache.getName());
            expires.put(cache.getName(), cache.getTimeout());
        }

        //使用自定义的RedisCacheManager
        LiveRedisCacheManager rcm = new LiveRedisCacheManager(redisTemplate, cacheNames);
        //设置默认24小时过期。自定义的LiveRedisCacheManager已经屏蔽
        //rcm.setDefaultExpiration(86400L);
        //设置自定义缓存值的过期时间(对应@Cacheable里面的value值)
        rcm.setExpires(expires);
        rcm.afterPropertiesSet();

        return rcm;
    }

    /**
     * 自定义二级缓存 caffeine + redis
     */
    @Bean
    public CacheManager secondaryCacheManager(RedisTemplate redisTemplate) {
        return new SecondaryCacheManager(redisTemplate);
    }

    @Bean
    @Primary
    public CompositeCacheManager compositeCacheManager(CacheManager caffeineCacheManager,
            CacheManager redisCacheManager, CacheManager secondaryCacheManager) {
        CompositeCacheManager cacheManager = new CompositeCacheManager();
        cacheManager
                .setCacheManagers(
                        Arrays.asList(caffeineCacheManager, redisCacheManager, secondaryCacheManager));
        cacheManager.setFallbackToNoOpCache(Boolean.TRUE);
        return cacheManager;
    }
}
