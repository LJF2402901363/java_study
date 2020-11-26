package com.moyisuiying.booksystem.service.impl;

import com.moyisuiying.booksystem.entity.Account;
import com.moyisuiying.booksystem.mapper.AccountMapper;
import com.moyisuiying.booksystem.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 陌意随影
 * @since 2020-11-26
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

}
