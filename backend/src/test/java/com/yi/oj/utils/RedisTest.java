package com.yi.oj.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
public class RedisTest {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Test
    public void test() throws Exception {
        redisTemplate.opsForValue().set("aaa", "111");
        System.out.println(redisTemplate.opsForValue().get("aaa"));
    }
}