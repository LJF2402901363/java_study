package com.moyisuiying.jwt.mapper;

import com.moyisuiying.jwt.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Classname:jwt
 *
 * @description: 用户的mapper
 * @author: 陌意随影
 * @Date: 2021-01-31 23:18
 */
@Mapper
public interface UserMapper {
    /**
     * @Description :通过用户名和密码获取登录的User
     * @Date 23:25 2021/1/31 0031
     * @Param * @param name 用户名
     * @param password ：用户密码
     * @return com.moyisuiying.jwt.entity.User
     **/
    public User login(@Param("name") String name, @Param("password") String password);
}
