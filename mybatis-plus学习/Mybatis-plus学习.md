# Mybatis-plus学习

## 1.初步认识

### 1.1简介

Mybatis-Plus（简称MP）是一个 Mybatis 的增强工具，在 Mybatis 的基础上只做增强不做改变，为简化开发、提高效率而生。这是官方给的定义，关于mybatis-plus的更多介绍及特性，可以参考[mybatis-plus官网](https://links.jianshu.com/go?to=http%3A%2F%2Fmp.baomidou.com%2F%23%2F)。那么它是怎么增强的呢？其实就是它已经封装好了一些crud方法，我们不需要再写xml了，直接调用这些方法就行，就类似于JPA。

### 1.2.特性

- **无侵入**：只做增强不做改变，引入它不会对现有工程产生影响，如丝般顺滑
- **损耗小**：启动即会自动注入基本 CURD，性能基本无损耗，直接面向对象操作
- **强大的 CRUD 操作**：内置通用 Mapper、通用 Service，仅仅通过少量配置即可实现单表大部分 CRUD 操作，更有强大的条件构造器，满足各类使用需求
- **支持 Lambda 形式调用**：通过 Lambda 表达式，方便的编写各类查询条件，无需再担心字段写错
- **支持主键自动生成**：支持多达 4 种主键策略（内含分布式唯一 ID 生成器 - Sequence），可自由配置，完美解决主键问题
- **支持 ActiveRecord 模式**：支持 ActiveRecord 形式调用，实体类只需继承 Model 类即可进行强大的 CRUD 操作
- **支持自定义全局通用操作**：支持全局通用方法注入（ Write once, use anywhere ）
- **内置代码生成器**：采用代码或者 Maven 插件可快速生成 Mapper 、 Model 、 Service 、 Controller 层代码，支持模板引擎，更有超多自定义配置等您来使用
- **内置分页插件**：基于 MyBatis 物理分页，开发者无需关心具体操作，配置好插件之后，写分页等同于普通 List 查询
- **分页插件支持多种数据库**：支持 MySQL、MariaDB、Oracle、DB2、H2、HSQL、SQLite、Postgre、SQLServer 等多种数据库
- **内置性能分析插件**：可输出 Sql 语句以及其执行时间，建议开发测试时启用该功能，能快速揪出慢查询
- **内置全局拦截插件**：提供全表 delete 、 update 操作智能分析阻断，也可自定义拦截规则，预防误操作

### 1.3框架结构

![image-20201122200900533](images\1.png)

### 1.4  快速开始

#### 1.4.1新建一个javaweb数据库：

```
create database if not  exists javaweb;
```

#### 1.4.2新建一个Account表

 ~~~xml
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id主键，自增',
  `name` varchar(32) DEFAULT NULL,
  `password` varchar(50) NOT NULL COMMENT '密码',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` int(11) DEFAULT '0',
  `type` int(11) DEFAULT NULL,
  `hobby` varchar(128) DEFAULT NULL,
  `signature` varchar(128) DEFAULT NULL,
  `age` int(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COMMENT='用户表';
 ~~~

#### 1.4.3使用Navicat手动插入数据：

![image-20201124234112497](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201124234112497.png)

#### 1.4.4创建一个springboot项目

![image-20201124234451760](E:\新技术学习\java_study\mybatis-plus学习\images\image-20201124234451760.png)



配置application.properties文件：

```properties
server.port=8080
#数据库连接的用户名
spring.datasource.username=root
#数据库连接的密码
spring.datasource.password=root
#数据库连接的URL
spring.datasource.url=jdbc:mysql://localhost:3306/javaweb?serverTimezone=UTC
#数据库连接的驱动   MySQL8使用com.mysql.cj.jdbc.Driver   ,mysql5使用com.mysql.jdbc.Driver
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
#配置日志
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
```



#### 1.4.5在entity包中新建一个实体类Account.java（为了简便这里只对应了account表中的三个字段）

```java
package com.moyisuiying.booksystem.entity;

import lombok.*;
import org.springframework.stereotype.Repository;

/**
 * Classname:Account
 *
 * @description:用户实体类
 * @author: 陌意随影
 * @Date: 2020-11-24 22:53
 * @Version: 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private int id;
    private String name;
    private String password;

}

```

#### 1.4.6在dao包中新建一个接口类AccountDao.java继承于BaseMapper.java接口

```java
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

```

#### 1.4.7在主启动类BooksystemApplication.java中添加扫描dao包的接口

```java
package com.moyisuiying.booksystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.moyisuiying.booksystem.dao")
public class BooksystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooksystemApplication.class, args);
    }

}
```

#### 1.4.8在测试类中新建测试

```java
package com.moyisuiying.booksystem;

import com.moyisuiying.booksystem.dao.AccountDao;
import com.moyisuiying.booksystem.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BooksystemApplicationTests {
    @Autowired
    AccountDao accountDao;
    @Test
    public  void testFindAll(){
        List<Account> accountList = accountDao.selectList(null);
        accountList.forEach(System.out::println);
    }

}

```

#### 1.4.9开始测试

![image-20201124235437334](E:\新技术学习\java_study\mybatis-plus学习\images\image-20201124235437334.png)

可见测试访问查找所有成功。

## 2.使用BaseMapper以及了解其原理

### 2.1查看BaseMapper的源码

AccountDao.java接口继承于mybatis-plus提供的BaseMapper.java接口，而BaseMapper.java接口继承于Mapper.java接口。我们首先看Mapper.java接口里面的方法：

```java
**
 * 顶级Mapper
 *
 * @author nieqiurong 2019/4/13.
 */
public interface Mapper<T> {

}

```

该接口里面没有任何方法，只是定义了一个接口类。

然后接着看BaseMapper.java接口：

```java
/**
 * Mapper 继承该接口后，无需编写 mapper.xml 文件，即可获得CRUD功能
 * <p>这个 Mapper 支持 id 泛型</p>
 *
 * @author hubin
 * @since 2016-01-23
 */
public interface BaseMapper<T> extends Mapper<T> {

    /**
     * 插入一条记录
     *
     * @param entity 实体对象
     */
    int insert(T entity);

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    int deleteById(Serializable id);

    /**
     * 根据 columnMap 条件，删除记录
     *
     * @param columnMap 表字段 map 对象
     */
    int deleteByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);

    /**
     * 根据 entity 条件，删除记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null,里面的 entity 用于生成 where 语句）
     */
    int delete(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    /**
     * 删除（根据ID 批量删除）
     *
     * @param idList 主键ID列表(不能为 null 以及 empty)
     */
    int deleteBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);

    /**
     * 根据 ID 修改
     *
     * @param entity 实体对象
     */
    int updateById(@Param(Constants.ENTITY) T entity);

    /**
     * 根据 whereEntity 条件，更新记录
     *
     * @param entity        实体对象 (set 条件值,可以为 null)
     * @param updateWrapper 实体对象封装操作类（可以为 null,里面的 entity 用于生成 where 语句）
     */
    int update(@Param(Constants.ENTITY) T entity, @Param(Constants.WRAPPER) Wrapper<T> updateWrapper);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     */
    T selectById(Serializable id);

    /**
     * 查询（根据ID 批量查询）
     *
     * @param idList 主键ID列表(不能为 null 以及 empty)
     */
    List<T> selectBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);

    /**
     * 查询（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     */
    List<T> selectByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);

    /**
     * 根据 entity 条件，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    T selectOne(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    /**
     * 根据 Wrapper 条件，查询总记录数
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    Integer selectCount(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    /**
     * 根据 entity 条件，查询全部记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    List<T> selectList(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    /**
     * 根据 Wrapper 条件，查询全部记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    List<Map<String, Object>> selectMaps(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    /**
     * 根据 Wrapper 条件，查询全部记录
     * <p>注意： 只返回第一个字段的值</p>
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    List<Object> selectObjs(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    <E extends IPage<T>> E selectPage(E page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    /**
     * 根据 Wrapper 条件，查询全部记录（并翻页）
     *
     * @param page         分页查询条件
     * @param queryWrapper 实体对象封装操作类
     */
    <E extends IPage<Map<String, Object>>> E selectMapsPage(E page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
}
```

通过阅读源码，我们发现该接口定义了很多用于操作数据库实现增删查改（CRUD）功能的函数。具体的函数以及它的功能源码中已经有说明了。



### 2.1测试Mapper的增删查改功能

```java
package com.moyisuiying.booksystem;

