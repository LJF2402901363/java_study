package com.moyisuiying.booksystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Classname:Account
 *
 * @description:用户实体类
 * @author: 陌意随影
 * @Date: 2020-11-24 22:53
 * @Version: 1.0
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    //配置ID属性自增
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String password;
    //逻辑删除
    @TableLogic
    private Integer deleted;
    @Version
    private Integer version;
    //插入时自动生成
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    //更新时自动生成
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
