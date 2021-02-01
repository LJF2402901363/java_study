package com.moyisuiying.jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Classname:LoginUser
 *
 * @description: 登录用户
 * @author: 陌意随影
 * @Date: 2021-02-01 11:14
 * @Version: 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**用户信息*/
    private User user;
}
