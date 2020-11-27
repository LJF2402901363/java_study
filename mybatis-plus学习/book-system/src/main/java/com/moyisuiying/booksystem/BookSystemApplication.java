package com.moyisuiying.booksystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.moyisuiying.booksystem.mapper")
public class BookSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookSystemApplication.class, args);
    }

}
