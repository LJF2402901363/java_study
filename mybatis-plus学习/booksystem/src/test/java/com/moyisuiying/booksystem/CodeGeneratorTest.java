package com.moyisuiying.booksystem;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import java.util.ArrayList;

/**
 * Classname:GenerateCodeTest
 *
 * @description:测试代码生成
 * @author: 陌意随影
 * @Date: 2020-11-25 23:54
 * @Version: 1.0
 **/

//
public class CodeGeneratorTest {

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
//        String projectPath = System.getProperty("user.dir");
//        项目的根路径
        String projectPath = "E:\\新技术学习\\java_study\\mybatis-plus学习\\booksystemWeb";
//        设置输出目录
        gc.setOutputDir(projectPath + "/src/main/java");
//        设置作者
        gc.setAuthor("陌意随影");
//        设置是否打开
        gc.setOpen(false);
        //第二次生成的把第一次覆盖掉
        gc.setFileOverride(true);
        //mapper 命名方式,默认值：null 例如：%sDao 生成 UserDao
        gc.setMapperName("%sMapper");
        //生成的service接口名字首字母是否为I，这样设置就没有I
        gc.setServiceName("%sService");
        //service impl 命名方式，默认值：null 例如：%sBusinessImpl 生成 UserBusinessImpl
        gc.setServiceImplName(null);

        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        gc.setSwagger2(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
//        设置数据的URL
        dsc.setUrl("jdbc:mysql://localhost:3306/javaweb?serverTimezone=UTC&useUnicode=true&useSSL=false&characterEncoding=utf8");
        // dsc.setSchemaName("public");
//        设置数据库的驱动
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
//        设置数据库连接的用户名
        dsc.setUsername("root");
//        设置数据库连接的用密码
        dsc.setPassword("root");
//        设置数据库类型
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
//        设置项目模块名
        pc.setModuleName("booksystem");
//        设置父类名
        pc.setParent("com.moyisuiying");
//        设置实体类包名
        pc.setEntity("entity");
//        设置控制器包名
        pc.setController("controller");
//        设置业务逻辑包名
        pc.setService("service");
//        设置mapper的包名
        pc.setMapper("mapper");
        mpg.setPackageInfo(pc);


        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
//        设置表名到实体类间的映射规则，表AccountPart 到实体类的 表AccountPart是一致的，没还有发生变化
        strategy.setNaming(NamingStrategy.no_change);
//        设置表的列属性到实体类属性名的映射规则，这里设置了没有改变，比如 表中的 nickName 到实体类的 nickName是一致的，没还有发生变化
        strategy.setColumnNaming(NamingStrategy.no_change);
//        strategy.setSuperEntityClass();
//        给实体类设置自动lombok
        strategy.setEntityLombokModel(true);
//        生成@RestController控制器
        strategy.setRestControllerStyle(true);
        // 公共父类
//        strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
        strategy.setSuperEntityColumns("id");
//        设置要映射的数据库表名
        strategy.setInclude("account","book","borrowbook");
//        设置逻辑删除的字段名称
        strategy.setLogicDeleteFieldName("deleted");
//        设置乐观锁
        strategy.setVersionFieldName("version");
        strategy.setControllerMappingHyphenStyle(true);
        // 自动填充配置
        TableFill gmtCreate = new TableFill("createTime", FieldFill.INSERT);
        TableFill gmtModified = new TableFill("updateTime",FieldFill.INSERT_UPDATE);
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(gmtCreate);
        tableFills.add(gmtModified);
        strategy.setTableFillList(tableFills);
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        //执行生成代码
        mpg.execute();
    }

}