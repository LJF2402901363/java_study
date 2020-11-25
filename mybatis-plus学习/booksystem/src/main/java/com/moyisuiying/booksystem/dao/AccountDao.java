package com.moyisuiying.booksystem.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moyisuiying.booksystem.entity.Account;
import org.springframework.stereotype.Repository;

/**
 * Classname:AccountDao
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2020-11-24 22:57
 * @Version: 1.0
 **/
@Repository
public  interface AccountDao   extends BaseMapper<Account> {
}
