package com.quark.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.Transient;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lhr on 17-7-31.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("quark_role")
public class Role implements Serializable{

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;

    //角色描述
    private String description;

    //是否持有角色标志
    @TableField(exist = false)
    private Integer selected;

    //角色与用户的关联关系
    @JsonIgnore
    @TableField(exist = false)
    private Set<AdminUser> adminUsers ;

    //角色与权限的关联关系
    @JsonIgnore
    @TableField(exist = false)
    private Set<Permission> permissions ;
}
