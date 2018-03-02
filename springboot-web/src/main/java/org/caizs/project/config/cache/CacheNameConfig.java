package org.caizs.project.config.cache;

import lombok.Data;
import org.essentials4j.New;

import java.util.List;

/**
 * 初衷是所有配置使用spring cache 框架（@CacheAble）的缓存名称要明确定义（如：过期时间），然后再使用
 */
public class CacheNameConfig {

    /**
     * ===========================redis start ===========================
     */
    /**
     * 明确声明使用cache name 的过期时间
     */
    @Data
    public static class RedisCacheEnum {

        public static List<RedisCacheEnum> values() {
            return New.list(new RedisCacheEnum("USER", 10), new RedisCacheEnum("MENU", 3600));
        }

        /**
         * 过期时间秒
         */
        public long timeout = 10L;
        public String name;

        public RedisCacheEnum(String name, long timeout) {
            this.timeout = timeout;
            this.name = name;
        }
    }

    /**
     * ===========================redis end ===========================
     */

    /**
     * ===========================local start ===========================
     */
    /**
     * 明确声明使用cache name 的过期时间和容量
     */
    @Data
    public static class LocalCacheEnum {

        public static List<LocalCacheEnum> values() {
            return New.list(new LocalCacheEnum("FAST_LOCAL", 5, 1000), new LocalCacheEnum("LOCAL", 600, 30000));
        }

        public String name;
        public long timeout = 10;
        public int capacity = 1000;

        LocalCacheEnum(String name, int timeout, int capacity) {
            this.name = name;
            this.timeout = timeout;
            this.capacity = capacity;
        }

    }
    /**
     * ===========================local end ===========================
     */

    /**
     * ===========================二级缓存配置（caffeine + redis） start ===========================
     */

    /**
     * 明确声明使用cache name 的过期时间和容量
     */
    public static class SecondaryCacheEnum {
        public static List<SecondaryCacheEnum> values() {
            return New.list(new SecondaryCacheEnum("VEDEO", 5, 1000), new SecondaryCacheEnum("COMPUTOR", 30, 30000));
        }

        public String name;
        public long timeout = 10;
        public int capacity = 1000;

        SecondaryCacheEnum(String name, int timeout, int capacity) {
            this.name = name;
            this.timeout = timeout;
            this.capacity = capacity;
        }
    }
    /**
     * ===========================二级缓存配置（caffeine + redis） end ===========================
     */
}