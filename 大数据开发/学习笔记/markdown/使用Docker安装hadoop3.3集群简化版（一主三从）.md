# 使用Docker安装Hadoop3.3集群简化版（一主三从）

**服务器配置：** 

> 2核4G ubuntu18.0.4 
>
> docker  Docker version 20.10.11和docker-compose version 1.29.2

**软件：**

> hadoop-3.3.1.tar.gz
>
> jdk-11.0.1_linux-x64_bin.tar.gz

**构建hadoop的基础镜像：**

> centos:latest

## 1.下载jdk和haddp源码到指定目录下并在该目录下创建一个dockerfile文件

首先将jdk11的源码压缩包和Hadoop3.3.0的源码压缩包上传到服务器，存在在某一个文件夹中。

### 1.1创建在CentOS中启动的脚本 startconfig.sh

```bash
vi startconfig.sh
```

加入以下内容：

```bash
#!/bin/bash
# 启动sshd服务器
/usr/sbin/sshd -D

# 覆盖写入/etc/hosts，不能有多余的ip和机器名映射。
cat > /etc/hosts<<EOF

# 覆盖写入到hosts的内容
192.168.0.2  hadoop-master

192.168.0.3  hadoop1

192.168.0.4  hadoop2

EOF

#由于/etc/hosts文件在容器启动时被重写，直接修改内容在容器重启后不能保留，为了让容器在重启之后获取集群hosts，使用了一种启动容器后重写hosts的方法。需要在~/.bashrc中追加内容
# 开始向~/.bashrc追加内容，注意是cat >> 追加写入而不是cat > 覆盖写入！！！！
cat >> ~/.bashrc <<EOF

# 追加到 ~/.bashrc的内容 开始,注意是 cat > 覆盖写入而不是 cat >>追加写入！！！！
:>/etc/hosts

cat > /etc/hosts<<EOF

192.168.0.2  hadoop-master
192.168.0.3  hadoop1
192.168.0.4  hadoop2
192.168.0.5  hadoop3
EOF


# 更新配置文件
source  ~/.bashrc

# 覆盖写入/etc/ansible/hosts
cat > /etc/ansible/hosts<<EOF

[hadoop]

hadoop-master

hadoop1

hadoop2

[master]

hadoop-master

[slaves]

hadoop1

hadoop2

EOF

```

![image-20211130213453913](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/20211130213454.png)

### 1.2dockerfile文件

```bash
vim dockerfile
```

创建dockerfile文件，并使用vim打开dockerfile并写入一下内容：

```dockerfile
#  基于centos构建
FROM centos
MAINTAINER moyisuiying
# 安装openssh 用于免密登录
RUN yum install -y openssh-server sudo
RUN sed -i 's/UsePAM yes/UsePAM no/g' /etc/ssh/sshd_config
RUN yum  install -y openssh-clients
# 镜像容器的root用户密码为root,可以自己修改
RUN echo "root:root" | chpasswd 
# 授予root 用户容器的全部权限
RUN echo "root   ALL=(ALL)       ALL" >> /etc/sudoers 
# 生成公钥秘钥
RUN ssh-keygen -t rsa -f /root/.ssh/id_rsa
# 将生成的公钥复制到authorized_keys中
RUN cat /root/.ssh/id_rsa.pub  >> /root/.ssh/authorized_keys
# 在centos中创建/hadoop/opt目录用于存放hadoop和jdk源码
RUN mkdir -p /hadoop/opt
# 将当前dockerfile同目录下的startconfig.sh复制到centos的/hadoop/opt/下
ADD startconfig.sh /hadoop/opt/startconfig.sh
# 赋予startconfig.sh权限可以执行
RUN chmod +x  /hadoop/opt/startconfig.sh
# 安装epel-release和ansible，用于后面便于从hadoop master进行批量传输文件到各个slave中
RUN yum -y install epel-release
RUN yum -y install ansible
# centos对外暴露端口
ENV PORT=22
EXPOSE $PORT
# 将dockerfile所在目录下的jdk-11.0.1_linux-x64_bin.tar.gz复制到centos的/hadoop/opt目录下并解压
ADD jdk-11.0.1_linux-x64_bin.tar.gz  /hadoop/opt
# 设置JAVA_HOME环境变量，注意jdk-11.0.1目录就是jdk-11.0.1_linux-x64_bin.tar.gz解压后的文件夹名
ENV JAVA_HOME /hadoop/opt/jdk-11.0.1
ENV PATH $JAVA_HOME/bin:$PATH
# 将dockerfile所在目录下的hadoop-3.3.1.tar.gz复制到centos的/hadoop/opt目录下并解压
ADD hadoop-3.3.1.tar.gz /hadoop/opt
# 设置HADOOP_HOMOE的环境变量，注意hadoop-3.3.1目录就是hadoop-3.3.1.tar.gz解压后的文件夹名
ENV HADOOP_HOME /hadoop/opt/hadoop-3.3.1
ENV PATH $HADOOP_HOME/bin:$PATH
# 解决从centos实例中通过ssh登录到另一个centos实例后出现环境变量失效的问题
RUN echo "export $(cat /proc/1/environ |tr '\0' '\n' | xargs)" >> /etc/profile
RUN yum install -y which sudo
# 启动容器时候执行centos中/hadoop/opt/startconfig.sh的脚本
CMD ["/hadoop/opt/startconfig.sh", "-D"]
```