import com.moyisuiying.booksystem.dao.AccountDao;
import com.moyisuiying.booksystem.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

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
        Account account = new Account(28,"张三","bb");
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
        account.setPassword("232335");
       int update = accountDao.updateById(account);
       System.out.println(update);
   }
}

```

## 3.使用通用的IService实现crud功能以及了解它的原理

### 3.1首先看mybatisplus提供的通用IService.java这个接口类提供的方法



#### 3.1.1Service CRUD 接口

①通用 Service CRUD 封装[IService](https://gitee.com/baomidou/mybatis-plus/blob/3.0/mybatis-plus-extension/src/main/java/com/baomidou/mybatisplus/extension/service/IService.java)接口，进一步封装 CRUD 采用 `get 查询单行` `remove 删除` `list 查询集合` `page 分页` 前缀命名方式区分 `Mapper` 层避免混淆，

②泛型 `T` 为任意实体对象

③建议如果存在自定义通用 Service 方法的可能，请创建自己的 `IBaseService` 继承 `Mybatis-Plus` 提供的基类

④对象 `Wrapper` 为 [条件构造器](https://baomidou.com/guide/wrapper.html)

⑤IService<T> 的默认实现类为ServiceImpl.java

![image-20201125120823383](E:\新技术学习\java_study\mybatis-plus学习\images\image-20201125120823383.png)

从这个实现类中我们可以看到，ServiceImpl.java使用了BaseMapper或者BaseMapper的子类用于实现增删查改等功能，实际上也就是在BaseMapper功能的基础上封装了一些方法。ServiceImpl功能的实现还是要依赖于BaseMapper。





#### 3.1.2Save

```java
// 插入一条记录（选择字段，策略插入）
boolean save(T entity);
// 插入（批量）
boolean saveBatch(Collection<T> entityList);
// 插入（批量）
boolean saveBatch(Collection<T> entityList, int batchSize);
```

##### 参数说明

|     类型      |   参数名   |     描述     |
| :-----------: | :--------: | :----------: |
|       T       |   entity   |   实体对象   |
| Collection<T> | entityList | 实体对象集合 |
|      int      | batchSize  | 插入批次数量 |

#### 3.1.4SaveOrUpdate

```java
// TableId 注解存在更新记录，否插入一条记录
boolean saveOrUpdate(T entity);
// 根据updateWrapper尝试更新，否继续执行saveOrUpdate(T)方法
boolean saveOrUpdate(T entity, Wrapper<T> updateWrapper);
// 批量修改插入
boolean saveOrUpdateBatch(Collection<T> entityList);
// 批量修改插入
boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize);
```

##### 参数说明

|     类型      |    参数名     |               描述               |
| :-----------: | :-----------: | :------------------------------: |
|       T       |    entity     |             实体对象             |
|  Wrapper<T>   | updateWrapper | 实体对象封装操作类 UpdateWrapper |
| Collection<T> |  entityList   |           实体对象集合           |
|      int      |   batchSize   |           插入批次数量           |

#### 3.1.5Remove

```java
// 根据 entity 条件，删除记录
boolean remove(Wrapper<T> queryWrapper);
// 根据 ID 删除
boolean removeById(Serializable id);
// 根据 columnMap 条件，删除记录
boolean removeByMap(Map<String, Object> columnMap);
// 删除（根据ID 批量删除）
boolean removeByIds(Collection<? extends Serializable> idList);
```

##### 参数说明

|                类型                |    参数名    |          描述           |
| :--------------------------------: | :----------: | :---------------------: |
|             Wrapper<T>             | queryWrapper | 实体包装类 QueryWrapper |
|            Serializable            |      id      |         主键ID          |
|        Map<String, Object>         |  columnMap   |     表字段 map 对象     |
| Collection<? extends Serializable> |    idList    |       主键ID列表        |

#### 3.1.6Update

```java
// 根据 UpdateWrapper 条件，更新记录 需要设置sqlset
boolean update(Wrapper<T> updateWrapper);
// 根据 whereEntity 条件，更新记录
boolean update(T entity, Wrapper<T> updateWrapper);
// 根据 ID 选择修改
boolean updateById(T entity);
// 根据ID 批量更新
boolean updateBatchById(Collection<T> entityList);
// 根据ID 批量更新
boolean updateBatchById(Collection<T> entityList, int batchSize);
```

##### 参数说明

|     类型      |    参数名     |               描述               |
| :-----------: | :-----------: | :------------------------------: |
|  Wrapper<T>   | updateWrapper | 实体对象封装操作类 UpdateWrapper |
|       T       |    entity     |             实体对象             |
| Collection<T> |  entityList   |           实体对象集合           |
|      int      |   batchSize   |           更新批次数量           |

#### 3.1.7Get

```java
// 根据 ID 查询
T getById(Serializable id);
// 根据 Wrapper，查询一条记录。结果集，如果是多个会抛出异常，随机取一条加上限制条件 wrapper.last("LIMIT 1")
T getOne(Wrapper<T> queryWrapper);
// 根据 Wrapper，查询一条记录
T getOne(Wrapper<T> queryWrapper, boolean throwEx);
// 根据 Wrapper，查询一条记录
Map<String, Object> getMap(Wrapper<T> queryWrapper);
// 根据 Wrapper，查询一条记录
<V> V getObj(Wrapper<T> queryWrapper, Function<? super Object, V> mapper);
```

##### 参数说明

|            类型             |    参数名    |              描述               |
| :-------------------------: | :----------: | :-----------------------------: |
|        Serializable         |      id      |             主键ID              |
|         Wrapper<T>          | queryWrapper | 实体对象封装操作类 QueryWrapper |
|           boolean           |   throwEx    |   有多个 result 是否抛出异常    |
|              T              |    entity    |            实体对象             |
| Function<? super Object, V> |    mapper    |            转换函数             |

#### 3.1.8List

```java
// 查询所有
List<T> list();
// 查询列表
List<T> list(Wrapper<T> queryWrapper);
// 查询（根据ID 批量查询）
Collection<T> listByIds(Collection<? extends Serializable> idList);
// 查询（根据 columnMap 条件）
Collection<T> listByMap(Map<String, Object> columnMap);
// 查询所有列表
List<Map<String, Object>> listMaps();
// 查询列表
List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper);
// 查询全部记录
List<Object> listObjs();
// 查询全部记录
<V> List<V> listObjs(Function<? super Object, V> mapper);
// 根据 Wrapper 条件，查询全部记录
List<Object> listObjs(Wrapper<T> queryWrapper);
// 根据 Wrapper 条件，查询全部记录
<V> List<V> listObjs(Wrapper<T> queryWrapper, Function<? super Object, V> mapper);
```

##### 参数说明

|                类型                |    参数名    |              描述               |
| :--------------------------------: | :----------: | :-----------------------------: |
|             Wrapper<T>             | queryWrapper | 实体对象封装操作类 QueryWrapper |
| Collection<? extends Serializable> |    idList    |           主键ID列表            |
|        Map<?String, Object>        |  columnMap   |         表字段 map 对象         |
|    Function<? super Object, V>     |    mapper    |            转换函数             |

#### 3.1.9Page

```java
// 无条件分页查询
IPage<T> page(IPage<T> page);
// 条件分页查询
IPage<T> page(IPage<T> page, Wrapper<T> queryWrapper);
// 无条件分页查询
IPage<Map<String, Object>> pageMaps(IPage<T> page);
// 条件分页查询
IPage<Map<String, Object>> pageMaps(IPage<T> page, Wrapper<T> queryWrapper);
```

##### 参数说明

|    类型    |    参数名    |              描述               |
| :--------: | :----------: | :-----------------------------: |
|  IPage<T>  |     page     |            翻页对象             |
| Wrapper<T> | queryWrapper | 实体对象封装操作类 QueryWrapper |

#### 3.1.10Count

```java
// 查询总记录数
int count();
// 根据 Wrapper 条件，查询总记录数
int count(Wrapper<T> queryWrapper);
```

##### 参数说明

|    类型    |    参数名    |              描述               |
| :--------: | :----------: | :-----------------------------: |
| Wrapper<T> | queryWrapper | 实体对象封装操作类 QueryWrapper |

#### 3.1.11Chain

#### query

```java
// 链式查询 普通
QueryChainWrapper<T> query();
// 链式查询 lambda 式。注意：不支持 Kotlin
LambdaQueryChainWrapper<T> lambdaQuery(); 

