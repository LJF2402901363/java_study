# Neo4J学习

## 1.入门简介

### 1.1简介

> ​	Neo4j是一个高性能的,NOSQL图形数据库，它将结构化数据存储在网络上而不是表中。它是一个嵌入的、基于磁盘的、具备完全的事务特性的Java持久化引擎，但是它将结构化数据存储在网络(从数学角度叫做图)上而不是表中。Neo4j也可以被看作是一个高性能的图引擎，该引擎具有成熟数据库的所有特性。程序员工作在一个面向对象的、灵活的网络结构下而不是严格、静态的表中——但是他们可以享受到具备完全的事务特性、企业级的数据库的所有好处。
>
> ​	Neo4j因其嵌入式、高性能、轻量级等优势，越来越受到关注.

### 1.2Neo4j的特点

- SQL就像简单的查询语言Neo4j CQL
- 它遵循属性图数据模型
- 它通过使用Apache Lucence支持索引
- 它支持UNIQUE约束
- 它包含一个用于执行CQL命令的UI：Neo4j数据浏览器
- 它支持完整的ACID（原子性，一致性，隔离性和持久性）规则
- 它采用原生图形库与本地GPE（图形处理引擎）
- 它支持查询的数据导出到JSON和XLS格式
- 它提供了REST API，可以被任何编程语言（如Java，Spring，Scala等）访问
- 它提供了可以通过任何UI MVC框架（如Node JS）访问的Java脚本
- 它支持两种Java API：Cypher API和Native Java API来开发Java应用程序

### 1.3Neo4j的优点

- 它很容易表示连接的数据
- 检索/遍历/导航更多的连接数据是非常容易和快速的
- 它非常容易地表示半结构化数据
- Neo4j CQL查询语言命令是人性化的可读格式，非常容易学习
- 使用简单而强大的数据模型
- 它不需要复杂的连接来检索连接的/相关的数据，因为它很容易检索它的相邻节点或关系细节没有连接或索引

### 1.5Neo4j的缺点或限制

- AS的Neo4j 2.1.3最新版本，它具有支持节点数，关系和属性的限制。
- 它不支持Sharding。

## 2.Neo4j属性图数据模型

Neo4j图数据库遵循属性图模型来存储和管理其数据。

属性图模型规则

- 表示节点，关系和属性中的数据
- 节点和关系都包含属性
- 关系连接节点
- 属性是键值对
- 节点用圆圈表示，关系用方向键表示。
- 关系具有方向：单向和双向。
- 每个关系包含“开始节点”或“从节点”和“到节点”或“结束节点”

## 3.neo4j在Ubuntu上Docker的安装

### 3.1安装到Docker上

```bash
docker run --name neo4j -p 7474:7474 -p 7687:7687  -d  -v /neo4j/data:/data    neo4j:latest
```

### 3.2访问：http://121.89.208.222:7474/

![image-20201127234756940](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20201127234756940.png)

### 3.3Neo4j - 构建模块



#### 3.3.1节点

节点是图表的基本单位。 它包含具有键值对的属性，如下所示

![image-20201127234914803](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20201127234914803.png)

这里Node Name =“Employee”，它包含一组属性作为键值对。



#### 3.3.2属性

属性是用于描述图节点和关系的键值对

**Key = 值**

其中Key是一个字符串

值可以通过使用任何Neo4j数据类型来表示

#### 3.3.3关系

关系是图形数据库的另一个主要构建块。 它连接两个节点，如下所示。

![image-20201127235046483](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20201127235046483.png)

这里Emp和Dept是两个不同的节点。 “WORKS_FOR”是Emp和Dept节点之间的关系。因为它表示从Emp到Dept的箭头标记，那么这种关系描述的一样Emp WORKS_FOR Dept每个关系包含一个起始节点和一个结束节点。这里“Emp”是一个起始节点。“Dept”是端节点。由于该关系箭头标记表示从“Emp”节点到“Dept”节点的关系，该关系被称为“进入关系”到“Dept”节点。并且“外向关系”到“Emp”节点。像节点一样，关系也可以包含属性作为键值对。

![image-20201127235121444](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20201127235121444.png)



这里的“WORKS_FOR”关系有一个属性作为键值对ID = 123它代表了这种关系的一个ID。



#### 3.3.4标签

​	Label将一个公共名称与一组节点或关系相关联。 节点或关系可以包含一个或多个标签。 我们可以为现有节点或关系创建新标签。 我们可以从现有节点或关系中删除现有标签。

