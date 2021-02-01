# JWT基本原理及Springboot集成

## 1.JWT（Json Web Token）简介

>  Json web token (JWT), 是为了在网络应用环境间传递声明而执行的一种基于JSON的开放标准（[(RFC 7519](https://link.jianshu.com?t=https://tools.ietf.org/html/rfc7519)).该token被设计为紧凑且安全的，特别适用于分布式站点的单点登录（SSO）场景。JWT的声明一般被用来在身份提供者和服务提供者间传递被认证的用户身份信息，以便于从资源服务器获取资源，也可以增加一些额外的其它业务逻辑所必须的声明信息，该token也可直接被用于认证，也可被加密。

说起JWT，我们应该来谈一谈基于token的认证和传统的session认证的区别。

## 2.传统的session认证

### 2.1session认证原理

![image-20210131220005825](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20210131220005825.png)

我们知道，http协议本身是一种无状态的协议，而这就意味着如果用户向我们的应用提供了用户名和密码来进行用户认证，那么下一次请求时，用户还要再一次进行用户认证才行，因为根据http协议，我们并不能知道是哪个用户发出的请求，所以为了让我们的应用能识别是哪个用户发出的请求，我们只能在服务器存储一份用户登录的信息，这份登录信息会在响应时传递给浏览器，告诉其保存为cookie,以便下次请求时发送给我们的应用，这样我们的应用就能识别请求来自哪个用户了,这就是传统的基于session认证。

但是这种基于session的认证使应用本身很难得到扩展，随着不同客户端用户的增加，独立的服务器已无法承载更多的用户，而这时候基于session认证应用的问题就会暴露出来.

### 2.2基于session认证所显露的问题

#### 2.2.1session消耗内存大

每个用户经过我们的应用认证之后，我们的应用都要在服务端做一次记录，以方便用户下次请求的鉴别，通常而言session都是保存在内存中，而随着认证用户的增多，服务端的开销会明显增大。

#### 2.2.2扩展性不高

 用户认证之后，服务端做认证记录，如果认证的记录被保存在内存中的话，这意味着用户下次请求还必须要请求在这台服务器上,这样才能拿到授权的资源，这样在分布式的应用上，相应的限制了负载均衡器的能力。这也意味着限制了应用的扩展能力。

#### 2.2.3容易受到CSRF攻击

因为是基于cookie来进行用户识别的, cookie如果被截获，用户就会很容易受到跨站请求伪造的攻击。

## 3.基于token的鉴权机制

基于token的鉴权机制类似于http协议也是无状态的，它不需要在服务端去保留用户的认证信息或者会话信息。这就意味着基于token认证机制的应用不需要去考虑用户在哪一台服务器登录了，这就为应用的扩展提供了便利。

### 3.1验证流程

- 用户使用用户名密码来请求服务器
- 服务器进行验证用户的信息
- 服务器通过验证发送给用户一个token
- 客户端存储token，并在每次请求时附送上这个token值
- 服务端验证token值，并返回数据

![image-20210131220358681](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20210131220358681.png)

这个token必须要在每次请求时传递给服务端，它应该保存在请求头里， 另外，服务端要支持`CORS(跨来源资源共享)`策略，一般我们在服务端这么做就可以了`Access-Control-Allow-Origin: *`。

## 4.JWT的构成

JWT组成格式类似：xxxx.xxxx.xxxx的字符串，这里JWT的官网(https://jwt.io/)给出了JWT生成与验证的工具。

![image-20210131220743650](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20210131220743650.png)

JWT主要由三个部分组成：头部(HEADER)，载荷(PAYLOAD)，签证(SIGNATURE)。

### 4.1HEADER

jwt的头部承载两部分信息：

①声明类型，这里是jwt

②声明加密的算法 通常直接使用 HMAC SHA256

完整的头部就像下面这样的JSON：

```bash
{
  'typ': 'JWT',
  'alg': 'HS256'
}
```

然后将头部进行base64加密（该加密是可以对称解密的),构成了第一部分.

```undefined
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9
```

### 4.2PAYLOAD

载荷就是存放有效信息的地方。这个名字像是特指飞机上承载的货品，这些有效信息包含三个部分

①标准中注册的声明

②公共的声明

③私有的声明

#### 4.2.1**标准中注册的声明** (建议但不强制使用) ：

- **iss**: jwt签发者
- **sub**: jwt所面向的用户
- **aud**: 接收jwt的一方
- **exp**: jwt的过期时间，这个过期时间必须要大于签发时间
- **nbf**: 定义在什么时间之前，该jwt都是不可用的.
- **iat**: jwt的签发时间
- **jti**: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。

#### 4.2.2**公共的声明** ：

 公共的声明可以添加任何的信息，一般添加用户的相关信息或其他业务需要的必要信息.但不建议添加敏感信息，因为该部分在客户端可解密.

#### 4.2.3**私有的声明** ：

 私有声明是提供者和消费者所共同定义的声明，一般不建议存放敏感信息，因为base64是对称解密的，意味着该部分信息可以归类为明文信息。

#### 4.2.4定义一个payload:

```json
{
  "sub": "1234567890",
  "name": "John Doe",
  "iat": 1516239022
}
```

然后将其进行base64加密，得到Jwt的第二部分。

```undefined
eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ
```

### 4.3SIGNATURE

jwt的第三部分是一个签证信息，这个签证信息由三部分组成：

①header (base64后的)

②payload (base64后的)

③secret

这个部分需要base64加密后的header和base64加密后的payload使用`.`连接组成的字符串，然后通过header中声明的加密方式进行加盐`secret`组合加密，然后就构成了jwt的第三部分。

```
OLX0PAgWEmAE5Jc9UWi1xHLGb7alyV9tH0d0MQtjEVM
```

![image-20210131222021373](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20210131222021373.png)

使用jwt官网的验证工具生成一个token：

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.OLX0PAgWEmAE5Jc9UWi1xHLGb7alyV9tH0d0MQtjEVM
```

**注意：secret是保存在服务器端的，jwt的签发生成也是在服务器端的，secret就是用来进行jwt的签发和jwt的验证，所以，它就是你服务端的私钥，在任何场景都不应该流露出去。一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。**

## 5.应用

一般是在请求头里加入`Authorization`，并加上`Bearer`标注：

```bash
fetch('api/user/1', {
  headers: {
    'Authorization': 'Bearer ' + token
  }
})
```

服务端会验证token，如果验证通过就会返回相应的资源。整个流程就是这样的:

![image-20210131222944276](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20210131222944276.png)



## 6.JWT的有点和安全

### 6.1优点

①因为json的通用性，所以JWT是可以进行跨语言支持的，像JAVA,JavaScript,NodeJS,PHP等很多语言都可以使用。

②因为有了payload部分，所以JWT可以在自身存储一些其他业务逻辑所必要的非敏感信息。

③便于传输，jwt的构成非常简单，字节占用很小，所以它是非常便于传输的。

④它不需要在服务端保存会话信息, 所以它易于应用的扩展

### 6.2安全相关

①不应该在jwt的payload部分存放敏感信息，因为该部分是客户端可解密的部分。

②保护好secret私钥，该私钥非常重要。

③如果可以，请使用https协议



## 7.Springboot中集成jwt

### 7.1在pom.xml中添加jwt依赖和配置application.properties

书写pom.xml文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.4.0</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.moyisuiying</groupId>
    <artifactId>jwt</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>jwt</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>11</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
<!--        MySQL8依赖-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <!--阿里数据库连接池 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.2.4</version>
        </dependency>
        <!--mybatis-->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.4</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
<!--     引入jwt依赖   -->
        <dependency>
            <groupId>com.auth0</groupId>
            <artifactId>java-jwt</artifactId>
            <version>3.4.0</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>

```

书写配置文件application.properties：

```properties
#项目启动端口
server.port=8080
#配置MySQL数据库
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.url=jdbc:mysql://localhost:3306/jwt?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
#配置mybatis
logging.level.com.moyisuiying.jwt = debug
#配置mapper的映射xml文件 
mybatis.mapper-locations=classpath*:com.moyisuiying.jwt.mapper/*.xml
#给实体类包的每个类其别名
mybatis.type-aliases-package=com.moyisuiying.jwt.entity
#指定日志打印实现类
mybatis.configuration.log-impl=org.apache.ibatis.logging.slf4j.Slf4jImpl
#token的配置
#header中token的名字
token.header=token
#token的秘钥
token.secret=123
#token的有效时间，以天为单位，默认为1天
token.expireTime=1
```



### 7.2建立MySQL的用户User表

```sql
 create database if not exists jwt ;
 use jwt;
 create table  if not exists user(id int(8) primary key auto_increment,name varchar(16) not null unique,password varchar(16) not null);
  insert into user(name,password) values("a","a");
  insert into user(name,password) values("b","b");
  insert into user(name,password) values("c","c");
```

### 7.3创建实体类User.java和LoginUser.java

```
package com.moyisuiying.jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classname:User
 *
 * @description:实体类USer
 * @author: 陌意随影
 * @Date: 2021-01-31 23:10
 * @Version: 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String name;
    private String password;
}

```

```java
package com.moyisuiying.jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Classname:LoginUser
 *
 * @description: 登录用户
 * @author: 陌意随影
 * @Date: 2021-02-01 11:14
 * @Version: 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**用户信息*/
    private User user;
}

```

### 7.4创建UserMapper.java和对应的 UserMapper.xml文件

```java
package com.moyisuiying.jwt.mapper;

import com.moyisuiying.jwt.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Classname:jwt
 *
 * @description: 用户的mapper
 * @author: 陌意随影
 * @Date: 2021-01-31 23:18
 */
@Mapper
public interface UserMapper {
    /**
     * @Description :通过用户名和密码获取登录的User
     * @Date 23:25 2021/1/31 0031
     * @Param * @param name 用户名
     * @param password ：用户密码
     * @return com.moyisuiying.jwt.entity.User
     **/
    public User login(@Param("name") String name, @Param("password") String password);
}

```

对应的 UserMapper.xml文件：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.moyisuiying.jwt.mapper.UserMapper">
    <select id="login" resultType="user">
        select id,name,password from user where name=#{name} and password = #{password}
    </select>
</mapper>
```



### 7.5创建UserService和UserServiceImpl

```java
package com.moyisuiying.jwt.service;

import com.moyisuiying.jwt.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * Classname:UserService
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-01-31 23:23
 * @Version: 1.0
 **/
public interface UserService {
    /**
     * @Description :通过用户名和密码获取登录的User
     * @Date 23:25 2021/1/31 0031
     * @Param * @param name 用户名
     * @param password ：用户密码
     * @return java.util.Map<java.lang.String,java.lang.String>
     **/
    public Map<String,Object> login(@Param("name") String name, @Param("password") String password);
}

```

```java
package com.moyisuiying.jwt.service.impl;

import com.moyisuiying.jwt.entity.LoginUser;
import com.moyisuiying.jwt.entity.User;
import com.moyisuiying.jwt.mapper.UserMapper;
import com.moyisuiying.jwt.service.UserService;
import com.moyisuiying.jwt.uitl.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * Classname:UserServiceImpl
 *
 * @description: 用户的业务逻辑
 * @author: 陌意随影
 * @Date: 2021-01-31 23:26
 * @Version: 1.0
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    JwtUtil jwtUtil;
    @Override
    public Map<String,Object> login(String name, String password) {
        //响应数据的map
        Map<String,Object> resultMap = new HashMap<>();
        //从数据库 中获取登陆用户
        User login = userMapper.login(name,password);
        if (login == null){
            resultMap.put("status","0");
            resultMap.put("msg","该账号尚未注册！");
        }else {
            //生成token的map
            Map<String,String> tokenMap = new HashMap<>();
            tokenMap.put("name",login.getName());
            tokenMap.put("id",String.valueOf(login.getId()));
            //生成token
            String token = jwtUtil.createToken(tokenMap);
            //设置loginUser对象
            LoginUser loginUser = JwtUtil.buildLoginUser(login, token);
            resultMap.put("status","1");
            resultMap.put("msg","登录成功");
            resultMap.put("loginUser",loginUser);
            log.info("用户名:[{}]",name);
            log.info("用户密码[{}]",password);

        }
        return resultMap;
    }
}

```

### 7.6创建UserController.java

```java
package com.moyisuiying.jwt.controller;

import com.moyisuiying.jwt.entity.User;
import com.moyisuiying.jwt.service.UserService;
import com.moyisuiying.jwt.uitl.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Classname:UserController
 *
 * @description: 用户User的控制器
 * @author: 陌意随影
 * @Date: 2021-01-31 23:29
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
     /**
      * @Description :用户登录
      * @Date 16:39 2021/2/1 0001
      * @Param * @param name  用户名
      * @param password ：用户密码
      * @return java.util.Map<java.lang.String,java.lang.Object>
      **/
    @PostMapping("/login")
    public Map<String,Object> login(@RequestParam("name")String name, @RequestParam("password") String password){
        Map<String,Object> map = userService.login(name, password);
        return map;
    }
    @GetMapping("/test")
    public String test(String str){
        return "测试验证成功";
    }
}

