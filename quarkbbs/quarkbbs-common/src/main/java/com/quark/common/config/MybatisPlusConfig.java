package com.quark.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classname:MybatisPlusConfig
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-05-15 20:42
 * @Version: 1.0
 **/
@Configuration
@MapperScan("com.quark.common.dao")
public class MybatisPlusConfig {
    /**
     * @Description :配置分页插件
     * @Date 20:46 2021/5/15 0015
     * @Param * @param  ：
     * @return com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
     **/
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
