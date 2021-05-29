package com.quark.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author LHR
 * Create By 2017/8/18
 *
 * 标签
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("quark_label")
public class Label implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer postsCount = 0;
    //详情
    private String details;
}
