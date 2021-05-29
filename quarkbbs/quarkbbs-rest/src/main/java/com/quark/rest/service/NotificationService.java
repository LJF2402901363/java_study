package com.quark.rest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quark.common.entity.Notification;
import com.quark.common.entity.User;

import java.util.List;

/**
 * @Author LHR
 * Create By 2017/9/6
 */
public interface NotificationService extends IService<Notification> {

    /**
     * 获取用户的未读通知数量
     * @param uid
     * @return
     */
    long getNotificationCount(int uid);

    /**
     * 获取用户所有通知
     * @param uid
     */
    List<Notification> findReadedNotificationByUserId(Integer uid);

    /**
     * 根据用户删除所有通知
     * @param uid
     * @return
     */
    void deleteNotificationByUserId(Integer uid);
    /**
     * @Description : 通过用户的id获取全部的信息
     * @Date 22:01 2021/5/24 0024
     * @Param * @param uid ：
     * @return java.util.List<com.quark.common.entity.Notification>
     **/
    List<Notification> findUnreadNotificationByUserId(Integer uid);
}