从前面的图中，我们可以观察到有两个节点。

左侧节点都有一个标签：“EMP”，而右侧节点都有一个标签：“Dept”。

这两个节点之间的关系，也有一个标签：“WORKS_FOR”

**注： -**Neo4j将数据存储在节点或关系的属性中。

#### 3.3.5数据浏览器

一旦我们安装Neo4j，我们可以访问Neo4j数据浏览器使用以下URL

http：// ip：7474 / browser /

![Neo4j数据浏览器](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/201612260916443762.png)

Neo4j数据浏览器用于执行CQL命令并查看输出输出。这里我们需要在美元提示符处执行所有CQL命令：“$”

在美元符号后键入命令，然后单击“执行”按钮运行命令。它与Neo4j数据库服务器交互，检索和显示下面的结果到那个美元提示。使用“UI视图”按钮以图形格式查看结果。 上图以“UI视图”格式显示结果。使用“网格视图”按钮在网格视图中查看结果。 下图在“网格视图”格式中显示相同的结果。

![网格视图](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/201612260918064546.png)

当我们使用“网格视图”查看我们的查询结果时，我们可以将它们导出为两种不同格式的文件。

## 4Neo4j CQL 命令

### 4.1Neo4j使用CQL“CREATE”命令

①创建没有属性的节点

②使用属性创建节点

③在没有属性的节点之间创建关系

④使用属性创建节点之间的关系

⑤为节点或关系创建单个或多个标签

#### 4.1.1Neo4j CQL创建一个没有属性的节点

Neo4j CQL“CREATE”命令用于创建没有属性的节点。 它只是创建一个没有任何数据的节点。

##### CREATE命令语法

```bash
CREATE (<node-name>:<label-name>)
```

语法说明

| 语法元素     | 描述                       |
| ------------ | -------------------------- |
| CREATE       | 它是一个Neo4j CQL命令。    |
| <node-name>  | 它是我们要创建的节点名称。 |
| <label-name> | 它是一个节点标签名称       |

**注意事项 -**

1、Neo4j数据库服务器使用此<node-name>将此节点详细信息存储在Database.As中作为Neo4j DBA或Developer，我们不能使用它来访问节点详细信息。

2、Neo4j数据库服务器创建一个<label-name>作为内部节点名称的别名。作为Neo4j DBA或Developer，我们应该使用此标签名称来访问节点详细信息。

例子：

```bash
CREATE (dept:Dept)
```



#### 4.1.2Neo4j CQL创建具有属性的节点

Neo4j CQL“CREATE”命令用于创建带有属性的节点。 它创建一个具有一些属性（键值对）的节点来存储数据。

##### CREATE命令语法： 

```bash
CREATE (
   <node-name>:<label-name>
   { 	
      <Property1-name>:<Property1-Value>
      ........
      <Propertyn-name>:<Propertyn-Value>
   }
)
```



语法说明：

| 语法元素                              | 描述                                            |
| ------------------------------------- | ----------------------------------------------- |
| <node-name>                           | 它是我们将要创建的节点名称。                    |
| <label-name>                          | 它是一个节点标签名称                            |
| <Property1-name>...<Propertyn-name>   | 属性是键值对。 定义将分配给创建节点的属性的名称 |
| <Property1-value>...<Propertyn-value> | 属性是键值对。 定义将分配给创建节点的属性的值   |

例子：

```bash
CREATE (dept:Dept { deptno:10,dname:"Accounting",location:"Hyderabad" })
```

**这里的属性名称是deptno，dname，location**

**属性值为10，"Accounting","Hyderabad"**

**正如我们讨论的，属性一个名称 - 值对。**

**Property = deptno:10**

**因为deptno是一个整数属性，所以我们没有使用单引号或双引号定义其值10。**

**由于dname和location是String类型属性，因此我们使用单引号或双引号定义其值10。**

**注意 - 要定义字符串类型属性值，我们需要使用单引号或双引号。**

### 4.2Neo4j CQL - MATCH命令

Neo4j CQL MATCH命令用于 - 

①从数据库获取有关节点和属性的数据

②从数据库获取有关节点，关系和属性的数据

##### MATCH命令语法：

```
MATCH 
(
   <node-name>:<label-name>
)
```



语法说明

| 语法元素     | 描述                         |
| ------------ | ---------------------------- |
| <node-name>  | 这是我们要创建一个节点名称。 |
| <label-name> | 这是一个节点的标签名称       |

