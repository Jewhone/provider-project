package com.zhp.jewhone.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisTemplate<String, Set<Object>> redisTemplate;

    @Test
    public void testList() {
        String key = "qq";
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("v");
        /*stringRedisTemplate.opsForValue().set("abc", "测试");
        stringRedisTemplate.opsForList().leftPushAll(key, list); // 向redis存入List
        stringRedisTemplate.opsForList().leftPush(key,"add");
        stringRedisTemplate.opsForList().range("qwe", 0, -1).forEach(System.out::println);
       stringRedisTemplate.opsForList().rightPush(key,"radd");*/
       stringRedisTemplate.opsForList().trim(key,0,2);
    }

    @Test
    public void testSet(){

    }
}
