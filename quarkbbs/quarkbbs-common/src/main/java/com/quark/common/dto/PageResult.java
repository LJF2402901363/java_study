package com.quark.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 后台翻页响应数据
 *
 * @Author LHR
 * Create By 2017/8/10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    private String draw;//表示请求次数

    private Long recordsTotal;//总记录数

    private Long recordsFiltered;//过滤后的总记录数

    private T data;//具体的数据


}
