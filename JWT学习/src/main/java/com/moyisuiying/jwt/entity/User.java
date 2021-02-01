package com.moyisuiying.jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Classname:User
 *
 * @description:实体类USer
 * @author: 陌意随影
 * @Date: 2021-01-31 23:10
 * @Version: 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private int id;
    private String name;
    private String password;
}
