package com.moyisuiying.booksystem.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classname:Config
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2020-11-25 21:21
 * @Version: 1.0
 **/
    //Spring boot方式
    @Configuration
    @MapperScan("com.moyisuiying.booksystem.dao")
    @Slf4j
    public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor paginationInnerInterceptor(){
        MybatisPlusInterceptor myBatisInterceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setOverflow(true);
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        myBatisInterceptor.addInnerInterceptor(paginationInnerInterceptor);
        return myBatisInterceptor;
    }
    }