// 示例：
query().eq("column", value).one();
lambdaQuery().eq(Entity::getId, value).list();
```

#### update

```java
// 链式更改 普通
UpdateChainWrapper<T> update();
// 链式更改 lambda 式。注意：不支持 Kotlin 
LambdaUpdateChainWrapper<T> lambdaUpdate();

// 示例：
update().eq("column", value).remove();
lambdaUpdate().eq(Entity::getId, value).update(entity);
```

### 3.2自定义一个继承于IService.java接口的AccountService.java接口，然后实现它

```java

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

```

```java
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


```

### 3.3开始测试

```java
package com.moyisuiying.booksystem;

import com.moyisuiying.booksystem.entity.Account;
import com.moyisuiying.booksystem.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

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
        Account account = new Account(1,"陌意随影","1234");
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
}

```

由于AccountService中的方法除了自定义的方法外和IService中的方法一样，所以我们不用写基本的crud代码实现mybatisplus已经 帮我们实现了。并且在AccountService中我们还可以实现更加强大的更新和查找功能。

### 3.4Wrapper的使用

#### 3.4.1Wrapper的类图体系

![img](E:\新技术学习\java_study\mybatis-plus学习\images\1470521-20200408175328100-117288012.png)

#### 3.4.2wapper介绍 

- Wrapper ： 条件构造抽象类，最顶端父类，抽象类中提供4个方法西面贴源码展示

- AbstractWrapper ： 用于查询条件封装，生成 sql 的 where 条件

- AbstractLambdaWrapper ： Lambda 语法使用 Wrapper统一处理解析 lambda 获取 column。

- LambdaQueryWrapper ：看名称也能明白就是用于Lambda语法使用的查询Wrapper

- LambdaUpdateWrapper ： Lambda 更新封装Wrapper

- QueryWrapper ： Entity 对象封装操作类，不是用lambda语法

- UpdateWrapper ： Update 条件封装，用于Entity对象更新操作

  

> QueryWrapper(LambdaQueryWrapper) 和 UpdateWrapper(LambdaUpdateWrapper) 的父类
> 用于生成 sql 的 where 条件, entity 属性也用于生成 sql 的 where 条件
> 注意: entity 生成的 where 条件与 使用各个 api 生成的 where 条件**没有任何关联行为**

#### 3.4.3常用方法

![img](E:\新技术学习\java_study\mybatis-plus学习\images\1470521-20200408174347575-1589738937.png)

#### 3.4.4方法详情：https://baomidou.com/guide/wrapper.html#abstractwrapper

#### 3.4.5测试查询

```java
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
```

#### 3.4.5测试更新

```java
@Test
    public void testUpDateByWrapper(){
        Account account = new Account(null,"b","bb");
        UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",19);
        accountService.update(account,updateWrapper);
    }