### 13构建镜像

```bash
# docker build -f  [dockerfile name] -t  [tagname:版本]  .   
# 不要漏掉这个  "."
docker build -f dockerfile -t hadoop-centos:v1  .
```

![image-20211130193736656](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/20211130193736.png)

## 2.创建一个子网

```bash
docker network create --driver=bridge --subnet=192.168.0.0/16 --gateway=192.168.0.1  hadoopnetwork

```



## 3.启动容器

### 3.1mapreduce的默认端口

| mapreduce.jobhistory.address                  | 0.0.0.0:10020     | MapReduce JobHistory Server IPC host:port        |
| --------------------------------------------- | ----------------- | ------------------------------------------------ |
| **mapreduce.jobhistory.webapp.address**       | **0.0.0.0:19888** | **MapReduce JobHistory Server Web UI host:port** |
| **mapreduce.jobhistory.webapp.https.address** | **0.0.0.0:19890** |                                                  |
| **mapreduce.jobhistory.admin.address**        | **0.0.0.0:10033** |                                                  |

### 3.2yarn的默认端口

| yarn.nodemanager.webapp.address                   | ${yarn.nodemanager.hostname}:8042         |
| ------------------------------------------------- | ----------------------------------------- |
| **yarn.nodemanager.webapp.https.address**         | **0.0.0.0:8044**                          |
| **yarn.router.webapp.address**                    | **0.0.0.0:8089**                          |
| **yarn.router.webapp.https.address**              | **0.0.0.0:8091**                          |
| **yarn.nodemanager.amrmproxy.address**            | **0.0.0.0:8049**                          |
| **yarn.sharedcache.webapp.address**               | **0.0.0.0:8788**                          |
| **yarn.sharedcache.uploader.server.address**      | **0.0.0.0:8046**                          |
| **yarn.sharedcache.client-server.address**        | **0.0.0.0:8045**                          |
| **yarn.sharedcache.admin.address**                | **0.0.0.0:8047**                          |
| **yarn.resourcemanager.resource-tracker.address** | **${yarn.resourcemanager.hostname}:8031** |
| **yarn.resourcemanager.address**                  | **${yarn.resourcemanager.hostname}:8032** |
| **yarn.resourcemanager.admin.address**            | **${yarn.resourcemanager.hostname}:8033** |
| **yarn.resourcemanager.webapp.address**           | **${yarn.resourcemanager.hostname}:8088** |
| **yarn.resourcemanager.scheduler.address**        | **${yarn.resourcemanager.hostname}:8030** |
| **yarn.resourcemanager.webapp.https.address**     | **${yarn.resourcemanager.hostname}:8090** |

### 3.3dfs的默认端口

