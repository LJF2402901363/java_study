package com.quark.rest.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.quark.common.entity.Label;
import com.quark.common.entity.Posts;
import com.quark.common.entity.User;

import java.util.List;

/**
 * @Author LHR
 * Create By 2017/8/26
 */
public interface PostsService extends IService<Posts> {

    /**
     * 保存帖子
     * @param posts 帖子
     * @param labelId 标签id
     */
    void savePosts(Posts posts,Integer labelId,User user);
    /**
     * @Description :模糊搜素置顶帖子
     * @Date 18:25 2021/5/25 0025
     * @Param * @param search  模糊搜索字符串
     * @param pageNo 起始页
     * @param length ：每页大小
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.quark.common.entity.Posts>
     **/
    Page<Posts> getPostsPageByTop(String search, int pageNo,int length);
    /**
     * @Description :模糊搜素好评帖子
     * @Date 18:25 2021/5/25 0025
     * @Param * @param search  模糊搜索字符串
     * @param pageNo 起始页
     * @param length ：每页大小
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.quark.common.entity.Posts>
     **/
    Page<Posts> getPostsPageByGood(String search, int pageNo,int length);
    /**
     * @Description :模糊搜素帖子
     * @Date 18:25 2021/5/25 0025
     * @Param * @param search  模糊搜索字符串
     * @param pageNo 起始页
     * @param length ：每页大小
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.quark.common.entity.Posts>
     **/
    Page<Posts> getPostsPage(String search, int pageNo,int length);
    /**
     * 获取用户最近发布的10个POSTS
     * @param id
     * @return
     */
    List<Posts> getPostsByUserId(Integer id);


    /**
     * 根据标签分页获取获取Posts
     * @param labelId
     * @return
     */
    Page<Posts> getPostsByLabelId(Integer labelId, int pageNo, int lenght);
}
