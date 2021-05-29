package com.quark.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.oas.annotations.EnableOpenApi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by lhr on 17-7-31.
 */
@SpringBootApplication
@EnableCaching//缓存支持

public class RestApplication {

    public static void main(String[] args) throws IOException {
       SpringApplication.run(RestApplication.class,args);
    }


}