| dfs.namenode.secondary.http-address      | 0.0.0.0:9868      | The secondary namenode http server address and port.         |
| ---------------------------------------- | ----------------- | ------------------------------------------------------------ |
| **dfs.namenode.secondary.https-address** | **0.0.0.0:9869**  | **The secondary namenode HTTPS server address and port.**    |
| **dfs.namenode.backup.address**          | **0.0.0.0:50100** | **The backup node server address and port. If the port is 0 then the server will start on a free port.** |
| **dfs.namenode.backup.http-address**     | **0.0.0.0:50105** |                                                              |
| **dfs.namenode.http-address**            | **0.0.0.0:9870**  |                                                              |
| **dfs.namenode.https-address**           | **0.0.0.0:9871**  |                                                              |
| **dfs.datanode.http.address**            | **0.0.0.0:9864**  | **The datanode http server address and port.**               |
| **dfs.datanode.https.address**           | **0.0.0.0:9865**  | **The datanode secure http server address and port.**        |
| **dfs.datanode.address**                 | **0.0.0.0:9866**  | **The datanode server address and port for data transfer.**  |
| **dfs.datanode.ipc.address**             | **0.0.0.0:9867**  | **The datanode ipc server address and port.**                |

### 3.4架构的划分

|      | hadoop-master         | hadoop1                        | hadoop2                         | hadoop3           |
| ---- | --------------------- | ------------------------------ | ------------------------------- | ----------------- |
| HDFS | **nameNode**,datanode | **SecondaryNameNode**,DataNode | DataNode                        | DataNode          |
| YARN | NodeManager           | NodeManager                    | **ResourceManager,NodeManager** | **historyServer** |



```bash
docker run -d  --privileged=true --name  hadoop-master -h hadoop-master  -p 9870:9870  -p 9871:9871   --net hadoopnetwork --ip 192.168.0.2 -v /bigdata/hadoop/hadoop-master:/hadoop/file  hadoop-centos:v1  /usr/sbin/init
docker run -d  --privileged=true --name  hadoop1 -h hadoop1  -p 9868:9868 -p 9869:9869   --net hadoopnetwork --ip 192.168.0.3  hadoop-centos:v1  /usr/sbin/init
docker run -d  --privileged=true --name  hadoop2 -h hadoop2  -p 8032:8032 -p 8088:8088 -p 8042:8042 --net hadoopnetwork --ip 192.168.0.4   hadoop-centos:v1  /usr/sbin/init
docker run -d  --privileged=true --name  hadoop3 -h hadoop3 -p 10020:10020 -p 19888:19888 -p 19890:19890  --net hadoopnetwork --ip 192.168.0.5    hadoop-centos:v1  /usr/sbin/init
```

启动容器后进入容器无法使用systemctl命令：

```bash
[root@hadoop4 /]# systemctl status sshd
Failed to connect to bus: No such file or directory
```

![image-20211130160024950](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/20211130160025.png)

解决方法：

```bash
docker run --privileged=true  --name imagesname  images:tag   /usr/sbin/init
```

在启动容器时候要加入 **--privileged=true** 和**/usr/sbin/init**



## 4.进入hadoop-master容器验证是否能和hadoop1,hadoop2,hadoop3免密登录

```bash
# 进入master容器
docker exec -it hadoop-master bash
# 第一次进入时候要 启动 /hadoop/opt/startconfig.sh，注意一定要启动一下！！！
cd /hadoop/opt
#  执行startconfig.sh脚本！！！！！！！！！！！！！！！！！！！！
./startconfig.sh
```

![image-20211130200222746](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/20211130200222.png)

可见能够直接在hadoop-master中直接免密登录hadoop1，同理，依次进入其它容器，验证是否能两两免密登录。

### 4.1ssh登录　The authenticity of host 192.168.0.xxx can't be established.的问题

修改/etc/ssh/ssh_config文件的配置，以后则不会再出现此问题

最后面添加：

```bash
StrictHostKeyChecking noUser
KnownHostsFile /dev/null
```



### 4.2ssh登录不同机器的环境变量问题导致无法使用Java和Hadoop的问题

容器中启用sshd，可以方便连接和排障，以及进行一些日常的运维操作。
但是很多用户进入到容器中却发现，在docker启动时候配置的环境变量通过`env`命令并不能够正常显示。
这个的主要原因还是ssh为用户建立连接的时候会导致环境变量被重置。
这样导致的最大问题就是通过ssh启动的容器进程将无法获取到容器启动时候配置的环境变量。

