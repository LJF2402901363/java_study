package com.quark.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quark.common.entity.Label;
import org.apache.ibatis.annotations.Param;

public interface LabelDao extends BaseMapper<Label> {
    /**
     * @Description :分页查找标签数据
     * @Date 10:35 2021/5/24 0024
     * @Param * @param page ：
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.quark.common.entity.Label>
     **/
    Page<Label> findLabelByPage(Page<Label> page);
    /**
     * @Description :分页模糊查找标签数据
     * @Date 10:35 2021/5/24 0024
     * @Param * @param page ：
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.quark.common.entity.Label>
     **/
    Page<Label> findSearchLabelByPage(@Param("page") Page<Label> page,@Param("label") Label label);
}
