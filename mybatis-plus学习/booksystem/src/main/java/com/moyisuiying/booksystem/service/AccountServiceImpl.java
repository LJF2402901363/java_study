package com.moyisuiying.booksystem.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyisuiying.booksystem.entity.Account;
import org.springframework.stereotype.Service;
/**
 * Classname:AccountServiceImpl
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2020-11-25 11:49
 * @Version: 1.0
 **/
@Service("accountService")
public class AccountServiceImpl  extends ServiceImpl<BaseMapper<Account>, Account> implements AccountService{
    @Override
    public Account login(String name, String password) {
        //实例化查询条件对象
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        //设置查询条件
        queryWrapper.eq("name",name).eq("password", password);
        Account loginUser = this.getOne(queryWrapper);
        return  loginUser;
    }
}
