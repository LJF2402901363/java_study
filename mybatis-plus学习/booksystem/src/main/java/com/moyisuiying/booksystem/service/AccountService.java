package com.moyisuiying.booksystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moyisuiying.booksystem.entity.Account;
import org.springframework.stereotype.Service;

/**
 * Classname:booksystem
 *
 * @description:{description}
 * @author: 陌意随影
 * @Date: 2020-11-25 11:38
 */

public interface AccountService extends IService<Account> {
    /**
     * @Description :自定义的方法，用于实现用户Account登录逻辑实现
     * @Date 12:14 2020/11/25 0025
     * @Param * @param name  用户名
     * @param password ：密码
     * @return com.moyisuiying.booksystem.entity.Account
     **/
    public Account login(String name, String password);

}