了解了原理后，这个问题有个简单的方法解决。就是可以通过将容器的环境变量重新设置到ssh连接后的session中。
具体的实现方式是，ssh连接后，会自动执行`source /etc/profile`。
那么我们其实只要在`/etc/profile`追加几行代码，从1号进程获取容器本身的环境变量，然后循环将环境变量export一下即可。

以下是一个简单的for循环实现。

```bash
for item in `cat /proc/1/environ |tr '\0' '\n'`do export $itemdone
```

当然，有更简洁的命令，就是

```bash
export $(cat /proc/1/environ |tr '\0' '\n' | xargs)
```

可以实现同样的效果。



## 5.修改hadoop的配置文件

**配置文件参考：https://hadoop.apache.org/docs/current/index.html**

hadoop运行所需配置文件首先进入$HADOOP_HOME/etc/hadoop/目录

```bash
cd $HADOOP_HOME/etc/hadoop/
```

### 5.1修改workers文件

![image-20211130221447447](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/20211130221447.png)

```
vi workers 
```

修改workers（**注意hadoop3.X版本是workers文件，3.x版本以下是slaves文件**）文件，写入以下内容：

```bash
hadoop-master
hadoop1
hadoop2
hadoop3
```

### 5.2修改core-site.xml

```bash
 vim  core-site.xml
```

修改为一下内容：

```xml
<configuration>
    <property>     
        <name>hadoop.tmp.dir</name>  
        <value>/hadoop/tmp</value>    
        <description>A base for other temporary directories.</description>   
    </property>   
    <!-- file system properties -->   
    <property>    
        <name>fs.default.name</name>  
        <value>hdfs://hadoop-master:9000</value> 
    </property>   
    <property>  
        <name>fs.trash.interval</name>
        <value>4320</value>   
    </property></configuration>
```

### 5.3修改修改hdfs-site.xml

```bash
vi  hdfs-site.xml 
```

修改为一下内容：

```bash
<configuration>
    <!-- 指定secondary的访问地址和端口 -->
    <property>
        <name>dfs.namenode.secondary.http-address</name>
        <value>hadoop1:9868</value>
    </property>
    <!-- 指定namenode的访问地址和端口 -->
    <property>
        <name>dfs.namenode.http-address</name>
        <value>hadoop-master:9870</value>
    </property>
    <property>
        <name>dfs.namenode.name.dir</name>
        <value>file://${hadoop.tmp.dir}/dfs/name</value>
    </property>
    <property>
        <name>dfs.datanode.data.dir</name>
        <value>file://${hadoop.tmp.dir}/dfs/data</value>
    </property>
    <property>
        <name>dfs.replication</name>
        <value>4</value>
    </property>
    <property>
        <name>dfs.webhdfs.enabled</name>
        <value>true</value>
    </property>
    <property>
        <name>dfs.permissions.superusergroup</name>
        <value>staff</value>
    </property>
    <property>
        <name>dfs.permissions.enabled</name>
        <value>false</value>
    </property>
    <!-- 指定namenode日志文件的存放目录 -->
    <property>
        <name>dfs.namenode.edits.dir</name>
        <value>${dfs.namenode.name.dir}</value>
    </property>
    <!-- 设置一个文件切片的大小：128M-->
    <property>
        <name>dfs.blocksize</name>
        <value>134217728</value>
    </property>
</configuration>
```

### 5.4修改mapred-site.xml

```
vi mapred-site.xml
```

修改为一下内容：

```xml
<configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
    <property>
        <name>yarn.app.mapreduce.am.env</name>
        <value>HADOOP_MAPRED_HOME=${HADOOP_HOME}</value>
    </property>
    <property>
        <name>mapreduce.map.env</name>
        <value>HADOOP_MAPRED_HOME=${HADOOP_HOME}</value>
    </property>
    <property>
        <name>mapreduce.reduce.env</name>
        <value>HADOOP_MAPRED_HOME=${HADOOP_HOME}</value>
    </property>
    <property>
        <name>mapreduce.jobhisotry.address</name>
        <value>hadoop3:10020</value>
    </property>
    <property>
        <name>mapreduce.jobhistory.webapp.address</name>
        <value>hadoop3:19888</value>
    </property>
    <property>
        <name>mapreduce.jobhistory.done-dir</name>
        <value>/hadoop/jobhistory/done</value>
    </property>
    <property>
        <name>mapreduce.intermediate-done-dir</name>
        <value>/hadoop/jobhisotry/done_intermediate</value>
    </property>
    <property>
        <name>mapreduce.job.ubertask.enable</name>
        <value>true</value>
    </property>
</configuration>
```

