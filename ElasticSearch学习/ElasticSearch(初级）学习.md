





# elasticsearch简单使用总结

## 1.Linux  Ubuntu18.04安装ELK

### 1.1安装ElastiSearch 7.9.3

#### 1.1.1安装ElastiSearch 7.9.3

```
docker pull elasticsearch:7.9.3
```

#### 1.1.2运行

```
docker run --name elasticsearch01 -d  -p 9200:9200 -p 9300:9300  --network esnet -e "discovery.type=single-node" elasticsearch:7.9.3
```

#### 1.1.3访问：http://121.89.208.222:9200/

![image-20201121203104550](images\21.png)

出现该页面则访问成功。

##### 1.1.4解决跨域请求

进入容器：

```
docker exec -it elasticsearch01 bash
```



修改  config/elasticsearch.yml配置文件尾部添加以下内容：：

```
vi  config/elasticsearch.yml
```

![image-20201121203659670](images\22.png)



```
http.cors.enabled: true
http.cors.allow-origin: "*"

```

### 1.2安装kibana7.9.3（版本要和elasticsearch7.9.3一致）

#### 1.2.1安装kibana7.9.3到本地

```
docker pull kibana:7.9.3
```

#### 1.2.2运行kibana(特别注意要指定 --link = 你创建的正在运行的elastisearch的容器名称)

```
docker run --name kibana01 -d  -p 5602:5601 --link=elasticsearch01 kibana:7.9.3
```

这里使用我已经创建的elastisearch01作为kibana连接的容器。必须要指定 --link=“你已经创建的正在运行的elasticsearch的容器名称”作为容器连接，否则会出现：

![image-20201121205818859](images\25.png)

#### 1.2.3进入kibana容器kibana01，修改配置文件 kibana.yml



![image-20201121205100867](images\23.png)

将 “http://elasticsearch:9200”修改为：“http://elasticsearch01:9200”，也就是把这个URL中的IP地址改为和之前创建kibana时指定的 --link =elasticsearch01的对应。如果创建kibana时未指定 --link，则默认是 “elasticsearch".



![image-20201121205214593](images\24.png)

修改后重新启动kibana。

```
docker restart kibana01

```

![image-20201121205935284](images\26.png)

### 1.3安装elasticsearch head插件监控管理

#### 1.3.1安装并运行

```
 docker run --name eshead -d -p 9100:9100  mobz/elasticsearch-head:5-alpine
```

![image-20201121212906437](images\28.png)

#### 1.3.2在服务器开放9100端口

#### 1.3.3访问地址：http://121.89.208.222:9100/

![image-20201121212844162](images\27.png)

## 2、ES核心概念

```
集群，节点，索引，类型，文档，分片，映射是什么？
```

> elasticsearch是面向文档，关系型数据库和elasticsearch客观的对比！一切都是json

| Relational DB      | Elasticsearch   |
| ------------------ | --------------- |
| 数据库（database） | 索引（indices） |
| 表（tables）       | types           |
| 行（rows）         | documents       |
| 字段（columns）    | fields          |

物理设计：

elasticsearch在后台把每个索引划分成多个分片。每个分片可以在集群中的不同服务器间迁移

逻辑设计：

一个索引类型中，抱哈an多个文档，当我们索引一篇文档时，可以通过这样的一个顺序找到它：索引-》类型-》文档id，通过这个组合我们就能索引到某个具体的文档。注意：`ID不必是整数，实际上它是一个字符串。`

### 文档

> 文档

就是我们的一条条的记录

之前说elasticsearch是面向文档的,那么就意味着索弓和搜索数据的最小单位是文档, elasticsearch中,文档有几个重要属性:

- 自我包含, - -篇文档同时包含字段和对应的值,也就是同时包含key:value !
- 可以是层次型的，-一个文档中包含自文档,复杂的逻辑实体就是这么来的! {就是一 个json对象! fastjson进行自动转换!}
- 灵活的结构,文档不依赖预先定义的模式,我们知道关系型数据库中,要提前定义字段才能使用,在elasticsearch中,对于字段是非常灵活的,有时候,我们可以忽略该字段,或者动态的添加一个新的字段。

尽管我们可以随意的新增或者忽略某个字段,但是,每个字段的类型非常重要,比如一一个年龄字段类型,可以是字符串也可以是整形。因为elasticsearch会保存字段和类型之间的映射及其他的设置。这种映射具体到每个映射的每种类型,这也是为什么在elasticsearch中,类型有时候也称为映射类型。

### 类型

> 类型

类型是文档的逻辑容器,就像关系型数据库一样,表格是行的容器。类型中对于字段的定 义称为映射,比如name映射为字符串类型。我们说文档是无模式的 ,它们不需要拥有映射中所定义的所有字段,比如新增一个字段,那么elasticsearch是怎么做的呢?elasticsearch会自动的将新字段加入映射,但是这个字段的不确定它是什么类型, elasticsearch就开始猜,如果这个值是18 ,那么elasticsearch会认为它是整形。但是elasticsearch也可能猜不对 ，所以最安全的方式就是提前定义好所需要的映射,这点跟关系型数据库殊途同归了,先定义好字段,然后再使用,别整什么幺蛾子。

### 索引

> 索引

就是数据库!

索引是映射类型的容器, elasticsearch中的索引是一个非常大的文档集合。索|存储了映射类型的字段和其他设置。然后它们被存储到了各个分片上了。我们来研究下分片是如何工作的。

**物理设计:节点和分片如何工作**

一个集群至少有一 个节点,而一个节点就是一-个elasricsearch进程 ,节点可以有多个索引默认的,如果你创建索引,那么索引将会有个5个分片( primary shard ,又称主分片)构成的,每一个主分片会有-一个副本( replica shard ,又称复制分片）

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828224136138.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

上图是一个有3个节点的集群,可以看到主分片和对应的复制分片都不会在同-个节点内,这样有利于某个节点挂掉了,数据也不至于丢失。实际上, 一个分片是- -个Lucene索引, -一个包含倒排索引的文件目录,倒排索引的结构使得elasticsearch在不扫描全部文档的情况下,就能告诉你哪些文档包含特定的关键字。不过,等等,倒排索引是什么鬼?

### 倒排索引

> 倒排索引

elasticsearch使用的是一种称为倒排索引 |的结构,采用Lucene倒排索作为底层。这种结构适用于快速的全文搜索，一个索引由文
档中所有不重复的列表构成,对于每一个词,都有一个包含它的文档列表。 例如,现在有两个文档，每个文档包含如下内容:

```shell
Study every day， good good up to forever  # 文 档1包含的内容
To forever, study every day，good good up  # 文档2包含的内容
12
```

为为创建倒排索引,我们首先要将每个文档拆分成独立的词(或称为词条或者tokens) ,然后创建一一个包含所有不重 复的词条的排序列表,然后列出每个词条出现在哪个文档:

| term    | doc_1 | doc_2 |
| ------- | ----- | ----- |
| Study   | √     | x     |
| To      | x     | x     |
| every   | √     | √     |
| forever | √     | √     |
| day     | √     | √     |
| study   | x     | √     |
| good    | √     | √     |
| every   | √     | √     |
| to      | √     | x     |
| up      | √     | √     |

现在，我们试图搜索 to forever，只需要查看包含每个词条的文档

| term    | doc_1 | doc_2 |
| ------- | ----- | ----- |
| to      | √     | x     |
| forever | √     | √     |
| total   | 2     | 1     |

两个文档都匹配,但是第一个文档比第二个匹配程度更高。如果没有别的条件,现在,这两个包含关键字的文档都将返回。
再来看一个示例,比如我们通过博客标签来搜索博客文章。那么倒排索引列表就是这样的一个结构:

| 博客文章(原始数据) | 博客文章(原始数据) | 索引列表(倒排索引) | 索引列表(倒排索引) |
| ------------------ | ------------------ | ------------------ | ------------------ |
| 博客文章ID         | 标签               | 标签               | 博客文章ID         |
| 1                  | python             | python             | 1，2，3            |
| 2                  | python             | linux              | 3，4               |
| 3                  | linux，python      |                    |                    |
| 4                  | linux              |                    |                    |

如果要搜索含有python标签的文章,那相对于查找所有原始数据而言，查找倒排索引后的数据将会快的多。只需要查看标签这一栏,然后获取相关的文章ID即可。完全过滤掉无关的所有数据,提高效率!

elasticsearch的索引和Lucene的索引对比

在elasticsearch中，索引(库)这个词被频繁使用,这就是术语的使用。在elasticsearch中 ,索引被分为多个分片,每份分片是-个Lucene的索引。**所以一个elasticsearch索引是由多 个Lucene索引组成的**。别问为什么,谁让elasticsearch使用Lucene作为底层呢!如无特指，说起索引都是指elasticsearch的索引。

接下来的一切操作都在kibana中Dev Tools下的Console里完成。基础操作!

### ik分词器

> 什么是IK分词器 ?

分词:即把一-段中文或者别的划分成一个个的关键字,我们在搜索时候会把自己的信息进行分词,会把数据库中或者索引库中的数据进行分词,然后进行一个匹配操作,默认的中文分词是将每个字看成一个词,比如“我爱狂神”会被分为"我",“爱”,“狂”,“神” ,这显然是不符合要求的,所以我们需要安装中文分词器ik来解决这个问题。

如果要使用中文,建议使用ik分词器!

IK提供了两个分词算法: **ik_ smart和ik_ max_ word** ,其中ik_ smart为最少切分, ik_ max_ _word为最细粒度划分!一会我们测试!

什么是IK分词器：

- 把一句话分词
- 如果使用中文：推荐IK分词器
- 两个分词算法：ik_smart（最少切分），ik_max_word（最细粒度划分）

**【ik_smart】测试：**

```json
GET _analyze
{
  "analyzer": "ik_smart",
  "text": "我是社会主义接班人"
}

//输出
{
  "tokens" : [
    {
      "token" : "我",
      "start_offset" : 0,
      "end_offset" : 1,
      "type" : "CN_CHAR",
      "position" : 0
    },
    {
      "token" : "是",
      "start_offset" : 1,
      "end_offset" : 2,
      "type" : "CN_CHAR",
      "position" : 1
    },
    {
      "token" : "社会主义",
      "start_offset" : 2,
      "end_offset" : 6,
      "type" : "CN_WORD",
      "position" : 2
    },
    {
      "token" : "接班人",
      "start_offset" : 6,
      "end_offset" : 9,
      "type" : "CN_WORD",
      "position" : 3
    }
  ]
}
123456789101112131415161718192021222324252627282930313233343536373839
```

**【ik_max_word】测试：**

```json
GET _analyze
{
  "analyzer": "ik_max_word",
  "text": "我是社会主义接班人"
}
//输出
{
  "tokens" : [
    {
      "token" : "我",
      "start_offset" : 0,
      "end_offset" : 1,
      "type" : "CN_CHAR",
      "position" : 0
    },
    {
      "token" : "是",
      "start_offset" : 1,
      "end_offset" : 2,
      "type" : "CN_CHAR",
      "position" : 1
    },
    {
      "token" : "社会主义",
      "start_offset" : 2,
      "end_offset" : 6,
      "type" : "CN_WORD",
      "position" : 2
    },
    {
      "token" : "社会",
      "start_offset" : 2,
      "end_offset" : 4,
      "type" : "CN_WORD",
      "position" : 3
    },
    {
      "token" : "主义",
      "start_offset" : 4,
      "end_offset" : 6,
      "type" : "CN_WORD",
      "position" : 4
    },
    {
      "token" : "接班人",
      "start_offset" : 6,
      "end_offset" : 9,
      "type" : "CN_WORD",
      "position" : 5
    },
    {
      "token" : "接班",
      "start_offset" : 6,
      "end_offset" : 8,
      "type" : "CN_WORD",
      "position" : 6
    },
    {
      "token" : "人",
      "start_offset" : 8,
      "end_offset" : 9,
      "type" : "CN_CHAR",
      "position" : 7
    }
  ]
}
123456789101112131415161718192021222324252627282930313233343536373839404142434445464748495051525354555657585960616263646566
```

## 3、命令模式的使用

### 3.1 Rest风格说明

一种软件架构风格，而不是标准。更易于实现缓存等机制

| method | url地址                                         | 描述                   |
| ------ | ----------------------------------------------- | ---------------------- |
| PUT    | localhost:9200/索引名称/类型名称/文档id         | 创建文档(指定文档id)   |
| POST   | localhost:9200/索引名称/类型名称                | 创建文档（随机文档id） |
| POST   | localhost:9200/索引名称/类型名称/文档id/_update | 修改文档               |
| DELETE | localhost:9200/索引名称/类型名称/文档id         | 删除文档               |
| GET    | localhost:9200/索引名称/类型名称/文档id         | 通过文档id查询文档     |
| POST   | localhost:9200/索引名称/类型名称/_search        | 查询所有的数据         |

> 基础测试

1.创建一个索引

PUT /索引名/类型名(高版本都不写了，都是_doc)/文档id

{请求体}

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828224224886.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

完成了自动添加了索引！数据也成功的添加了。

![[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-LdUc6t8b-1598625546984)(C:\Users\53984\AppData\Roaming\Typora\typora-user-images\1598532887497.png)]](https://img-blog.csdnimg.cn/20200828224246679.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

那么name这个字段用不用指定类型呢

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828224311944.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

指定字段的类型properties 就比如sql创表

获得这个规则！可以通过GET请求获得具体的信息

![在这里插入图片描述](https://img-blog.csdnimg.cn/2020082822452110.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

如果自己不设置文档字段类型，那么es会自动给默认类型

![[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-CNGgrrjI-1598625546996)(C:\Users\53984\AppData\Roaming\Typora\typora-user-images\1598533818617.png)]](https://img-blog.csdnimg.cn/20200828224539919.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

### 3.2 cat命令

获取健康值

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828224607691.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

获取所有的信息

```shell
GET _cat/indices?v
1
```

![[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-1ZKzwXyB-1598625547001)(C:\Users\53984\AppData\Roaming\Typora\typora-user-images\1598534090085.png)]](https://img-blog.csdnimg.cn/20200828224623550.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

还有很多 可以自动展示 都试试

> #### 修改索引

1.修改我们可以还是用原来的PUT的命令，根据id来修改

![[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-J5lCYCsQ-1598625547003)(C:\Users\53984\Desktop\文件\md学习文件\1598534298931.png)]](https://img-blog.csdnimg.cn/2020082822464153.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

但是如果没有填写的字段 会重置为空了 ，相当于java接口传对象修改，如果只是传id的某些字段，那其他没传的值都为空了。

2.还有一种update方法 这种不设置某些值 数据不会丢失

```html
POST /test3/_doc/1/_update
{
  "doc":{
    "name":"212121"
  }
}

//下面两种都是会将不修改的值清空的

POST /test3/_doc/1
{
    "name":"212121"
}

POST /test3/_doc/1
{
  "doc":{
    "name":"212121"
  }
}
1234567891011121314151617181920
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828224703275.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828224718584.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

带doc修改 查询也是带doc的（document）

> #### 删除索引

关于删除索引或者文档的操作

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828224733641.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

通过DELETE命令实现删除，根据你的请求来判断是删除索引还是删除文档记录

使用RESTFUL的风格是我们ES推荐大家使用的！

### 3.3 关于文档的基本操作

#### 查询

最简单的搜索是GET

搜索功能search

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828224751861.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

这边name是text 所以做了分词的查询 如果是keyword就不会分词搜索了

> ### 复杂操作搜索select（排序，分页，高亮，模糊查询，精准查询）

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828224818363.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

```json
//测试只能一个字段查询
GET lisen/user/_search
{
  "query": {
    "match": {
      "name": "李森"
    }
  }
}
123456789
```

结果过滤，就是只展示列表中某些字段

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828224837652.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

包含

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828224854820.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

不包含

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828224913742.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

排序

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828224929501.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

分页

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828224950826.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

代码

```json
GET lisen/user/_search
{
  "query": {
    "match": {
      "name": "李森"
    }
  },
  "sort":{
    "age":{
      "order":"asc"
    }
  },
  "from": 0,
  "size": 1
}
123456789101112131415
```

#### 多条件查询

> 布尔值查询

must（and），所有的条件都要符合

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828225018246.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

should（or）或者的 跟数据库一样

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828225032999.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

must_not（not）
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828225058189.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

条件区间
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020082822512379.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

- gt大于
- gte大于等于
- lte小于
- lte小于等于

> 匹配多个条件（数组）

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828225140204.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

match没用倒排索引 这边改正一下

> 精确查找

term查询是直接通过倒排索引指定的词条进程精确查找的

#### 关于分词

- term，直接查询精确的
- match，会使用分词器解析！（先分析文档，然后通过分析的文档进行查询）

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828225158338.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

默认的是被分词了

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828225215133.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

keyword没有被分词

精确查询多个值

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828225246611.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

> 高亮

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828225305992.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

还能自定义高亮的样式
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200828225327239.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpc2VuMDEwNzAxMDc=,size_16,color_FFFFFF,t_70#pic_center)

## 4、springboot集成

### 4.1 引入依赖包

创建一个springboot的项目 同时勾选上`springboot-web`的包以及`Nosql的elasticsearch`的包

如果没有就手动引入

```xml
<!--es客户端-->
<dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>elasticsearch-rest-high-level-client</artifactId>
    <version>7.6.2</version>
</dependency>

<!--springboot的elasticsearch服务-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
</dependency>
123456789101112
```

注意下spring-boot的parent包内的依赖的es的版本是不是你对应的版本

不是的话就在pom文件下写个properties的版本

```xml
<!--这边配置下自己对应的版本-->
<properties>
    <java.version>1.8</java.version>
    <elasticsearch.version>7.6.2</elasticsearch.version>
</properties>
12345
```

### 4.2 注入RestHighLevelClient 客户端

```java
@Configuration
public class ElasticSearchClientConfig {
    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("127.0.0.1",9200,"http"))
        );
        return client;
    }
}
12345678910
```

### 4.3 索引的增、删、是否存在

```java
//测试索引的创建
@Test
void testCreateIndex() throws IOException {
    //1.创建索引的请求
    CreateIndexRequest request = new CreateIndexRequest("lisen_index");
    //2客户端执行请求，请求后获得响应
    CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
    System.out.println(response);
}

