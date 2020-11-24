package com.swagger.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * Classname:User
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2020-11-22 13:36
 * @Version: 1.0
 **/
@Repository
@Scope("prototype")
@ApiModel("用户类")
public class User {
    @ApiModelProperty("用户名字")
    private String name;
    @ApiModelProperty("用户密码")
    private String password;
    @ApiModelProperty("用户年龄")
    private int age;
    @ApiModelProperty("用户生日")
    private LocalDate birthDay;

    public User() {
    }

    public User(String name, String password, int age, LocalDate localDate) {
        this.name = name;
        this.password = password;
        this.age = age;
        this.birthDay = localDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", localDate=" + birthDay +
                '}';
    }
}