注意事项 -

- Neo4j数据库服务器使用此<node-name>将此节点详细信息存储在Database.As中作为Neo4j DBA或Developer，我们不能使用它来访问节点详细信息。
- Neo4j数据库服务器创建一个<label-name>作为内部节点名称的别名。作为Neo4j DBA或Developer，我们应该使用此标签名称来访问节点详细信息。

**注意-**我们不能单独使用MATCH Command从数据库检索数据。 如果我们单独使用它，那么我们将InvalidSyntax错误。

#### 例如：

这个例子演示了“如果我们单独使用MATCH命令从数据库检索数据会发生什么”。 按照下面给出的步骤 - 

**步骤1** -打开Neo4j的数据浏览器。

**步骤2** -在数据浏览器的dollar提示符处键入以下命令。

```
MATCH (dept:Dept)
```

这里 -

- dept是节点名称
- Dept是emp节点的标签名称

![image-20201128113056991](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20201128113056991.png)

数据浏览器中看到的是错误的消息。如果你观察到错误消息，它告诉我们，MATCH 经常需要与其他的语句配合才可以使用.

如：match (n) return n



```
# 查询Dept下的内容
MATCH (dept:Dept) return dept
```

![image-20201128113446727](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20201128113446727.png)

### 4.3Neo4j CQL - RETURN子句

Neo4j CQL RETURN子句用于 -

①检索节点的某些属性

②检索节点的所有属性

③检索节点和关联关系的某些属性

④检索节点和关联关系的所有属性

##### RETURN命令语法：

```
RETURN 
   <node-name>.<property1-name>,
   ........
   <node-name>.<propertyn-name>
```



语法说明:

| 语法元素                            | 描述                                                         |
| ----------------------------------- | ------------------------------------------------------------ |
| <node-name>                         | 它是我们将要创建的节点名称。                                 |
| <Property1-name>...<Propertyn-name> | 属性是键值对。 <Property-name>定义要分配给创建节点的属性的名称 |

##### 例如：

本示例演示了“如果我们单独使用RETURN命令从数据库检索数据，会发生什么”。 按照下面给出的步骤 - 

**步骤1** -打开Neo4j的数据浏览器。



**步骤2** -在数据浏览器的dollar提示符处键入以下命令。

```
RETURN dept.deptno
```



这里 -

- dept是节点名称
- deptno是dept节点的属性名称

![image-20201128131614470](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20201128131614470.png)

发现该错误消息，它告诉我们，我们不能单独使用RETURN子句。我们应该既MATCH使用或CREATE命令。

### 4.4Neo4j CQL - MATCH & RETURN匹配和返回

在Neo4j CQL中，我们不能单独使用MATCH或RETURN命令，因此我们应该合并这两个命令以从数据库检索数据。Neo4j使用CQL MATCH + RETURN命令 - 

①检索节点的某些属性

②检索节点的所有属性

③检索节点和关联关系的某些属性

④检索节点和关联关系的所有属性

#### 4.4.1MATCH RETURN命令语法：

```
MATCH Command
RETURN Command
```

语法说明：

| 语法元素   | 描述                       |
| ---------- | -------------------------- |
| MATCH命令  | 这是Neo4j CQL MATCH命令。  |
| RETURN命令 | 这是Neo4j CQL RETURN命令。 |

##  

#### 4.4.2MATCH命令语法：

```
MATCH 
(
   <node-name>:<label-name>
)
```



语法说明：

| 语法元素     | 描述                         |
| ------------ | ---------------------------- |
| <node-name>  | 它是我们将要创建的节点名称。 |
| <label-name> | 它是一个节点标签名称         |



要点 -

- Neo4j数据库服务器使用此<node-name>将此节点详细信息存储在Database.As中作为Neo4j DBA或Developer，我们不能使用它来访问节点详细信息。
- Neo4j数据库服务器创建一个<label-name>作为内部节点名称的别名。作为Neo4j DBA或Developer，我们应该使用此标签名称来访问节点详细信息。

## 

#### 4.4.3RETURN命令语法：

```
RETURN 
   <node-name>.<property1-name>,
   ...
   <node-name>.<propertyn-name>
```



语法说明：

| 语法元素                            | 描述                                            |
| ----------------------------------- | ----------------------------------------------- |
| <node-name>                         | 它是我们将要创建的节点名称。                    |
| <Property1-name>...<Propertyn-name> | 属性是键值对。 定义将分配给创建节点的属性的名称 |