```

### 7.7创建JWT的工具类JwtUtil.java



```java
package com.moyisuiying.jwt.uitl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.moyisuiying.jwt.entity.LoginUser;
import com.moyisuiying.jwt.entity.User;
import lombok.Data;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Classname:JWTUtil
 *
 * @description: jWT的工具类
 * @author: 陌意随影
 * @Date: 2021-01-31 23:32
 * @Version: 1.0
 **/
@Slf4j
@Component
@Data
public class JwtUtil {
    // 令牌自定义标识
    @Value("${token.header}")
    private String header;
    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌有效期（默认1）
    @Value("${token.expireTime}")
    public static  Integer expireTime;
    /**
     * 默认过期时间1天
     */
    public static final Integer DEFAULT_EXPIRETIME = 1;

    /**
     * 生成JwtToken
     * @param playloaMap  封装有用户书籍的map
     */
    public String createToken(Map<String, String> playloaMap) {
        if (playloaMap == null || playloaMap.size() == 0) {
            return null;
        }
        // 过期时间
        Calendar ca = Calendar.getInstance();
        if (expireTime == null || expireTime <= 0) {
            expireTime = DEFAULT_EXPIRETIME;
        }
        //设置token有效日期
        ca.add(Calendar.DATE, expireTime);
        // 创建JwtToken对象
        JWTCreator.Builder builder = JWT.create();
        playloaMap.forEach((k, v) -> {
            builder.withClaim(k, v);
        });
        // 发布时间
        builder.withIssuedAt(new Date());
        // 过期时间
        builder.withExpiresAt(ca.getTime());
        // 签名加密
        String  token = builder.sign(Algorithm.HMAC256(secret));
        return token;
    }
    /**
     * @Description :获取token数据声明中keyName对应的value值
     * @Date 16:55 2021/2/1 0001
     * @Param * @param keyName   数据生命的key
     * @param token 已有的token
     * @return String
     **/
  public String  getTokenClaimByName(String keyName,String token){
      DecodedJWT decode = JWT.decode(token);
      return decode.getClaim(keyName).asString();
  }
    /**
     * 验证JwtToken
     * @param token JwtToken数据
     * @return true 验证通过
     */
    public void verifyToken(String token) {
        JWTVerifier build = JWT.require(Algorithm.HMAC256(secret)).build();
        build.verify(token);
    }
    /**
     * @Description :通过User和token构建一个loginUser
     * @Date 16:04 2021/2/1 0001
     * @Param * @param user  需要登录的用户User对象
     * @param token ：token值
     * @return com.moyisuiying.jwt.entity.LoginUser
     **/
   public static LoginUser buildLoginUser(User user,String token){
        //将天数转化为毫秒  24 小时 * 60 分钟 * 60 秒 * 1000 毫秒 = 1 天
        Long expireTimeMillis = expireTime * 24 * 60 * 60 *1000L;
       LoginUser loginUser = new LoginUser();
       loginUser.setToken(token);
       loginUser.setExpireTime(expireTimeMillis);
       loginUser.setLoginTime(System.currentTimeMillis());
       loginUser.setUser(user);
       return loginUser;
   }

}


