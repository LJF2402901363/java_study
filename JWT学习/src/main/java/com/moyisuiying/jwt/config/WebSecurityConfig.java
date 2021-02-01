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
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**").excludePathPatterns("/static/**","/user/login");
    }
}
