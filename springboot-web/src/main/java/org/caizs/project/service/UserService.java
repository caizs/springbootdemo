package org.caizs.project.service;

import org.caizs.project.domain.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service("userService")
@CacheConfig(cacheNames = { "user" }) // 务必要声明缓存名称，避免redis里key冲突；只对内部有声明缓存注解的方法生效；可被方法级别声明覆盖
public class UserService {
    Set<User> users = new HashSet<User>();

    @CachePut(value = "user", key = "#user.id")
    public User save(User user) {
        users.add(user);
        return user;
    }

    @CachePut(key = "#user.id")
    public User update(User user) {
        users.remove(user);
        users.add(user);
        return user;
    }

    @CacheEvict(key = "#user.id")
    public User delete(User user) {
        users.remove(user);
        return user;
    }

    @CacheEvict(allEntries = true)
    public void deleteAll() {
        users.clear();
    }

    @Cacheable(value = "user", key = "#id")
    public User findById(final Integer id) {
        System.out.println("cache miss, invoke find by id, id:" + id);
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }
}