```

## 4.分页插件

### 4.1config.java中添加以下代码

```java
//Spring boot方式
@Configuration
@MapperScan("com.baomidou.cloud.service.*.mapper*")
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }
}
```

### 4.3分页函数

```
/**
     * 根据 entity 条件，查询全部记录（并翻页）
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
     * @param queryWrapper 实体对象封装操作类（可以为 null），用于作为查询条件，为null时默认查询所有记录。
     */
    <E extends IPage<T>> E selectPage(E page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
```



### 4.2进行测试

```java
  @Test
    public  void testPage(){
        //设置分页的条件，从第2的开始查询5条记录
        Page<Account> page = new Page<>(2,5);
        //分页，默认查询所有的记录
        Page<Account> accountPage = accountDao.selectPage(page, null);
        accountPage.getRecords().forEach(System.out::println);

    }
```



## 5.自定义ID生成器

> 自mybatis-plus3.3.0开始,默认使用雪花算法+UUID(不含中划线)

### 5.1雪花算法

snowflake是Twitter开源的分布式ID生成算法，结果是一个long型的ID。其核心思想是：使用41bit作为
毫秒数，10bit作为机器的ID（5个bit是数据中心，5个bit的机器ID），12bit作为毫秒内的流水号（意味
着每个节点在每毫秒可以产生 4096 个 ID），最后还有一个符号位，永远是0。可以保证几乎全球唯一！

### 5.2配置主键自增

![image-20201125210143458](E:\新技术学习\java_study\mybatis-plus学习\images\image-20201125210143458.png)

### 5.3在数据库中配置主键自增

![image-20201125210355729](E:\新技术学习\java_study\mybatis-plus学习\images\image-20201125210355729.png)

### 5.4IdType的类型

![image-20201125210547295](E:\新技术学习\java_study\mybatis-plus学习\images\image-20201125210547295.png)

```
  AUTO(0), // 数据库id自增
  NONE(1), // 未设置主键
  INPUT(2), // 手动输入
  ID_WORKER(3), // 默认的全局唯一id
  UUID(4), // 全局唯一id uuid
  ID_WORKER_STR(5); //ID_WORKER 字符串表示法
```

### 5.5使用自定义的生成主键

### 方式一：声明为Bean供Spring扫描注入

```java
@Component
public class CustomIdGenerator implements IdentifierGenerator {
    @Override
    public Long nextId(Object entity) {
      	//可以将当前传入的class全类名来作为bizKey,或者提取参数来生成bizKey进行分布式Id调用生成.
      	String bizKey = entity.getClass().getName();
        //根据bizKey调用分布式ID生成
        long id = ....;
      	//返回生成的id值即可.
        return id;
    }
}
```

### 方式二：使用配置类

```java
@Bean
public IdentifierGenerator idGenerator() {
    return new CustomIdGenerator();
}
```

### 方式三：通过MybatisPlusPropertiesCustomizer自定义

```java
@Bean
public MybatisPlusPropertiesCustomizer plusPropertiesCustomizer() {
    return plusProperties -> plusProperties.getGlobalConfig().setIdentifierGenerator(new CustomIdGenerator());
}
```

## 6.逻辑删除

说明:

只对自动注入的sql起效:

- 插入: 不作限制
- 查找: 追加where条件过滤掉已删除数据,且使用 wrapper.entity 生成的where条件会忽略该字段
- 更新: 追加where条件防止更新到已删除数据,且使用 wrapper.entity 生成的where条件会忽略该字段
- 删除: 转变为 更新

例如:

- 删除: `update user set deleted=1 where id = 1 and deleted=0`
- 查找: `select id,name,deleted from user where deleted=0`

字段类型支持说明:

- 支持所有数据类型(推荐使用 `Integer`,`Boolean`,`LocalDateTime`)
- 如果数据库字段使用`datetime`,逻辑未删除值和已删除值支持配置为字符串`null`,另一个值支持配置为函数来获取值如`now()`

附录:

- 逻辑删除是为了方便数据恢复和保护数据本身价值等等的一种方案，但实际就是删除。
- 如果你需要频繁查出来看就不应使用逻辑删除，而是以一个状态去表示。

### 6.1使用方法：

#### 6.2.1步骤1:

####  配置`com.baomidou.mybatisplus.core.config.GlobalConfig$DbConfig`

- 例: application.yml

```yaml
mybatis-plus:
  global-config:
    db-config:
      logic-delete-field: flag  # 全局逻辑删除的实体字段名(since 3.3.0,配置后可以忽略不配置步骤2)
      logic-delete-value: 1 # 逻辑已删除值(默认为 1)
      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)
