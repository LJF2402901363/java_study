package com.quark.common.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quark.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Classname:UserDaoTest
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-05-24 01:29
 * @Version: 1.0
 **/
@SpringBootTest
public class UserDaoTest {
    @Autowired
    private UserDao userDao;
    @Test
    public void test(){
        Page<User> byPage = userDao.findUserByPage(new Page<>(1, 5), new User());
        byPage.getRecords().forEach(user -> {
            System.out.println(user);
        });
    }
}
