package com.quark.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quark.common.entity.User;

/**
 * @Author : ChinaLHR
 * @Date : Create in 21:20 2017/10/23
 * @Email : 13435500980@163.com
 */
public interface ChatService extends IService<User> {

    /**
     * 根据Token获取用户
     * @param token
     * @return
     */
    User getUserByToken(String token);

    /**
     * 验证用户
     * @param id
     * @return
     */
    boolean authUser(Integer id);
}
