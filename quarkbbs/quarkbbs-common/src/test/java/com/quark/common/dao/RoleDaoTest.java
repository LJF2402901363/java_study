package com.quark.common.dao;

import com.quark.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

/**
 * Classname:RoleDaoTest
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-05-23 22:43
 * @Version: 1.0
 **/
@SpringBootTest
public class RoleDaoTest {
    @Autowired
    private RoleDao roleDao;
    @Test
    public void testfindRolesByPermissionId() {
        Set<Role> rolesByPermissionId = roleDao.findRolesByPermissionId(1);
        for (Role role : rolesByPermissionId) {
            System.out.println(role);
        }
    }
    @Test
    public void testfindRoleSetByAdminUserId(){
        Set<Role> roleSetByAdminUserId = roleDao.findRoleSetByAdminUserId(3);
        for (Role role : roleSetByAdminUserId) {
            System.out.println(role);
        }
    }

}