##### 例如：

本示例演示如何从数据库检索Dept节点的一些属性（deptno，dname）数据。**注-**结点包含3个属性：deptno，dname，location。 然而在这个例子中，我们感兴趣的是只查看两个属性数据。 按照下面给出的步骤 - 

**步骤1** -打开Neo4j的数据浏览器。

**步骤2** -在数据浏览器中的dollar提示符下键入以下命令。

```
MATCH (dept: Dept)
RETURN dept.deptNo,dept.dName
```

这里 -

- dept是节点名称
- 这里Dept是一个节点标签名
- deptno是dept节点的属性名称
- dname是dept节点的属性名

![image-20201128132137637](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20201128132137637.png)

## 5.Neo4j图数据库遵循属性图模型来存储和管理其数据。

### 5.1特点

根据属性图模型，关系应该是定向的。 否则，Neo4j将抛出一个错误消息。

#### 5.1.1基于方向性，Neo4j关系被分为两种主要类型。

①单向关系

②双向关系

#### 5.1.2在以下场景中，我们可以使用Neo4j CQL CREATE命令来创建两个节点之间的关系。 这些情况适用于Uni和双向关系。

①在两个现有节点之间创建无属性的关系

②在两个现有节点之间创建与属性的关系

③在两个新节点之间创建无属性的关系

④在两个新节点之间创建与属性的关系

⑤在具有WHERE子句的两个退出节点之间创建/不使用属性的关系

### 5.2Neo4j CQL - CREATE创建标签

#### 5.2.1Label是Neo4j数据库中的节点或关系的名称或标识符。

我们可以将此标签名称称为关系为“关系类型”。我们可以使用CQL CREATE命令为节点或关系创建单个标签，并为节点创建多个标签。 这意味着Neo4j仅支持两个节点之间的单个关系类型。我们可以在UI模式和网格模式下在CQL数据浏览器中观察此节点或关系的标签名称。 并且我们引用它执行CQL命令。到目前为止，我们只创建了一个节点或关系的标签，但我们没有讨论它的语法。

使用Neo4j CQL CREATE命令：

①为节点创建单个标签。

②为节点创建多个标签。

③为关系创建单个标签。

#### 5.2.2单个标签到节点

语法：

```
CREATE (<node-name>:<label-name>)
```

| S.No. | 语法元素               | 描述                      |
| ----- | ---------------------- | ------------------------- |
| 1     | CREATE 创建            | 它是一个Neo4j CQL关键字。 |
| 2     | <node-name> <节点名称> | 它是一个节点的名称。      |
| 3     | <label-name><标签名称> | 这是一个节点的标签名称。  |

##### **注意 -**

- 我们应该使用colon（:)运算符来分隔节点名和标签名。
- Neo4j数据库服务器使用此名称将此节点详细信息存储在Database.As Neo4j DBA或Developer中，我们不能使用它来访问节点详细信息
- Neo4j数据库服务器创建一个标签名称作为内部节点名称的别名。作为Neo4j DBA或开发人员，我们应该使用此标签名称来访问节点详细信息。

##### 例子：

```
CREATE (google1:GooglePlusProfile)
```

![image-20201128140154729](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20201128140154729.png)

#### 5.2.3多个标签到节点

语法：

```
CREATE (<node-name>:<label-name1>:<label-name2>.....:<label-namen>)
```

| S.No. | 语法元素                                         | 描述                           |
| ----- | ------------------------------------------------ | ------------------------------ |
| 1。   | CREATE 创建                                      | 这是一个Neo4j CQL关键字。      |
| 2。   | <node-name> <节点名称>                           | 它是一个节点的名称。           |
| 3。   | <label-name1>,<label-name2> <标签名1>，<标签名2> | 它是一个节点的标签名称的列表。 |

**注意 -**

- 我们应该使用colon（:)运算符来分隔节点名和标签名。
- 我们应该使用colon（:)运算符将一个标签名称分隔到另一个标签名称。

##### 例如：

本示例演示如何为“Cinema”节点创建多个标签名称。

我们的客户提供的多个标签名称：Cinema,Film,Movie,Picture。

```
CREATE (m:Movie:Cinema:Film:Picture)
```

这里m是一个节点名

Movie, Cinema, Film, Picture是m节点的多个标签名称

![image-20201128140416825](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20201128140416825.png)

#### 5.2.4单个标签到关系 

