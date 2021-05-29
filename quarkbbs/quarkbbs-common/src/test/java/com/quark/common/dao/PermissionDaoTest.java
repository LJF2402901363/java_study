package com.quark.common.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quark.common.entity.Permission;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * Classname:PermissionDaoTest
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-05-23 22:49
 * @Version: 1.0
 **/
@SpringBootTest
public class PermissionDaoTest {
    @Autowired
    private PermissionDao permissionDao;
    @Test
    public void test(){
        List<Permission> rolePermissionByRoleId = permissionDao.findRolePermissionByRoleId(6);
        rolePermissionByRoleId.forEach(permission -> {
            System.out.println(permission);
        });
    }
    @Test
    public void testfindPermissonByPage(){
        Page<Permission> permissonByPage = permissionDao.findPermissonByPage(new Page<>(1, 4));
        permissonByPage.getRecords().forEach(permission -> {
            System.out.println(permission);
        });
    }
}
