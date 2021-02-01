package com.moyisuiying.jwt.service;

import com.moyisuiying.jwt.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * Classname:UserService
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-01-31 23:23
 * @Version: 1.0
 **/
public interface UserService {
    /**
     * @Description :通过用户名和密码获取登录的User
     * @Date 23:25 2021/1/31 0031
     * @Param * @param name 用户名
     * @param password ：用户密码
     * @return java.util.Map<java.lang.String,java.lang.String>
     **/
    public Map<String,Object> login(@Param("name") String name, @Param("password") String password);
}
