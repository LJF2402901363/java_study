package com.moyisuiying.booksystem;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moyisuiying.booksystem.dao.AccountDao;
import com.moyisuiying.booksystem.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class BooksystemApplicationTests {
    @Autowired
    AccountDao accountDao;
    @Test
    public  void testFindAll(){
        List<Account> accountList = accountDao.selectList(null);
        accountList.forEach(System.out::println);
    }
    @Test
   public void testFindById(){
       Account account = accountDao.selectById(19);
       System.out.println(account);
   }
   @Test
    public void testInsert(){
        Account account = new Account();
        account.setName("硝酸钠");
        account.setPassword("ab");
       int fla = accountDao.insert(account);
       System.out.println(fla);

   }
   @Test
   public void testDeleteById(){
       int deleted = accountDao.deleteById(21);
       System.out.println(deleted);
   }
   @Test
   public void testDeleteByIds(){
       int deleteBatchIds = accountDao.deleteBatchIds(Arrays.asList(22, 23));
       System.out.println(deleteBatchIds);
   }
   @Test
    public void testUpdate(){
        Account account = new Account();
        account.setId(27);
        account.setName("李四");
        account.setPassword("aaaaa");
       int update = accountDao.updateById(account);
       System.out.println(update);
   }
   @Test
   public void testselectByMap(){
       Map<String,Object> map = new HashMap<>();
       map.put("id",19);
       map.put("id",24);
       map.put("id",99);
       List<Account> accountList = accountDao.selectByMap(map);
       accountList.forEach(System.out::print);
   }
    @Test
    public void testselectByWrapper(){

        Account account = new Account();
        account.setPassword("a");
        Wrapper<Account> wrapper = new QueryWrapper<>(account);
        List<Account> accountList = accountDao.selectList(wrapper);
        accountList.forEach(System.out::print);
    }
}
