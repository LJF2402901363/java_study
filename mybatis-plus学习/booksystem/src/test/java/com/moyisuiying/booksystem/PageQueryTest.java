package com.moyisuiying.booksystem;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyisuiying.booksystem.dao.AccountDao;
import com.moyisuiying.booksystem.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Classname:PageQueryTest
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2020-11-25 21:30
 * @Version: 1.0
 **/
@SpringBootTest
public class PageQueryTest {
    @Autowired
    AccountDao accountDao;
    @Test
    public  void testPage(){
        Page<Account> page = new Page<>(2,5);
        Page<Account> accountPage = accountDao.selectPage(page, null);
        accountPage.getRecords().forEach(System.out::println);

    }
}
