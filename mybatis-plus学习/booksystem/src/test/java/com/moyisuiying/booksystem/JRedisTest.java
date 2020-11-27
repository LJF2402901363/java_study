package com.moyisuiying.booksystem;

import com.moyisuiying.booksystem.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Classname:JRedisTest
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2020-11-27 20:33
 * @Version: 1.0
 **/
@SpringBootTest
public class JRedisTest {
    @Autowired
    RedisTemplate<String,String> redisTemplate;
    @Autowired
    RedisUtil redisUtil;
    @Test
  public   void testRedis(){
    redisTemplate.opsForValue().set("name","root");
        String name = redisTemplate.opsForValue().get("name");
        System.out.println(name);
    }
    @Test
    public void  testRedisUtil(){
        String name = redisUtil.get("name");
        redisUtil.set("password","1232442");
        System.out.println(name);
        System.out.println("password");
    }
}
