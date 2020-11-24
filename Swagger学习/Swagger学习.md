# Swagger学习

## 1.简介

### 1.1号称世界上最流行的API框架

### 1.2RestFul API文档自动在线生成工具---》API文档与API定义同时更新

### 1.3直接运行，可以在线测试API接口

### 1.4支持多种语言（Java，Python，PHP。。。）

### 1.5什么是Swagger

作为后端程序开发，我们多多少少写过几个后台接口项目，不管是编写手机端接口，还是目前比较火热的前后端分离项目，前端与后端都是由不同的工程师进行开发，那么这之间的沟通交流通过接口文档进行连接。但往往伴随很多问题，后端程序员认为编写接口文档及维护太花费时间精力，前端的认为接口文档变动更新不及时，导致程序之间相互调用出行问题。那么能简化接口文档的编写直接自动生成吗？当然能！如是乎Swagger这种接口文档在线自动生成工具便孕育而生。

Swagger 是一个规范和完整的框架，用于生成、描述、调用和可视化 RESTful 风格的 Web 服务。总体目标是使客户端和文件系统作为服务器以同样的速度来更新。文件的方法，参数和模型紧密集成到服务器端的代码，允许API来始终保持同步。Swagger 让部署管理和使用功能强大的API从未如此简单。

### 1.6Swagger优点

- 代码变，文档变。只需要少量的注解，Swagger 就可以根据代码自动生成 API 文档，很好的保证了文档的时效性。
- 跨语言性，支持 40 多种语言。
- Swagger UI 呈现出来的是一份可交互式的 API 文档，我们可以直接在文档页面尝试 API 的调用，省去了准备复杂的调用参数的过程。
- 还可以将文档规范导入相关的工具（例如 Postman、SoapUI）, 这些工具将会为我们自动地创建自动化测试。

## 2.在项目中使用Swagger

### 2.1Swagger2

### 2.2Swagger-UI

### 2.3需要springFox

## 3.springboot 集成Swagger

只需在pom.xml中引入依赖：

```
 <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-boot-starter</artifactId>
            <version>3.0.0</version>
</dependency>
```



### 3.1创建一个springboot项目

![image-20201122133250326](E:\Swagger学习\image\9.png)

### 3.2在pom.xml文件中加入依赖

~~~xml
 <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-boot-starter</artifactId>
            <version>3.0.0</version>
</dependency>
~~~



###  3.3新建一个config包，然后在这个包下新建一个SwaggerConfig.java用于配置Swagger



~~~xml
/**
 * Classname:SwaggerConfig
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2020-11-22 10:46
 * @Version: 1.0
 **/
@Configuration
@EnableOpenApi
public class SwaggerConfig {
        @Bean
         public Docket createDocket(){
             ApiInfo apiInfo = apiInfo();
          return new Docket(DocumentationType.SWAGGER_2).
                  apiInfo(apiInfo).select().
                  apis(RequestHandlerSelectors.basePackage("com.swagger.controller")).
                  paths(PathSelectors.any()).build();
         }

         public ApiInfo apiInfo() {
             return  new ApiInfoBuilder().title("测试接口").description("这是用于测试前端和后端连接接口").termsOfServiceUrl("http://moyisuiying.com").contact(new Contact("陌意随影","http://moyisuiying.com","2402901363@qq.com")).version("v1.0").build();
         }
}
~~~

### 3.4新建一个controller包，然后在其下新建一个控制类HellowController.java

```
package com.swagger.controller;

import com.swagger.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classname:HellowController
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2020-11-21 22:47
 * @Version: 1.0
 **/
@Controller
@RequestMapping("/user")
@ApiModel("用户控制类")
public class UserController {
    @GetMapping("/hello")
    @ResponseBody
    @ApiOperation("用户hello")
    public String hello(){
        return "hello";
    }
    @GetMapping("/findUser/{name}/{password}")
    @ResponseBody
    @ApiOperation("通过名字和密码查找用户")
    public User findUser(@ApiParam("用户名字") @PathVariable String name, @ApiParam("用户密码")@PathVariable String password){
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        return user;
    }
    @GetMapping("/findAll")
    @ApiOperation("查找所有用户")
    @ResponseBody
    public List<User> findAll(){
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setName("A");
        user.setPassword("A");
        userList.add(user);
        return userList;
    }
    @GetMapping("/getUserJson/{id}")
    @ApiOperation("转换字符串")
    @ResponseBody
    public String userJson(@ApiParam("用户ID")@PathVariable("id") int id){
        User user = new User();
        user.setName("A");
        user.setAge(32);
        user.setPassword("B");
        user.setBirthDay(LocalDate.now());
        return user.toString();
    }
    @PostMapping("/getUserById")
    @ResponseBody
    @ApiOperation("通过ID获取用户")
    public User getUserById(@ApiParam("用户ID")@PathVariable("id") int id){
        User user = new User();
        user.setName("A");
        user.setAge(32);
        user.setPassword("B");
        user.setBirthDay(LocalDate.now());
        return user;
    }
    
    
}

```

### 3.5新建一个entity包，在其下新建一个类User.java

```
package com.swagger.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * Classname:User
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2020-11-22 13:36
 * @Version: 1.0
 **/
@Repository
@Scope("prototype")
@ApiModel("用户类")
public class User {
    @ApiModelProperty("用户名字")
    private String name;
    @ApiModelProperty("用户密码")
    private String password;
    @ApiModelProperty("用户年龄")
    private int age;
    @ApiModelProperty("用户生日")
    private LocalDate birthDay;

    public User() {
    }

    public User(String name, String password, int age, LocalDate localDate) {
        this.name = name;
        this.password = password;
        this.age = age;
        this.birthDay = localDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", localDate=" + birthDay +
                '}';
    }
}

```

### 3.6启动

### 3.7访问：http://localhost:8081/swagger-ui/index.html

![image-20201122211401222](E:\Swagger学习\image\image-20201122211401222.png)

**Swagger3.0版本的地址是http://localhost:8088/swagger-ui/index.html，2.x版本中访问的地址的为http://localhost:8088/swagger-ui.html**

## 4.常用注解

我们也可以在接口上添加注释说明，方便我们在接口文档中解释说明接口的信息，例如接口的作用、参数说明等，方便调用者使用

| Swagger注解                                                | 简单说明                                             |
| :--------------------------------------------------------- | :--------------------------------------------------- |
| [@Api](https://springboot.io/u/api)(tags = “xxx模块说明”)  | 作用在模块类上                                       |
| **@ApiOperation**(“xxx接口说明”)                           | 作用在接口方法上                                     |
| **@ApiModel**(“xxxPOJO说明”)                               | 作用在模型类上：如VO、BO                             |
| **@ApiModelProperty**(value = “xxx属性说明”,hidden = true) | 作用在类方法和属性上，hidden设置为true可以隐藏该属性 |
| **@ApiParam**(“xxx参数说明”)                               | 作用在参数、方法和字段上，类似@ApiModelProperty      |