//测试索引是否存在
@Test
void testExistIndex() throws IOException {
    //1.创建索引的请求
    GetIndexRequest request = new GetIndexRequest("lisen_index");
    //2客户端执行请求，请求后获得响应
    boolean exist =  client.indices().exists(request, RequestOptions.DEFAULT);
    System.out.println("测试索引是否存在-----"+exist);
}

//删除索引
@Test
void testDeleteIndex() throws IOException {
    DeleteIndexRequest request = new DeleteIndexRequest("lisen_index");
    AcknowledgedResponse delete = client.indices().delete(request,RequestOptions.DEFAULT);
    System.out.println("删除索引--------"+delete.isAcknowledged());
}
123456789101112131415161718192021222324252627
```

### 4.4 文档的操作

```java
//测试添加文档
    @Test
    void testAddDocument() throws IOException {
        User user = new User("lisen",27);
        IndexRequest request = new IndexRequest("lisen_index");
        request.id("1");
        //设置超时时间
        request.timeout("1s");
        //将数据放到json字符串
        request.source(JSON.toJSONString(user), XContentType.JSON);
        //发送请求
        IndexResponse response = client.index(request,RequestOptions.DEFAULT);
        System.out.println("添加文档-------"+response.toString());
        System.out.println("添加文档-------"+response.status());
//        结果
//        添加文档-------IndexResponse[index=lisen_index,type=_doc,id=1,version=1,result=created,seqNo=0,primaryTerm=1,shards={"total":2,"successful":1,"failed":0}]
//        添加文档-------CREATED
    }

    //测试文档是否存在
    @Test
    void testExistDocument() throws IOException {
        //测试文档的 没有index
        GetRequest request= new GetRequest("lisen_index","1");
        //没有indices()了
        boolean exist = client.exists(request, RequestOptions.DEFAULT);
        System.out.println("测试文档是否存在-----"+exist);
    }

    //测试获取文档
    @Test
    void testGetDocument() throws IOException {
        GetRequest request= new GetRequest("lisen_index","1");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        System.out.println("测试获取文档-----"+response.getSourceAsString());
        System.out.println("测试获取文档-----"+response);

//        结果
//        测试获取文档-----{"age":27,"name":"lisen"}
//        测试获取文档-----{"_index":"lisen_index","_type":"_doc","_id":"1","_version":1,"_seq_no":0,"_primary_term":1,"found":true,"_source":{"age":27,"name":"lisen"}}

    }

    //测试修改文档
    @Test
    void testUpdateDocument() throws IOException {
        User user = new User("李逍遥", 55);
        //修改是id为1的
        UpdateRequest request= new UpdateRequest("lisen_index","1");
        request.timeout("1s");
        request.doc(JSON.toJSONString(user),XContentType.JSON);

        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        System.out.println("测试修改文档-----"+response);
        System.out.println("测试修改文档-----"+response.status());

//        结果
//        测试修改文档-----UpdateResponse[index=lisen_index,type=_doc,id=1,version=2,seqNo=1,primaryTerm=1,result=updated,shards=ShardInfo{total=2, successful=1, failures=[]}]
//        测试修改文档-----OK

//        被删除的
//        测试获取文档-----null
//        测试获取文档-----{"_index":"lisen_index","_type":"_doc","_id":"1","found":false}
    }


    //测试删除文档
    @Test
    void testDeleteDocument() throws IOException {
        DeleteRequest request= new DeleteRequest("lisen_index","1");
        request.timeout("1s");
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        System.out.println("测试删除文档------"+response.status());
    }

    //测试批量添加文档
    @Test
    void testBulkAddDocument() throws IOException {
        ArrayList<User> userlist=new ArrayList<User>();
        userlist.add(new User("cyx1",5));
        userlist.add(new User("cyx2",6));
        userlist.add(new User("cyx3",40));
        userlist.add(new User("cyx4",25));
        userlist.add(new User("cyx5",15));
        userlist.add(new User("cyx6",35));

        //批量操作的Request
        BulkRequest request = new BulkRequest();
        request.timeout("1s");

        //批量处理请求
        for (int i = 0; i < userlist.size(); i++) {
            request.add(
                    new IndexRequest("lisen_index")
                            .id(""+(i+1))
                            .source(JSON.toJSONString(userlist.get(i)),XContentType.JSON)
            );
        }
        BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
        //response.hasFailures()是否是失败的
        System.out.println("测试批量添加文档-----"+response.hasFailures());

//        结果:false为成功 true为失败
//        测试批量添加文档-----false
    }


    //测试查询文档
    @Test
    void testSearchDocument() throws IOException {
        SearchRequest request = new SearchRequest("lisen_index");
        //构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //设置了高亮
        sourceBuilder.highlighter();
        //term name为cyx1的
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "cyx1");
        sourceBuilder.query(termQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        System.out.println("测试查询文档-----"+JSON.toJSONString(response.getHits()));
        System.out.println("=====================");
        for (SearchHit documentFields : response.getHits().getHits()) {
            System.out.println("测试查询文档--遍历参数--"+documentFields.getSourceAsMap());
        }

//        测试查询文档-----{"fragment":true,"hits":[{"fields":{},"fragment":false,"highlightFields":{},"id":"1","matchedQueries":[],"primaryTerm":0,"rawSortValues":[],"score":1.8413742,"seqNo":-2,"sortValues":[],"sourceAsMap":{"name":"cyx1","age":5},"sourceAsString":"{\"age\":5,\"name\":\"cyx1\"}","sourceRef":{"fragment":true},"type":"_doc","version":-1}],"maxScore":1.8413742,"totalHits":{"relation":"EQUAL_TO","value":1}}
//        =====================
//        测试查询文档--遍历参数--{name=cyx1, age=5}
    }
```