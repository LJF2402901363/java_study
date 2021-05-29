package com.quark.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quark.common.entity.Permission;
import com.quark.common.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by lhr on 17-7-31.
 */
@CacheConfig(cacheNames = "permissions")
public interface PermissionDao extends BaseMapper<Permission> {


    @Cacheable
    List<Permission> findAll();
    /**
     * @Description :根据角色id查找权限
     * @Date 22:00 2021/5/23 0023
     * @Param * @param roleId ：角色id
     * @return java.util.List<com.quark.common.entity.Permission>
     **/
    List<Permission> findRolePermissionByRoleId(@Param("roleId") Integer roleId);
    /**
     * @Description :根据roleId获取所有的Permission
     * @Date 23:24 2021/5/23 0023
     * @Param * @param roleId ：roleId
     * @return java.util.List<com.quark.common.entity.AdminUser>
     **/
    Set<Permission> findSimplePermissionByRoleId(@Param("roleId") Integer roleId);
    /**
     * @Description :分页查找权限
     * @Date 0:31 2021/5/24 0024
     * @Param * @param page ：
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.quark.common.entity.Permission>
     **/
    Page<Permission> findPermissonByPage(Page<Permission> page);
}
