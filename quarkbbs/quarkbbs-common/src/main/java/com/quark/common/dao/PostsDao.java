package com.quark.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quark.common.entity.Label;
import com.quark.common.entity.Posts;
import com.quark.common.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@CacheConfig(cacheNames = "postses")
public interface PostsDao extends BaseMapper<Posts> {

    @Cacheable
    List<Posts> findAll();

    List<Posts> findHot(@Param("startTime")String startTime,@Param("endTime")String endTime);

    IPage<Posts> findByUser(@Param("user") User user);

    IPage<Posts> findByLabel(@Param("label") Label label);
    /**
     * @Description :根据posts查找分页数据
     * @Date 20:03 2021/5/24 0024
     * @Param * @param page
     * @param posts ：
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.quark.common.entity.Posts>
     **/
    Page<Posts> findPostsByPage(@Param("page") Page<Posts> page, @Param("posts") Posts posts);
    /**
     * @Description :根据posts查找分页数据
     * @Date 20:03 2021/5/24 0024
     * @Param * @param page
     * @param posts ：
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.quark.common.entity.Posts>
     **/
    Page<Posts> findPostsPageByType(@Param("page") Page<Posts> page, @Param("posts") Posts posts,@Param("type") String type);
    /**
     * @Description :根据id或者posts
     * @Date 2:23 2021/5/24 0024
     * @Param * @param postsId ：
     * @return com.quark.common.entity.Posts
     **/
    Posts findPostById(@Param("postsId")Integer postsId);
    Page<Posts> getPostsByUserId(@Param("page") Page<Posts> page, @Param("id") Integer id);

    Page<Posts> getPostsByLabelId(@Param("page") Page<Posts> page, @Param("label_id") Integer label_id);
}
