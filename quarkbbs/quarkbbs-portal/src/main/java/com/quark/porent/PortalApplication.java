package com.quark.porent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.IOException;

/**
 * @Author LHR
 * Create By 2017/8/21
 */
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class PortalApplication {


    public static void main(String[] args) throws IOException {
        //更改properties配置文件名称,避免依赖冲突
        SpringApplication.run(PortalApplication.class,args);
    }

}
