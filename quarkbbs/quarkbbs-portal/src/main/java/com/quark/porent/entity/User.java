package com.quark.porent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author LHR
 * Create By 2017/8/18
 * <p>
 * 用户
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;


    private String email;


    private String username;


    // 头像
    private String Icon;

    // 个人签名
    private String signature;


    private Date initTime;

    //性别 0 ：男 1：女
    private Integer sex = 0;

    //是否被封禁,默认为１：开启
    private Integer enable = 1;

}
