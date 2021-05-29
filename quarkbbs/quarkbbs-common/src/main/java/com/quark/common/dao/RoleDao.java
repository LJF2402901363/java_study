package com.quark.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quark.common.entity.AdminUser;
import com.quark.common.entity.Permission;
import com.quark.common.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Set;

/**
 * Created by lhr on 17-7-31.
 */

@CacheConfig(cacheNames = "roles")
public interface RoleDao extends BaseMapper<Role> {

    @Cacheable
    List<Role> findAll();
    /**
     * @Description :通过权限id找到对应的角色集合
     * @Date 22:32 2021/5/23 0023
     * @Param * @param permissionId ：
     * @return java.util.List<com.quark.common.entity.Role>
     **/
    Set<Role> findRolesByPermissionId(@Param("permissionId")Integer permissionId);


    /**
     * @Description :通过用户的id查找到角色集合roleSet
     * @Date 23:32 2021/5/23 0023
     * @Param * @param adminId ：用户的ID
     * @return java.util.Set<com.quark.common.entity.Role>
     **/
    Set<Role> findRoleSetByAdminUserId(@Param("adminUserId")Integer adminId);
    /**
     * @Description :根据角色id获取角色Role
     * @Date 0:24 2021/5/24 0024
     * @Param * @param id ：角色id
     * @return com.quark.common.entity.Role
     **/
    Role findRoleByRoleId(@Param("roleId") Integer roleId);
}
