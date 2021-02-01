package com.moyisuiying.jwt.config;

import com.moyisuiying.jwt.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Classname:WebSecurityConfig
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-02-01 12:49
 * @Version: 1.0
 **/
@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {
    @Autowired
    JwtInterceptor jwtInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注意这里不要使用 new JwtInterceptor() ，否则就会出现拦截器JwtInterceptor里无法自动注入JwtUtil的问题
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**").excludePathPatterns("/static/**","/user/login");
    }
}
