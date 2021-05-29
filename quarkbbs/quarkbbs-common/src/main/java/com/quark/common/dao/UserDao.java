package com.quark.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quark.common.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@CacheConfig(cacheNames = "users")
public interface UserDao extends BaseMapper<User> {

    User findUserByUserId(@Param("userId") Integer userId);
    User findByUsername(@Param("username") String username);

    User findByEmail(@Param("email") String email);

//    @Query(value = "select u.id, u.username , u.icon from quark_user u where DATE_SUB(CURDATE(), INTERVAL 30 DAY) <=DATE(u.init_time) ORDER BY u.id DESC limit 12" ,nativeQuery = true)
    List<User> findNewUser(@Param("startTime")String startTime,@Param("endTime")String endTime);

    Page<User> findUserByPage(@Param("page") Page<User> page, @Param("user") User user);
}