### 5.5修改yarn-site.xml

```bash
vi  yarn-site.xml
```



```xaml
<configuration>
    <property>
        <name>yarn.resourcemanager.hostname</name>
        <value>hadoop2</value>
    </property>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
    <property>
        <name>yarn.nodemanager.aux-services.mapreduce.shuffle.class</name>
        <value>org.apache.hadoop.mapred.ShuffleHandler</value>
    </property>
    <property>
        <name>yarn.nodemanager.webapp.address</name>
        <value>${yarn.resourcemanager.hostname}:8042</value>
    </property>
    <property>
        <name>yarn.resourcemanager.address</name>
        <value>${yarn.resourcemanager.hostname}:8032</value>
    </property>
    <property>
        <name>yarn.resourcemanager.scheduler.address</name>
        <value>${yarn.resourcemanager.hostname}:8030</value>
    </property>
    <property>
        <name>yarn.resourcemanager.resource-tracker.address</name>
        <value>${yarn.resourcemanager.hostname}:8031</value>
    </property>
    <property>
        <name>yarn.resourcemanager.admin.address</name>
        <value>${yarn.resourcemanager.hostname}:8033</value>
    </property>
    <property>
        <name>yarn.resourcemanager.webapp.address</name>
        <value>${yarn.resourcemanager.hostname}:8088</value>
    </property>
    <property>
        <name>yarn.log-aggregation-enable</name>
        <value>true</value>
    </property>
    <property>
        <name>yarn.log-aggregation.retain-seconds</name>
        <value>86400</value>
    </property>
    <property>
        <name>yarn.log-aggregation.retain-check-interval-seconds</name>
        <value>86400</value>
    </property>
    <property>
        <name>yarn.nodemanager.remote-app-log-dir</name>
        <value>/hadoop/logs/remoteApiLogs</value>
    </property>
    <property>
        <name>yarn.nodemanager.remote-app-log-dir-suffix</name>
        <value>logs</value>
    </property>
</configuration>
```

## 6.将修改好后的配置文件打包后分别分发给slaves

### 6.1创建hadoop-configfile.yaml用于分发配置文件到各个slaves

只需要将需要修改的配置文件分发到每个slave即可，不需要整个Hadoop的文件夹都发送

```bash
vi hadoop-configfile.yaml
```

添加以下内容：

```bash
---
- hosts: hadoop
  tasks:
    - name: copy Hadoop-3.3.1的配置文件  to slaves
      copy: src=/hadoop/opt/hadoop-3.3.1/etc/hadoop dest=/hadoop/opt/hadoop-3.3.1/etc/
    - name: copy hadoop configfile to slaves
      copy: src=/hadoop/opt/hadoop-3.3.1/sbin  dest=/hadoop/opt/hadoop-3.3.1/
```

将以上yaml保存为hadoop-configfile.yaml）并执行

```bash
ansible-playbook hadoop-configfile.yaml
```

![image-20211130224952743](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/20211130224952.png)

![image-20211130225003753](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/20211130225003.png)



### 6.2发送master的系统配置文件到slave

```bash
vi profile.yaml
```

加入以下内容：

```bash
---- 
   hosts: hadoop  tasks:    
     - name: copy profile to slaves     
       copy: src=/etc/profile dest=/etc/      
       notify:       
        - exec sourceprofile    
     - name: copy bashrc to slaves      
       copy: src=~/.bashrc dest=~/    
       notify:       
        - exec sourcebashrc  handlers:   
        - name: exec sourceprofile      
        shell: source  /etc/profile     
        - name: exec sourcebashrc      
          shell: source ~/.bashrc
```

执行分发命令：

```bash
[root@hadoop-master opt]# ansible-playbook profile.yaml 
```



## 7.启动Hadoop

### 7.1格式化namenode

```bash
hadoop namenode -format
```