```

或者 application.properties中

```

# 全局逻辑删除的实体字段名(since 3.3.0,配置后可以忽略不配置步骤2)
mybatis-plus.global-config.db-config.logic-delete-field=deleted
# 逻辑已删除值(默认为 1)
mybatis-plus.global-config.db-config.logic-delete-value=1
# 逻辑未删除值(默认为 0)
mybatis-plus.global-config.db-config.logic-not-delete-value=0
```



### 6.2.2步骤2: 

实体类字段上加上`@TableLogic`注解

```java
@TableLogic
private Integer deleted;
```

### 6.3在springboot中使用

#### 6.3.1在数据库中增加字段   **deleted**

![image-20201125220421261](E:\新技术学习\java_study\mybatis-plus学习\images\image-20201125220421261.png)

#### 6.3.2在实体类中添加注释

![image-20201125220545245](E:\新技术学习\java_study\mybatis-plus学习\images\image-20201125220545245.png)

#### 6.3.3在application.properties中添加配置

```properties
server.port=8080
#数据库连接的用户名
spring.datasource.username=root
#数据库连接的密码
spring.datasource.password=root
#数据库连接的URL
spring.datasource.url=jdbc:mysql://localhost:3306/javaweb?serverTimezone=UTC
#数据库连接的驱动   MySQL8使用com.mysql.cj.jdbc.Driver   ,mysql5使用com.mysql.jdbc.Driver
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
#配置日志
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
# 全局逻辑删除的实体字段名(since 3.3.0,配置后可以忽略不配置步骤2)
mybatis-plus.global-config.db-config.logic-delete-field=deleted
# 逻辑已删除值(默认为 1)
mybatis-plus.global-config.db-config.logic-delete-value=1
# 逻辑未删除值(默认为 0)
mybatis-plus.global-config.db-config.logic-not-delete-value=0
```

#### 6.3.4测试

```
  @Test
    public void testGetOneById(){
        Account byId = accountService.getById(1);
        System.out.println(byId);
    }
