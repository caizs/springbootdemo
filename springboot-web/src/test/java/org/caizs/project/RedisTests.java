package org.caizs.project;

import java.util.Date;

import org.caizs.project.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import org.caizs.project.common.utils.RedisUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RedisTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisUtil<User> redis;

    @Autowired
    @Qualifier("jedisTemplate") // 注意一定要声明@Qualifier("jedisTemplate")
    public RedisTemplate<String, User> redisTemplate;

    @Test
    public void stringRedisTemplate() throws Exception {
        stringRedisTemplate.opsForValue().set("key1", "value1");
        Assert.assertEquals("value1", stringRedisTemplate.opsForValue().get("key1"));
        stringRedisTemplate.delete("key1");
        Assert.assertEquals(null, stringRedisTemplate.opsForValue().get("key1"));
    }

    @Test
    public void RedisUtil() throws Exception {
        User user = new User(1, "name", 17, "address", 1, new Date());
        redis.setObject("user", user, null);
        User a = redis.getObject("user");
        Assert.assertTrue("name".equals(a.getName()));
    }
    
    @Test
    public void redisTemplate() throws Exception {
        User user = new User(1, "name", 17, "address", 1, new Date());
        BoundValueOperations<String, User> ops = redisTemplate.boundValueOps("key2");
        ops.set(user);
        User b = redis.getObject("user");
        Assert.assertTrue("name".equals(b.getName()));
//        redisTemplate.delete("key2");
//        Assert.assertTrue(redisTemplate.opsForValue().get("key2") == null);
    }

}