### 7.2启动hadoop

```bash
./start-dfs.sh
```

### 7.3出现“Attempting to operate on hdfs namenode as root”的解决办法

1、master，slave都需要修改start-dfs.sh，stop-dfs.sh，start-yarn.sh，stop-yarn.sh四个文件
2、如果你的Hadoop是另外启用其它用户来启动，记得将root改为对应用户

HDFS格式化后启动dfs出现以下错误：

```bash
[root@master sbin]# ./start-dfs.shStarting namenodes on [master]ERROR: Attempting to operate on hdfs namenode as rootERROR: but there is no HDFS_NAMENODE_USER defined. Aborting operation.Starting datanodesERROR: Attempting to operate on hdfs datanode as rootERROR: but there is no HDFS_DATANODE_USER defined. Aborting operation.Starting secondary namenodes [slave1]ERROR: Attempting to operate on hdfs secondarynamenode as rootERROR: but there is no HDFS_SECONDARYNAMENODE_USER defined. Aborting operation.1234567891011
```

解决办法：

在/hadoop/sbin路径下：
将start-dfs.sh，stop-dfs.sh两个文件顶部添加以下参数

```bash
#!/usr/bin/env bash
HDFS_DATANODE_USER=root
HADOOP_SECURE_DN_USER=hdfs
HDFS_NAMENODE_USER=root
HDFS_SECONDARYNAMENODE_USER=root
```

还有，start-yarn.sh，stop-yarn.sh顶部也需添加以下：

```bash
#!/usr/bin/envbash
YARN_RESOURCEMANAGER_USER=root
HADOOP_SECURE_DN_USER=yarn
YARN_NODEMANAGER_USER=root
```

修改好后重新分发到每个slave:

```bash
ansible-playbook profile.yaml 
```



### 7.4启动hadoop，报错Error JAVA_HOME is not set and could not be found

