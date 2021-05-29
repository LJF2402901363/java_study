package com.quark.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quark.common.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author LHR
 * Create By 2017/8/18
 *
 * 用户
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("quark_user")
public class User implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    //注册邮箱
    private String email;

    // 用户名
    private String username;

    // 密码
    @JsonIgnore
    private String password;

    // 头像
    private String icon ="http://127.0.0.1/images/upload/default.png";

    // 个人签名
    private String signature;

    // 注册时间
    @JsonFormat(pattern = Constants.DATE_FORMAT, timezone = "GMT+8")
    private Date init_time;

    //性别 0 ：男 1：女
    private Integer sex = 0;

    //是否被封禁,默认为１：开启
    private Integer enable = 1;

}