```

![image-20201125222918802](E:\新技术学习\java_study\mybatis-plus学习\images\image-20201125222918802.png)

可见我们在进行删除时mybatisplus会自动添加条件转换为更新语句。

## 7.乐观锁

### 7.1OptimisticLockerInnerInterceptor

> 当要更新一条记录的时候，希望这条记录没有被别人更新
> 乐观锁实现方式：
>
> > - 取出记录时，获取当前version
> > - 更新时，带上这个version
> > - 执行更新时， set version = newVersion where version = oldVersion
> > - 如果version不对，就更新失败

### 7.2使用方法

字段上加上`@Version`注解

```java
@Version
private Integer version;
```

说明:

- **支持的数据类型只有:int,Integer,long,Long,Date,Timestamp,LocalDateTime**
- 整数类型下 `newVersion = oldVersion + 1`
- `newVersion` 会回写到 `entity` 中
- 仅支持 `updateById(id)` 与 `update(entity, wrapper)` 方法
- **在 `update(entity, wrapper)` 方法下, `wrapper` 不能复用!!!**

## 8.自动填充功能

### 8.1原理

①实现元对象处理器接口：com.baomidou.mybatisplus.core.handlers.MetaObjectHandler

②注解填充字段 `@TableField(.. fill = FieldFill.INSERT)` 生成器策略部分也可以配置！

```java
public class User {

