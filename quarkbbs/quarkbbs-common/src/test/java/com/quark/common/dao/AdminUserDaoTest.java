package com.quark.common.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quark.common.entity.AdminUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Classname:AdminUserDaoTest
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-05-22 16:40
 * @Version: 1.0
 **/
@SpringBootTest
public class AdminUserDaoTest {
    @Autowired
    private AdminUserDao adminUserDao;
    @Test
    public void testFindAdminUserByName(){
        AdminUser lhr = adminUserDao.findByUsername("lhr");
        System.out.println(lhr);
    }
    @Test
    public void testFindAdminUserByPage(){
        Page<AdminUser> page = new Page<>(1,10);
        AdminUser user = new AdminUser();
        user.setId(51);
        user.setUsername("lhr");
        user.setPassword("root");
        Page<AdminUser> byPage = adminUserDao.findByPage(page,user);
        byPage.getRecords().forEach(e->{
            System.out.println(e);
        });

    }
    @Test
    public  void test(){

    }
}
