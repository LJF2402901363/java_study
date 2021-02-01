package com.moyisuiying.jwt.service.impl;

import com.moyisuiying.jwt.entity.LoginUser;
import com.moyisuiying.jwt.entity.User;
import com.moyisuiying.jwt.mapper.UserMapper;
import com.moyisuiying.jwt.service.UserService;
import com.moyisuiying.jwt.uitl.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * Classname:UserServiceImpl
 *
 * @description: 用户的业务逻辑
 * @author: 陌意随影
 * @Date: 2021-01-31 23:26
 * @Version: 1.0
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    JwtUtil jwtUtil;
    @Override
    public Map<String,Object> login(String name, String password) {
        //响应数据的map
        Map<String,Object> resultMap = new HashMap<>();
        //从数据库 中获取登陆用户
        User login = userMapper.login(name,password);
        if (login == null){
            resultMap.put("status","0");
            resultMap.put("msg","该账号尚未注册！");
        }else {
            //生成token的map
            Map<String,String> tokenMap = new HashMap<>();
            tokenMap.put("name",login.getName());
            tokenMap.put("id",String.valueOf(login.getId()));
            //生成token
            String token = jwtUtil.createToken(tokenMap);
            //设置loginUser对象
            LoginUser loginUser = JwtUtil.buildLoginUser(login, token);
            resultMap.put("status","1");
            resultMap.put("msg","登录成功");
            resultMap.put("loginUser",loginUser);
            log.info("用户名:[{}]",name);
            log.info("用户密码[{}]",password);

        }
        return resultMap;
    }
}
