package com.quark.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quark.common.entity.AdminUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by lhr on 17-7-31.
 */
@CacheConfig(cacheNames = "adminusers")
public interface AdminUserDao extends BaseMapper<AdminUser> {


    AdminUser findByUsername(@Param("username") String username);

    Page<AdminUser> findByPage(@Param("page") Page<AdminUser> page, @Param("adminUser") AdminUser adminUser);

    AdminUser findAdminUserById(@Param("id") Integer id);
    /**
     * @Description :根据roleId获取所有的AdminUser
     * @Date 23:24 2021/5/23 0023
     * @Param * @param roleId ：roleId
     * @return java.util.List<com.quark.common.entity.AdminUser>
     **/
    Set<AdminUser> findAdminUserByRoleId(@Param("roleId") Integer roleId);
//    List<AdminUser> findAll(Specification specification, SpringDataWebProperties.Sort sort);
}
