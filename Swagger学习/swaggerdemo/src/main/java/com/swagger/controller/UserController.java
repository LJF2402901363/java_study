package com.swagger.controller;

import com.swagger.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classname:HellowController
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2020-11-21 22:47
 * @Version: 1.0
 **/
@Controller
@RequestMapping("/user")
@ApiModel("用户控制类")
public class UserController {
    @GetMapping("/hello")
    @ResponseBody
    @ApiOperation("用户hello")
    public String hello(){
        return "hello";
    }
    @GetMapping("/findUser/{name}/{password}")
    @ResponseBody
    @ApiOperation("通过名字和密码查找用户")
    public User findUser(@ApiParam("用户名字") @PathVariable String name, @ApiParam("用户密码")@PathVariable String password){
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        return user;
    }
    @GetMapping("/findAll")
    @ApiOperation("查找所有用户")
    @ResponseBody
    public List<User> findAll(){
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setName("A");
        user.setPassword("A");
        userList.add(user);
        return userList;
    }
    @GetMapping("/getUserJson/{id}")
    @ApiOperation("转换字符串")
    @ResponseBody
    public String userJson(@ApiParam("用户ID")@PathVariable("id") int id){
        User user = new User();
        user.setName("A");
        user.setAge(32);
        user.setPassword("B");
        user.setBirthDay(LocalDate.now());
        return user.toString();
    }
    @PostMapping("/getUserById")
    @ResponseBody
    @ApiOperation("通过ID获取用户")
    public User getUserById(@ApiParam("用户ID")@PathVariable("id") int id){
        User user = new User();
        user.setName("A");
        user.setAge(32);
        user.setPassword("B");
        user.setBirthDay(LocalDate.now());
        return user;
    }
    
    
}