```

### 7.8创建拦截器 

```java
package com.moyisuiying.jwt.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyisuiying.jwt.uitl.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Classname:Jwtinterceptor
 *
 * @description:  JWT的拦截器用于拦截没有token的请求
 * @author: 陌意随影
 * @Date: 2021-02-01 12:45
 * @Version: 1.0
 **/
@Slf4j
@Component
public class JwtInterceptor  implements HandlerInterceptor {
    @Autowired
    JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取token值
        String token = request.getHeader(jwtUtil.getHeader());
        Map<String, String> map = new HashMap<>();
        try {
            //验证token是否正确
            jwtUtil.verifyToken(token);
          return  true;
        }catch (SignatureGenerationException signatureGenerationException){
            log.info("签名无效");
            map.put("status","0");
            map.put("msg",signatureGenerationException.getMessage());
        }catch (TokenExpiredException tokenExpiredException){
            log.info("token已过期");
            map.put("status","0");
            map.put("msg",tokenExpiredException.getMessage());
        }catch (AlgorithmMismatchException algorithmMismatchException){
            log.info("加密方法无效");
            map.put("status","0");
            map.put("msg",algorithmMismatchException.getMessage());
        }catch (Exception e){
            log.info("token无效");
            map.put("status","0");
            map.put("msg",e.getMessage());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(map);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
        response.getWriter().flush();
        return false;
    }
}