解决方案参考：[https://blog.csdn.net/chun19920827/article/details/74924434](https://blog.csdn.net/chun19920827/article/details/74924434)

### 7.5启动集群

**如下方式启动会容易出现问题！！！！！！！！！！：**

```bash
cd $HADOOP_HOME/sbinstart-dfs.shstart-yarn.sh
#注意，jobhistory需要用Start historyServer命令起动
mr-jobhistory-daemon.sh start historyserver
```

#### 8.5.1解决“java.net.BindException: Port in use: hadoop2:8088”的问题

当是使用以下命令启动集群出现错误，查看日志发现： 

```bash
java.net.BindException: Port in use: hadoop2:8088        at org.apache.hadoop.http.HttpServer2.constructBindException(HttpServer2.java:1213)        at org.apache.hadoop.http.HttpServer2.bindForSinglePort(HttpServer2.java:1235)        at org.apache.hadoop.http.HttpServer2.openListeners(HttpServer2.java:1294)        at org.apache.hadoop.http.HttpServer2.start(HttpServer2.java:1149)        at org.apache.hadoop.yarn.webapp.WebApps$Builder.start(WebApps.java:439)        at org.apache.hadoop.yarn.server.resourcemanager.ResourceManager.startWepApp(ResourceManager.java:1203)        at org.apache.hadoop.yarn.server.resourcemanager.ResourceManager.serviceStart(ResourceManager.java:1312)        at org.apache.hadoop.service.AbstractService.start(AbstractService.java:194)        at org.apache.hadoop.yarn.server.resourcemanager.ResourceManager.main(ResourceManager.java:1507)Caused by: java.net.BindException: 无法指定被请求的地址        at sun.nio.ch.Net.bind0(Native Method)        at sun.nio.ch.Net.bind(Net.java:433)        at sun.nio.ch.Net.bind(Net.java:425)        at sun.nio.ch.ServerSocketChannelImpl.bind(ServerSocketChannelImpl.java:223)        at sun.nio.ch.ServerSocketAdaptor.bind(ServerSocketAdaptor.java:74)        at org.eclipse.jetty.server.ServerConnector.openAcceptChannel(ServerConnector.java:351)                                                                                                                 
```

去网上查的时候发现大部分都是netstat grep一下自己的端口号是否被占用什么的，查完之后发现并没有。

直到最近发现问题，在我的yarn-site.xml文件中配置的

```bash
    <!-- 指定ResourceManager的地址-->   
    <property>        
    <name>yarn.resourcemanager.hostname</name>        
    <value>hadoop2</value>    
    </property>
```

才明白，启动yarn的时候必须在你这个resoucemanager的地址才可以。由于以前懒省事，想在一台机子上直接start-all.sh。但是最近考虑到负载均衡问题，所以要手动配置一下resourcemanager和namenode的地址。而start-all.sh = （在一台机子上）start-dfs.sh + start-yarn.sh 这样很容易导致负载不均衡.

根据搭建时候集群的分布情况：

|      | hadoop-master         | hadoop1                        | hadoop2                         | hadoop3           |
| ---- | --------------------- | ------------------------------ | ------------------------------- | ----------------- |
| HDFS | **nameNode**,datanode | **SecondaryNameNode**,DataNode | DataNode                        | DataNode          |
| YARN | NodeManager           | NodeManager                    | **ResourceManager,NodeManager** | **historyServer** |

可知要在hadoop-master中启动：start-dfs.sh脚本，在hadoop2中启动start-yarn.sh脚本，在hadoo3中启动mr-jobhistory-daemon.sh脚本

创建一个脚本：

```bash
vi run-hadoop.sh
```



```bash
#!/bin/bash
if [ $# -lt 1 ]
then
    echo "No Args Input..."
    exit ;
fi
case $1 in
"start")
        echo " =================== 启动 hadoop集群 ==================="
#路径要是自己的路径才可以 用户名别忘了修改！
#以下都根据自己的实际路径来配置
        echo " --------------- 启动 hdfs ---------------"
         /hadoop/opt/hadoop-3.3.1/sbin/start-dfs.sh
        echo " --------------- 启动 yarn ---------------"
        ssh root@hadoop2 "/hadoop/opt/hadoop-3.3.1/sbin/start-yarn.sh"
        #historyserver
        echo " --------------- 启动 historyserver ---------------"
        ssh root@hadoop3 "/hadoop/opt/hadoop-3.3.1/sbin/mr-jobhistory-daemon.sh start historyserver"
;;
"stop")
        echo " ==============jps===== 关闭 hadoop集群 ==================="

        echo " --------------- 关闭 historyserver ---------------"
        ssh root@hadoop3 "/hadoop/opt/hadoop-3.3.1/sbin/mr-jobhistory-daemon.sh stop historyserver"
        echo " --------------- 关闭 yarn ---------------"
        ssh root@hadoop2 "/hadoop/opt/hadoop-3.3.1/sbin/stop-yarn.sh"
        echo " --------------- 关闭 hdfs ---------------"
        /hadoop/opt/hadoop-3.3.1/sbin/stop-dfs.sh
;;
*)
    echo "Input Args Error..."
;;
esac
```

```bash
# 赋予脚本执行权限
chmod +x run-hadoop.sh
```

```bash
# 开启集群
./run-hadoop.sh  start
```

```bash
# 关闭集群
./run-hadoop.sh  stop
```



### 7.6访问（其中host是你的主机ip地址，端口需要在服务器的安全规则组中开放）

```bash
http://host:9870   # NameNode
http://host:8042   # ResourceManager  NodeManager
http://host:19888  # jobhistory
http://host:9868 # namesecondary
```

![image-20211204165844422](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/20211204165845.png)



![image-20211204165858471](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/20211204165858.png)

![image-20211204165951165](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/20211204165951.png)

![image-20211204170239889](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/20211204170240.png)

## 8.测试hadoo是否搭建正确

### 8.1测试创建hadoop目录

命令可参考：[Hadoop Shell命令](https://hadoop.apache.org/docs/r1.0.4/cn/hdfs_shell.html)

1.在hdfs中创建一个目录

```bash
hadoop fs -mkdir -p /output/
```

该命令相当于在Linux中的：

```bash
mkdir -p /output/
```



2.查看hdfs中的文件

```bash
hadoop fs  -ls /
```

相当于在linux中的：

```bash
ls  /
```

![image-20211201170709660](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/20211201170709.png)

### 8.2测试上传文件

#### 8.2.1创建一个test-wordcout.txt

```bash
hello, I'm  moyisuiying,nice to meet youhello, I'm niu dehui,nice to meet youhello, I'm niu heng,nice to meet youhello, I'm niu nahai,nice to meet you
```

#### 8.2.2将该文件上传到hdfs中的/data/wordcount目录下

> put使用方法：hadoop fs -put <localsrc> ... <dst>
>
> 从本地文件系统中复制单个或多个源路径到目标文件系统。也支持从标准输入中读取输入写入目标文件系统。
>
> - hadoop fs -put localfile /user/hadoop/hadoopfile
> - hadoop fs -put localfile1 localfile2 /user/hadoop/hadoopdir
> - hadoop fs -put localfile hdfs://host:port/hadoop/hadoopfile
> - hadoop fs -put - hdfs://host:port/hadoop/hadoopfile
>   从标准输入中读取输入。
>
> 返回值：
>
> 成功返回0，失败返回-1。

```bash
[root@hadoop-master test]# hadoop fs -put test-wordcout.txt  /data/wordcount[root@hadoop-master test]# hadoop fs -ls /data/wordcount
```

![image-20211201171943983](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/20211201171944.png)

![image-20211201172306917](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/20211201172307.png)



### 8.3运行wordcount例子统计test-wordcout.txt词频

```bash
 hadoop jar /hadoop/opt/hadoop-3.3.1/share/hadoop/mapreduce/hadoop-mapreduce-examples-3.3.1.jar  wordcount test-wordcout.txt /output/wordcount
```

出现" Could not find or load main class org.apache.hadoop.mapreduce.v2.app.MRAppMaster"的问题：

```bash
[2021-12-04 12:02:51.115]Container exited with a non-zero exit code 1. Error file: prelaunch.err.Last 4096 bytes of prelaunch.err :Last 4096 bytes of stderr :Error: Could not find or load main class org.apache.hadoop.mapreduce.v2.app.MRAppMasterCaused by: java.lang.ClassNotFoundException: org.apache.hadoop.mapreduce.v2.app.MRAppMasterPlease check whether your <HADOOP_HOME>/etc/hadoop/mapred-site.xml contains the below configuration:<property>  <name>yarn.app.mapreduce.am.env</name>  <value>HADOOP_MAPRED_HOME=${full path of your hadoop distribution directory}</value></property><property>  <name>mapreduce.map.env</name>  <value>HADOOP_MAPRED_HOME=${full path of your hadoop distribution directory}</value></property><property>  <name>mapreduce.reduce.env</name>  <value>HADOOP_MAPRED_HOME=${full path of your hadoop distribution directory}</value></property>[2021-12-04 12:02:51.119]Container exited with a non-zero exit code 1. Error file: prelaunch.err.Last 4096 bytes of prelaunch.err :Last 4096 bytes of stderr :Error: Could not find or load main class org.apache.hadoop.mapreduce.v2.app.MRAppMasterCaused by: java.lang.ClassNotFoundException: org.apache.hadoop.mapreduce.v2.app.MRAppMasterPlease check whether your <HADOOP_HOME>/etc/hadoop/mapred-site.xml contains the below configuration:<property>  <name>yarn.app.mapreduce.am.env</name>  <value>HADOOP_MAPRED_HOME=${full path of your hadoop distribution directory}</value></property><property>  <name>mapreduce.map.env</name>  <value>HADOOP_MAPRED_HOME=${full path of your hadoop distribution directory}</value></property><property>  <name>mapreduce.reduce.env</name>  <value>HADOOP_MAPRED_HOME=${full path of your hadoop distribution directory}</value></property>
```

根据提示，找到hadoop安装目录下$HADOOP_HOME/etc/hadoop/mapred-site.xml,增加以下代码：

```bash
<property>  <name>yarn.app.mapreduce.am.env</name>  <value>HADOOP_MAPRED_HOME=${HADOOP_HOME}</value></property><property>  <name>mapreduce.map.env</name>  <value>HADOOP_MAPRED_HOME=${HADOOP_HOME}</value></property><property>  <name>mapreduce.reduce.env</name>  <value>HADOOP_MAPRED_HOME=${HADOOP_HOME}</value></property>
```



![image-20211204202023093](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/20211204202023.png)

