package com.quark.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quark.common.entity.Reply;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author LHR
 * Create By 2017/8/20
 */
@CacheConfig(cacheNames = "replies")
public interface ReplyDao extends BaseMapper<Reply> {

    @Cacheable
    List<Reply> findAll();

    Page<Reply> findReplyByPage(@Param("page") Page<Reply> page, @Param("reply") Reply reply);
    Page<Reply> findByPageById(@Param("page") Page<Reply> page, @Param("id") Integer id);

}
