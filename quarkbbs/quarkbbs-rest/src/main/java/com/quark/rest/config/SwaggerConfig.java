package com.quark.rest.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Classname:SwaggerConfig
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-05-24 20:14
 * @Version: 1.0
 **/
@Configuration
@EnableOpenApi
public class SwaggerConfig {
    @Bean
    public Docket createDocket(){
    return   new Docket(DocumentationType.OAS_30)
             .apiInfo(apiInfo())
              .select().apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return  new ApiInfoBuilder().title("测试接口")
                .description("这是用于测试前后端连接使用")
                .termsOfServiceUrl("http://moyisuiying.com")
                .contact(new Contact("陌意随影","http://moyisuiying.com","2402901363@qq.com"))
                .version("1.0").build();
    }
}