##### 语法：

```bash
CREATE (<node1-name>:<label1-name>)-
	[(<relationship-name>:<relationship-label-name>)]
	->(<node2-name>:<label2-name>)
```

##  

##### 语法说明

| S.No. | 语法元素                                 | 描述                      |
| ----- | ---------------------------------------- | ------------------------- |
| 1     | CREATE 创建                              | 它是一个Neo4J CQL关键字。 |
| 2     | <node1-name> <节点1名>                   | 它是From节点的名称。      |
| 3     | <node2-name> <节点2名>                   | 它是To节点的名称。        |
| 4     | <label1-name> <LABEL1名称>               | 它是From节点的标签名称    |
| 5     | <label2-name> <LABEL2名称>               | 它是To节点的标签名称。    |
| 6     | <relationship-name> <关系名称>           | 它是一个关系的名称。      |
| 7     | <relationship-label-name> <相关标签名称> | 它是一个关系的标签名称。  |

**注意 -**

- 我我们应该使用colon（:)运算符来分隔节点名和标签名。
- 我们应该使用colon（:)运算符来分隔关系名称和关系标签名称。
- 我们应该使用colon（:)运算符将一个标签名称分隔到另一个标签名称。
- Neo4J数据库服务器使用此名称将此节点详细信息存储在Database.As中作为Neo4J DBA或开发人员，我们不能使用它来访问节点详细信息。
- Neo4J Database Server创建一个标签名称作为内部节点名称的别名。作为Neo4J DBA或Developer，我们应该使用此标签名称来访问节点详细信息。

##### 例如：

```
CREATE (p1:Profile1)-[r1:LIKES]->(p2:Profile2)
```

这里p1和profile1是节点名称和节点标签名称“From Node”

p2和Profile2是“To Node”的节点名称和节点标签名称

r1是关系名称

LIKES是一个关系标签名称

![image-20201128140602570](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/image-20201128140602570.png)

## 6.Neo4j Java架构

### 6.1下图显示了Neo4j JAVA API应用程序的体系结构

![Neo4j JAVA API](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/201701021529515240.jpg)

客户端程序使用Neo4j Java API之一来在Neo4j数据库上交互和执行数据库操作。

### 6.2在springboot中集成neo4j

Spring Boot承担了应用程序配置和引导的大部分责任，因此我们选择在本指南的项目中利用该帮助。[Spring Data Neo4j](https://github.com/neo4j-examples/spring-data-neo4j-intro-app)的示例项目代码在GitHub中。您可以克隆存储库并与本指南一起运行代码，也可以从[Spring Initializr](http://start.spring.io/)页面从头开始构建项目。

我们将使用您可能已经熟悉的电影领域-人员（演员，导演等）和这些人员所涉及的电影。

#### 6.2.1首先在pom.xml文件中添加依赖文件

```properties
<!--        导入springboot的neo4j依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-neo4j</artifactId>
            <version>2.4.0</version>
        </dependency>
        <!--导入neo4j的ogm依赖 -->
        <dependency>
            <groupId>org.neo4j</groupId>
            <artifactId>neo4j-ogm-core</artifactId>
            <version>3.2.18</version>
        </dependency>
```

完整的pom.xml文件为：

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
    <artifactId>neo4j</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>neo4j</name>
    <description>用于测试neo4j的项目</description>

    <properties>
        <java.version>11</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>

        </dependency>
        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>2.4.0</version>
        </dependency>
<!--        导入springboot的neo4j依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-neo4j</artifactId>
            <version>2.4.0</version>
        </dependency>
        <!--导入neo4j的ogm依赖 -->
        <dependency>
            <groupId>org.neo4j</groupId>
            <artifactId>neo4j-ogm-core</artifactId>
            <version>3.2.18</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.16</version>
            <scope>provided</scope>
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

```



#### 6.2.1特征

①Spring Boot集成

②基于注释的对象图映射

③基于接口的存储库支持带注释的和派生的finder方法

④快速的类元数据扫描

⑤优化了数据加载和变更跟踪管理，以减少数据传输

⑥多种传输方式-**二进制协议**，HTTP和嵌入式

⑦持续生命周期事件

#### 6.2.2开始写实体类

实体概述了应用程序中的主要对象。在我们的图中，它们将是节点。

![图模型](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/GraphModel.png)

有一个`Person`实体和一个`Movie`实体，因此每个实体都需要一个域类。每个域类的前几行如下所示。

```java
@NodeEntity
public class Person {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Property("born")
    private int birthyear;
}

@NodeEntity
public class Movie {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private int released;
    @Property("tagline")
    private String description;
}
```

每个类都带有注释，`@NodeEntity`以将这些对象用作图中的节点。每个字段都包含一个*id*字段，该字段被注释为，`id`并填充了数据库（`@GeneratedValue`）生成的值。之后的`id`，将设置其他字段来保存我们要捕获的有关该对象的不同信息。

现在我们需要在每个类中添加行以映射节点之间的关系。

#### 6.2.3编写关系角色类Role.java

在我们的图形数据中，电影和人通过几个不同的关系- `ACTED_IN`，`DIRECTED`等相互连接。在该`ACTED_IN`关系上，我们存储一个关系属性，其中包含该人在特定电影中扮演的角色的列表。我们将需要创建一个关系实体以将其绑定到节点实体`Movie`，`Person`并捕获有关该`ACTED_IN`关系的其他属性。

我们创建一个名为关系的实体`Role`连接`Person`到`Movie`由`Role`他/她玩。我们将此新类注释为关系实体（`@RelationshipEntity`），并指定关系的名称（`ACTED_IN`）。

```java
@RelationshipEntity(type = "ACTED_IN")
public class Role {
    @Id
    @GeneratedValue
    private Long id;
    private List<String> roles = new ArrayList<>();

    @StartNode
    private Person person;

    @EndNode
    private Movie movie;
}
```

该`Role`班也有一个*ID*是由数据库和管理领域*列表*类型字段包含可能的角色，一个人可以在电影播放（可能不止一个）。然后，添加注释以将`Person`和`Movie`节点标记为该关系的*开始节点*和*结束节点*。

#### 6.2.4关系映射

```java
@NodeEntity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Property("born")
    private int birthyear;
    @Relationship(type = "ACTED_IN")
    private List<Role> actedIn = new ArrayList<>();

    @Relationship(type = "DIRECTED")
    private List<Movie> directed = new ArrayList<>();
}