    // 注意！这里需要标记为填充字段
    @TableField(.. fill = FieldFill.INSERT)
    private String fillField;

    ....
}
```

③自定义实现类 MyMetaObjectHandler

```java
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐使用)
        // 或者
        this.strictUpdateFill(metaObject, "createTime", () -> LocalDateTime.now(), LocalDateTime.class); // 起始版本 3.3.3(推荐)
        // 或者
        this.fillStrategy(metaObject, "createTime", LocalDateTime.now()); // 也可以使用(3.3.0 该方法有bug)
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐)
        // 或者
        this.strictUpdateFill(metaObject, "updateTime", () -> LocalDateTime.now(), LocalDateTime.class); // 起始版本 3.3.3(推荐)
        // 或者
        this.fillStrategy(metaObject, "updateTime", LocalDateTime.now()); // 也可以使用(3.3.0 该方法有bug)
    }
}
```

注意事项：

- 填充原理是直接给`entity`的属性设置值!!!
- 注解则是指定该属性在对应情况下必有值,如果无值则入库会是`null`
- `MetaObjectHandler`提供的默认方法的策略均为:如果属性有值则不覆盖,如果填充值为`null`则不填充
- 字段必须声明`TableField`注解,属性`fill`选择对应策略,该声明告知`Mybatis-Plus`需要预留注入`SQL`字段
- 填充处理器`MyMetaObjectHandler`在 Spring Boot 中需要声明`@Component`或`@Bean`注入
- 要想根据注解`FieldFill.xxx`和`字段名`以及`字段类型`来区分必须使用父类的`strictInsertFill`或者`strictUpdateFill`方法
- 不需要根据任何来区分可以使用父类的`fillStrategy`方法

```java
public enum FieldFill {
    /**
     * 默认不处理
     */
    DEFAULT,
    /**
     * 插入填充字段
     */
    INSERT,
    /**
     * 更新填充字段
     */
    UPDATE,
    /**
     * 插入和更新填充字段
     */
    INSERT_UPDATE
}
```

### 8.2在springboot中实现

#### 8.2.1首先在account表中添加createTime和updateTime字段

![image-20201125232450730](E:\新技术学习\java_study\mybatis-plus学习\images\image-20201125232450730.png)

#### 8.2.2在Account.java中添加字段以及注释

```java
  //插入时自动生成
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    //更新时自动生成
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
```

#### 8.2.3在Component包中新建一个类AccountMetaHandler.java

```java
package com.moyisuiying.booksystem.component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Classname:AccountMetaHandler
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2020-11-25 22:56
 * @Version: 1.0
 **/
