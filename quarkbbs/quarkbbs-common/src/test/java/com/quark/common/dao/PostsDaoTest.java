package com.quark.common.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quark.common.entity.Posts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Classname:PostsDaoTest
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-05-24 01:44
 * @Version: 1.0
 **/
@SpringBootTest
public class PostsDaoTest {
    @Autowired
    private PostsDao postsDao;
    @Test
    public void test(){
        Posts posts = new Posts();
        posts.setGood(false);
        posts.setTop(false);
        Page<Posts> byPage = postsDao.findPostsByPage(new Page<>(1, 5), posts);
        byPage.getRecords().forEach(e->{
            System.out.println(e);
        });
    }

}