```

`

```java
@NodeEntity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private int released;
    @Property("tagline")
    private String description;
    @Relationship(type = "ACTED_IN", direction = INCOMING)
    private List<Role> actors = new ArrayList<>();

    @Relationship(type = "DIRECTED", direction = INCOMING)
    private List<Person> directors = new ArrayList<>();
}
```

`Person`和`Movie`类中的引用字段都使用标记为`@Relationship`，关系类型为`ACTED_IN`和`DIRECTED`。方向属性默认是*传出*的，因此我们必须指定关系在`Movie`节点上传入。

最后，由于这些实体都连接在一起，因此当我们在请求中拉出一个实体时，它将拉动其余实体。当它拉其他实体时，它将遵循关系回到起始实体，然后再回到相关实体，从而创建循环无限递归循环。为避免此错误，我们可以添加注释以在遍历关系时忽略某些字段。避免请求递归

```java
@NodeEntity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Property("born")
    private int birthyear;
    @JsonIgnoreProperties("person")
    @Relationship(type = "ACTED_IN")
    private List<Role> actedIn = new ArrayList<>();
    @JsonIgnoreProperties({"actors", "directors"})
    @Relationship(type = "DIRECTED")
    private List<Movie> directed = new ArrayList<>();
}
```

```java
@NodeEntity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private int released;
    @Property("tagline")
    private String description;
    @JsonIgnoreProperties("movie")
    @Relationship(type = "ACTED_IN", direction = INCOMING)
    private List<Role> actors = new ArrayList<>();
    @JsonIgnoreProperties({"actedIn", "directed"})
    @Relationship(type = "DIRECTED", direction = INCOMING)
    private List<Person> directors = new ArrayList<>();
}
```



```java


  @RelationshipEntity(type = "ACTED_IN")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Role {
       @Id
        @GeneratedValue
        private Long id;
        private List<String> roles = new ArrayList<>();
        @StartNode
        @JsonIgnoreProperties({"actedIn", "directed"})
        private Person person;
        @EndNode
        @JsonIgnoreProperties({"actors", "directors"})
        private Movie movie;
    }
```

该`@JsonIgnoreProperties`注释放在所有的关系变量忽略下一个实体的字段连接回，避免了无限递归错误和重复信息返回。现在，我们在应用程序中映射了图结构。这是对象图形映射（OGM）的一部分。

#### 6.2.5查询接口