@Component
@Slf4j
public class AccountMetaHandler  implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        // 起始版本 3.3.0(推荐使用)
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        // 起始版本 3.3.0(推荐使用)
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}

```

### 8.3测试插入和更新操作

```java
 @Test
    public void testInsert(){
        Account account = new Account();
        account.setName("硝酸钠");
        account.setPassword("ab");
       int fla = accountDao.insert(account);
       System.out.println(fla);

   }
```

```java
@Test
    public void testUpdate(){
        Account account = new Account();
        account.setId(27);
        account.setName("李四");
        account.setPassword("aaaaa");
       int update = accountDao.updateById(account);
       System.out.println(update);
   }
```

经过测试可以发现createTime在插入记录时会自动填充数据库中createTime的值；当update更新操作时updateTime会被自动更新。

## 9.代码生成器

AutoGenerator 是 MyBatis-Plus 的代码生成器，通过 AutoGenerator 可以快速生成 Entity、Mapper、Mapper XML、Service、Controller 等各个模块的代码，极大的提升了开发效率。

特别说明:

> 自定义模板有哪些可用参数？[Github](https://github.com/baomidou/mybatis-plus/blob/3.0/mybatis-plus-generator/src/main/java/com/baomidou/mybatisplus/generator/engine/AbstractTemplateEngine.java) [Gitee](https://gitee.com/baomidou/mybatis-plus/blob/3.0/mybatis-plus-generator/src/main/java/com/baomidou/mybatisplus/generator/engine/AbstractTemplateEngine.java) AbstractTemplateEngine 类中方法 getObjectMap 返回 objectMap 的所有值都可用。