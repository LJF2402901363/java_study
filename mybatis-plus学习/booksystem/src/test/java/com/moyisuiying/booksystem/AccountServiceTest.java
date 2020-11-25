package com.moyisuiying.booksystem;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.moyisuiying.booksystem.entity.Account;
import com.moyisuiying.booksystem.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classname:AccountServiceTest
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2020-11-25 11:40
 * @Version: 1.0
 **/
@SpringBootTest
public class AccountServiceTest {
    @Autowired
    AccountService accountService;
    @Test
    public void testSave(){
        Account account = new Account();
        boolean save = accountService.save(account);
        System.out.println(save);
    }
    @Test
    public void testUpdate(){
        Account newAccount = new Account();
        newAccount.setPassword("陌意随影");
        newAccount.setPassword("root");
        newAccount.setId(1);
        boolean updateById = accountService.updateById(newAccount);
        System.out.println(updateById);
    }
    @Test
    public void testRemoveById(){
        boolean b = accountService.removeById(25);
        System.out.println(b);
    }
    @Test
    public void testRemoveByIds(){
        boolean b = accountService.removeByIds(Arrays.asList(19,28));
        System.out.println(b);
    }
    @Test
    public void testGetOneById(){
        Account byId = accountService.getById(1);
        System.out.println(byId);
    }
    @Test
    public void testGetAll(){
        List<Account> accountList = accountService.getBaseMapper().selectList(null);
        accountList.forEach(System.out::println);
    }
    @Test
    public void  testLogin(){
        Account login = accountService.login("陌意随影", "root");
        System.out.println(login);
    }
    @Test
    public void testGetOneByWrapper(){
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        Map<String,Object> map = new HashMap<>();
        map.put("password","b");
        map.put("name",null);
        queryWrapper.allEq(map, false);
        Account account = accountService.getOne(queryWrapper);
        System.out.println(account);
    }
    @Test
    public void testUpDateByWrapper(){
        Account account = new Account();
        UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",19);
        accountService.update(account,updateWrapper);
    }
}
