package com.moyisuiying.jwt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.moyisuiying.jwt.uitl.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * Classname:JwtUtilTest
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-02-01 14:47
 * @Version: 1.0
 **/
@SpringBootTest
public class JwtUtilTest {
    @Autowired
    JwtUtil jwtUtil;
    String token = "";
    @Test
    public void test() {
        Map<String,String> map = new HashMap<>();
        map.put("name","a");
        map.put("password","b");
        token = jwtUtil.createToken(map);
        DecodedJWT decode = JWT.decode(token);

        System.out.println(token);
        String name = decode.getClaim("name").asString();
        System.out.println(name);
    }
    @Test
    public void testVerity(){
        jwtUtil.verifyToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6ImIiLCJuYW1lIjoiYSIsImV4cCI6MTYxMjE2NDE4NSwiaWF0IjoxNjEyMTYzNjg1fQ.Zi4uZgJ-_RvsluAFUob0lI1KEyYowLxfzlfwlN1dRHA");
    }
    @Test
    public void testGetClaim(){
        Map<String,String> map = new HashMap<>();
        map.put("name","a");
        map.put("password","b");
        token = jwtUtil.createToken(map);
        System.out.println(token);
        String name = jwtUtil.getTokenClaimByName("name",token);
        System.out.println(name);
    }
}