```

### 7.9创建一个WebSecurityConfig.java注册拦截器

```java
package com.moyisuiying.jwt.config;

import com.moyisuiying.jwt.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Classname:WebSecurityConfig
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-02-01 12:49
 * @Version: 1.0
 **/
@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {
    @Autowired
    JwtInterceptor jwtInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注意这里不要使用 new JwtInterceptor() ，否则就会出现拦截器JwtInterceptor里无法自动注入JwtUtil的问题
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**").excludePathPatterns("/static/**","/user/login");
    }
}

```

注意，这里的拦截器JwtInterceptor要使用容器自动注入的，因为JwtInterceptor中使用到了JwtUtil也是自动注入的，所以这个拦截器jwtInterceptor需要交给springboot的IOC容器管理。

## 7.10使用postman测试

#### 7.10.1首先测试 http://localhost:8080/user/test

首次测试时候发送一个字符串 str  = aa

header中尚未有token这个字段

![image-20210201172643430](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20210201172643430.png)

![image-20210201172607659](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20210201172607659.png)



响应的结果：

![image-20210201172809891](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20210201172809891.png)





可见无法正常访问。

#### 7.10.2测试登录 ：http://localhost:8080/user/login?name=a&password=a

登录用户name="a",password="a"的用户

![image-20210201172952080](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20210201172952080.png)

可见登录成功返回了token：

![image-20210201173126044](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20210201173126044.png)



### 7.10.3使用token发送http://localhost:8080/user/test?str=aa请求：

在header中设置 (key,value)=(token,"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiYSIsImlkIjoiMSIsImV4cCI6MTYxMjI1NDg0MiwiaWF0IjoxNjEyMTY4NDQyfQ.ByUnafzJaJYQXxCzM6CHwq1qiaA-OtVeVd8Gy091SRw")



![image-20210201173328223](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20210201173328223.png)

![image-20210201173517830](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20210201173517830.png)

响应成功。说明jwt验证起作用了。

参考文章：

链接：https://www.jianshu.com/p/576dbf44b2ae
