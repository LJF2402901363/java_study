package com.quark.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quark.common.entity.Notification;
import com.quark.common.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author LHR
 * Create By 2017/9/6
 */
public interface NotificationDao extends BaseMapper<Notification> {

    long getNotificationCount(@Param("id") Integer id);

    int updateByIsRead(@Param("userId") Integer userId);
    /**
     * @Description :通过用户的id获取全部未读信息
     * @Date 22:03 2021/5/24 0024
     * @Param * @param uid ：用户ID
     * @return java.util.List<com.quark.common.entity.Notification>
     **/
    List<Notification> findUnreadNotificationByUserId(@Param("uid") Integer uid);
    /**
     * @Description :通过用户的id获取全部已读信息
     * @Date 22:03 2021/5/24 0024
     * @Param * @param uid ：用户ID
     * @return java.util.List<com.quark.common.entity.Notification>
     **/
    List<Notification> findReadedNotificationByUserId(@Param("uid")Integer uid);
}
