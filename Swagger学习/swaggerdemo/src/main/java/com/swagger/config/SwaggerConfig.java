package com.swagger.config;

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
 * @Date: 2020-11-22 10:46
 * @Version: 1.0
 **/
@Configuration
@EnableOpenApi
public class SwaggerConfig {
        @Bean
         public Docket createDocket1(){
            ApiInfo apiInfo = apiInfo("测试接口C","用于测试接口C");
          return new Docket(DocumentationType.OAS_30).groupName("C").
                  apiInfo(apiInfo).select().
                  apis(RequestHandlerSelectors.basePackage("com.swagger.controller")).
                  paths(PathSelectors.any()).build();
         }
    @Bean
    public Docket createDocket2(){
        ApiInfo apiInfo = apiInfo("测试接口B","用于测试接口B");
        return new Docket(DocumentationType.OAS_30).groupName("B").groupName("B").
                apiInfo(apiInfo).select().
                apis(RequestHandlerSelectors.basePackage("com.swagger.controller")).
                paths(PathSelectors.any()).build();
    }
    @Bean
    public Docket createDocket3(){
        ApiInfo apiInfo = apiInfo("测试接口A","用于测试接口A");
        return new Docket(DocumentationType.OAS_30).groupName("A").
                apiInfo(apiInfo).select().
                apis(RequestHandlerSelectors.basePackage("com.swagger.controller")).
                paths(PathSelectors.any()).build();
    }
         public ApiInfo apiInfo(String title,String description) {
             return  new ApiInfoBuilder().title(title).description(description).termsOfServiceUrl("http://moyisuiying.com").contact(new Contact("陌意随影","http://moyisuiying.com","2402901363@qq.com")).version("v1.0").build();
         }
}
