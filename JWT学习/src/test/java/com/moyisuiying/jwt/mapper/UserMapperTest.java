package com.moyisuiying.jwt.mapper;

import com.moyisuiying.jwt.entity.User;
import com.moyisuiying.jwt.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Classname:UserMapperTest
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-02-01 12:55
 * @Version: 1.0
 **/
@SpringBootTest
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;
    @Test
    public void testLogin(){
        User login = userMapper.login("a", "a");
        System.out.println(login);
    }
}
