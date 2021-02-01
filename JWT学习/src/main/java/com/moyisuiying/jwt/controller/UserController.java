package com.moyisuiying.jwt.controller;

import com.moyisuiying.jwt.entity.User;
import com.moyisuiying.jwt.service.UserService;
import com.moyisuiying.jwt.uitl.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Classname:UserController
 *
 * @description: 用户User的控制器
 * @author: 陌意随影
 * @Date: 2021-01-31 23:29
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
     /**
      * @Description :用户登录
      * @Date 16:39 2021/2/1 0001
      * @Param * @param name  用户名
      * @param password ：用户密码
      * @return java.util.Map<java.lang.String,java.lang.Object>
      **/
    @PostMapping("/login")
    public Map<String,Object> login(@RequestParam("name")String name, @RequestParam("password") String password){
        Map<String,Object> map = userService.login(name, password);
        return map;
    }
    @GetMapping("/test")
    public String test(String str){
        return "测试验证成功";
    }
}
