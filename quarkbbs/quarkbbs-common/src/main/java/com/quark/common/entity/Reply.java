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
 * 回复
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("quark_reply")
public class Reply implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String content;

    //回复时间
    @JsonFormat(pattern = Constants.DATETIME_FORMAT, timezone = "GMT+8")
    private Date init_time;

    //点赞个数
    private Integer up = 0;

    @JsonIgnore
    @TableField(exist = false)
    private Posts posts;
    private Integer posts_id;
    @TableField(exist = false)
    private User user;
    private Integer user_id;
}
