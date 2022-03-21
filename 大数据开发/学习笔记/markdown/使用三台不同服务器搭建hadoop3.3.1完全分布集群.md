# 使用三台不同服务器搭建hadoop3.3.1完全分布集群



## 1.环境准备

**服务器**

> 腾讯云服务器2核4G  ubuntu18.0.4，用于当hadoop集群的master
>
> 百度云服务器1核2G   ubuntu18.0.4，用于当hadoop集群的slave1
>
> 青云服务器1核2G   ubuntu18.0.4，用于当hadoop集群的slave2

搭建集群的分布情况：

|      | hadoop-master                           | hadoop-slave1                  | hadoop-slave2                   |
| ---- | --------------------------------------- | ------------------------------ | ------------------------------- |
| HDFS | **nameNode**,**historyServer**,datanode | **SecondaryNameNode**,DataNode | DataNode                        |
| YARN | NodeManager                             | NodeManager                    | **ResourceManager,NodeManager** |



**软件**

> hadoop-3.3.1.tar.gz
>
> jdk-11.0.1_linux-x64_bin.tar.gz

## 2.安装ssh并实现服务器间两两免密登录

### 2.1两台服务器首先各自安装OpenSSH

```bash
#1.安装ssh服务端
sudo apt-get install openssh-server；
#确认sshserver是否启动了
ps -e | grep ssh
#如果只有ssh-agent那ssh-server还没有启动，需要/etc/init.d/ssh start，如果看到sshd那说明ssh-server已经启动了
#启动sshserver
/etc/init.d/ssh start
#SSH配置（如果需要）修改配置文件/etc/ssh/sshd_config，这里可以定义SSH的服务端口，默认端口是22，你可以自己定义成其他端口号如,然后重启服务
#重启sshserver
/etc/init.d/ssh restart
# 安装客户端
apt-get install ssh
#如果安装失败，则使用下面命令进行安装
apt-get install openssh-client

```

### 2.2生成公钥秘钥

```bash
ssh-keygen -t rsa
```

并一路回车，完成之后会生成~/.ssh目录，目录下有id_rsa（私钥文件）和id_rsa.pub（公钥文件），再将id_rsa.pub重定向到文件authorized_keys

```bash
#生成公钥秘钥一路回车
ssh-keygen -t rsa
```

然后将每台服务器的公钥~/.ssh/id_rsa.pub的内容统一复制到同一个本地文件中authorized_keys（自己在本地中先创建一个文件authorized_keys，这个文件就拥有了所有服务器的公钥，不需要有后缀名），然后再把这个authorized_keys上传到每台服务器的 ~/.ssh/目录下

![image-20211205182004432](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/20211205182005.png)

这时候 ~/.ssh/authorized_keys就有三个公钥的字符串了，三台的服务器都有这个相同authorized_keys。

之后在每台服务器中使用命令：

```bash
ssh 用户名@ip
```

就可以直接两两免密登录了

## 3.master服务器安装Ansible用于分发文件

### 3.1master服务器安装Ansible

```bash
#首先安装python
apt-get install python3.8
#安装Ansible，这样的话ansible会被安装到/etc/ansible目录下
apt-get  -y install ansible
```

### 3.2修改ansible的hosts文件

```bash
vim /etc/ansible/hosts
```

加入以下内容： 

```bash
[hadoop]
hadoop-master
hadoop-slave1
hadoop-slave2
[master]
hadoop-master
[slaves]
hadoop-slave1
hadoop-slave2
```



### 3.3修改/etc/hosts

```bash
vim /etc/hosts
```

```
你本机的ip（内网）  localhost
::1 hadoop-master 你的hostname
::1 localhost.localdomain localhost
::1 localhost6.localdomain6 localhost6
你本机的ip（内网） 你的hostname
cluster1的外网ip） cluster1的hostname
cluster2的外网ip） cluster2的hostname
```



加入以下内容：

master的/etc/hosts

```
10.0.16.5 localhost
::1 hadoop-master hadoop-master
::1 localhost.localdomain localhost
::1 localhost6.localdomain6 localhost6
10.0.16.5  hadoop-master
180.76.164.125  hadoop-slave1
139.198.109.98  hadoop-slave2
```





hadoop-slave1的/etc/hosts:

```
192.168.0.4 localhost
::1 hadoop-slave1  hadoop-slave1
::1 localhost.localdomain localhost
::1 localhost6.localdomain6 localhost6
111.229.11.173  hadoop-master
192.168.0.4  hadoop-slave1
139.198.109.98  hadoop-slave2
```

hadoop-slave2的/etc/hosts:



```
10.150.19.45 localhost
::1 hadoop-slave2 hadoop-slave2
::1 localhost.localdomain localhost
::1 localhost6.localdomain6 localhost6
111.229.11.173 hadoop-master
180.76.164.125  hadoop-slave1
10.150.19.45  hadoop-slave2
```















```bash
master服务器ip   hadoop-master
slave1服务器ip   hadoop-slave1
slave2服务器的ip  hadoop-slave2
```

## 4.使用ansible将文件从master发送到各个slave

### 4.1将hadoop3.3.1和jdk11的压缩包发送到各个slave

#### 4.1.1编写ansible的发送脚本

```bash
vim hadoop-jdk.yaml
```

加入以下内容：

```yaml
---
- hosts: hadoop
  tasks:
    - name: creat thme same dir in slaves 
      path: /b
    - name: copy hadoop-3.3.1-src.tar.gz  to slaves
      unarchive: src=/bigdata/hadoop/buildhadoop/hadoop-3.3.1.tar.gz  dest=/bigdata/hadoop/buildhadoop
    - name: copy jdk to slaves
      unarchive: src=/bigdata/hadoop/buildhadoop/jdk-11.0.1_linux-x64_bin.tar.gz   dest=/bigdata/hadoop/buildhadoop
    

```

#### 4.1.2使用ansible命令开始从master中发送到各个slave

```bash
ansible-playbook hadoop-jdk.yaml
```

![image-20211205231843322](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/20211205231843.png)

### 4.2配置master的hadoop和Java环境变量

#### 4.2.1首先配置master的java和hadoop的环境变量

```bash
vim /etc/profile
```

在末尾加入以下配置：

```bash
# hadoop
export HADOOP_HOME=/bigdata/hadoop/buildhadoop/hadoop-3.3.1
export PATH=$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$PATH
#java
export JAVA_HOME=/bigdata/hadoop/buildhadoop/jdk-11.0.1
export PATH=$PATH:$JAVA_HOME/bin
```

修改好后退出，执行命令：

```bash
# 使修改的配置生效
source /etc/profile
```

![image-20211206001002753](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/20211206001002.png)



#### 4.2.2书写ansible脚本将修改后的/etc/profile和/etc/hosts文件从master分发到各个slave

```bash
vim   send-profileto-slaves.yaml
```

加入以下内容：

```bash
---
- hosts: hadoop
  tasks:
    - name: copy profile to slaves
      copy: src=/etc/profile dest=/etc/
      notify:
       - exec sourceprofile
    - name: copy bashrc to slaves
      copy: src=~/.bashrc dest=~/
      notify:
       - exec sourcebashrc
  handlers:
    - name: exec sourceprofile
      shell: source  /etc/profile 
    - name: exec sourcebashrc
      shell: source ~/.bashrc
```

分发成功后各个slave的Java和hadoop环境都会自动生效。

## 5.修改hadoop的配置文件

搭建集群的分布情况：

|      | hadoop-master                           | hadoop-s.rlave1                | hadoop-slave2                   |
| ---- | --------------------------------------- | ------------------------------ | ------------------------------- |
| HDFS | **nameNode**,**historyServer**,datanode | **SecondaryNameNode**,DataNode | DataNode                        |
| YARN | NodeManager                             | NodeManager                    | **ResourceManager,NodeManager** |

首先进入hadoop-3.3.1的根目录：

![image-20211206002530374](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/20211206002530.png)

首先进入 /bigdata/hadoop/buildhadoop/hadoop-3.3.1/etc/hadoop/目录：

```bash
cd /bigdata/hadoop/buildhadoop/hadoop-3.3.1/etc/hadoop
```

![image-20211206002657022](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/20211206002657.png)

### 5.1修改works文件

```bash
hadoop-master
hadoop-slave1
hadoop-slave2
```



### 5.2修改core-site.xml文件

```xml
<configuration>
<property>
        <name>hadoop.tmp.dir</name>
        <value>/bigdata/hadoop/tmp</value>
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
    </property>
</configuration>

```



### 5.3修改hdfs-site.xml文件

```xml
<configuration>
<!-- 指定secondary的访问地址和端口 -->
 <property>
<name>dfs.namenode.secondary.http-address</name>
<value>hadoop-slave1:9868</value>
</property>
<!-- 指定namenode的访问地址和端口 -->
<property>
        <name>dfs.namenode.http-address</name>
        <value>hadoop-master:9870</value>
</property>
<property>
  <name>dfs.namenode.name.dir</name>
  <value>${hadoop.tmp.dir}/dfs/name</value>
</property>

 <property>
  <name>dfs.datanode.data.dir</name>
  <value>${hadoop.tmp.dir}/dfs/data</value>
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
 <!-- 如果是通过公网IP访问内网搭建的集群 -->
<property>
    <name>dfs.client.use.datanode.hostname</name>
    <value>true</value>
    <description>only cofig in clients</description>
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

### 5.4修改yarn-site.xml文件

```xml
<configuration>
    <property>
   <name>yarn.resourcemanager.hostname</name>
   <value>hadoop-slave2</value>
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
   <value>/bigdata/hadoop/logs/remoteApiLogs</value>
 </property>
<property>
   <name>yarn.nodemanager.remote-app-log-dir-suffix</name>
   <value>logs</value>
 </property>
</configuration>

```

### 5.5修改mapred-site.xml文件

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
  <value>hadoop-master:10020</value>
</property>
<property>
  <name>mapreduce.jobhistory.webapp.address</name>
  <value>hadoop-master:19888</value>
</property>
<property>
  <name>mapreduce.jobhistory.done-dir</name>
  <value>/bigdata/hadoop/jobhistory/done</value>
</property>
<property>
  <name>mapreduce.intermediate-done-dir</name>
  <value>/bigdata/hadoop/jobhisotry/done_intermediate</value>
</property>
<property>
  <name>mapreduce.job.ubertask.enable</name>
  <value>true</value>
</property>
</configuration>

```

### 5.6修改hadoop-env.sh

只需要指定 java和hadoop的根目录即可

```bash
export JAVA_HOME=/bigdata/hadoop/buildhadoop/jdk-11.0.1
export HADOOP_HOME=/bigdata/hadoop/buildhadoop/hadoop-3.3.1

```

### 5.7修改hdfs-start.sh和hdfs-stop.sh

在/hadoop3.3.1/sbin路径下：
将start-dfs.sh，stop-dfs.sh两个文件顶部添加以下参数

```bash
#!/usr/bin/env bash
HDFS_DATANODE_USER=root
HADOOP_SECURE_DN_USER=hdfs
HDFS_NAMENODE_USER=root
HDFS_SECONDARYNAMENODE_USER=root

```

### 5.8修改yarn-start.sh和yarn-stop.sh

在/hadoop3.3.1/sbin路径下 start-yarn.sh，stop-yarn.sh顶部也需添加以下：

```bash
#!/usr/bin/env bash
YARN_RESOURCEMANAGER_USER=root
HADOOP_SECURE_DN_USER=yarn
YARN_NODEMANAGER_USER=root
```

### 5.9将master中修改好的hadoop配置文件分发到slaves

#### 5.9.1编写 分发脚本send-hadoopconfigfile-to-slaves.yaml

```bash
vim  send-hadoopconfigfile-to-slaves.yaml

```

加入以下内容：

```bash
---
- hosts: hadoop
  tasks:
    - name: copy hadoop3.3.1/etc/hadoop  to slaves
      copy: src=/bigdata/hadoop/buildhadoop/hadoop-3.3.1/etc/hadoop  dest=/bigdata/hadoop/buildhadoop/hadoop-3.3.1/etc/
    - name: copy hadoop3.3.1/sbin   to slaves
      copy: src=/bigdata/hadoop/buildhadoop/hadoop-3.3.1/sbin   dest=/bigdata/hadoop/buildhadoop/hadoop-3.3.1/

```

#### 5.9.2执行分发脚本开始分发到slaves

```bash
ansible-playbook send-hadoopconfigfile-to-slaves.yaml
```

分发成功后集群的所有配置都一致了。

## 6.启动集群

搭建集群的分布情况：

|      | hadoop-master                           | hadoop-slave1                  | hadoop-slave2                   |
| ---- | --------------------------------------- | ------------------------------ | ------------------------------- |
| HDFS | **nameNode**,**historyServer**,datanode | **SecondaryNameNode**,DataNode | DataNode                        |
| YARN | NodeManager                             | NodeManager                    | **ResourceManager,NodeManager** |

### 6.1格式化namenode

```bash
hadoop namenode -format
```

### 

### 6.2如下方式启动会容易出现问题！！！！！！！！！！：

```bash
cd $HADOOP_HOME/sbin
start-dfs.sh
start-yarn.sh
#注意，jobhistory需要用Start historyServer命令起动
mr-jobhistory-daemon.sh start historyserver
```

### 6.3解决“java.net.BindException: Port in use: hadoop2:8088”的问题

当是使用以下命令启动集群出现错误，查看日志发现： 

```bash
java.net.BindException: Port in use: hadoop2:8088
        at org.apache.hadoop.http.HttpServer2.constructBindException(HttpServer2.java:1213)
        at org.apache.hadoop.http.HttpServer2.bindForSinglePort(HttpServer2.java:1235)
        at org.apache.hadoop.http.HttpServer2.openListeners(HttpServer2.java:1294)
        at org.apache.hadoop.http.HttpServer2.start(HttpServer2.java:1149)
        at org.apache.hadoop.yarn.webapp.WebApps$Builder.start(WebApps.java:439)
        at org.apache.hadoop.yarn.server.resourcemanager.ResourceManager.startWepApp(ResourceManager.java:1203)
        at org.apache.hadoop.yarn.server.resourcemanager.ResourceManager.serviceStart(ResourceManager.java:1312)
        at org.apache.hadoop.service.AbstractService.start(AbstractService.java:194)
        at org.apache.hadoop.yarn.server.resourcemanager.ResourceManager.main(ResourceManager.java:1507)
Caused by: java.net.BindException: 无法指定被请求的地址
        at sun.nio.ch.Net.bind0(Native Method)
        at sun.nio.ch.Net.bind(Net.java:433)
        at sun.nio.ch.Net.bind(Net.java:425)
        at sun.nio.ch.ServerSocketChannelImpl.bind(ServerSocketChannelImpl.java:223)
        at sun.nio.ch.ServerSocketAdaptor.bind(ServerSocketAdaptor.java:74)
        at org.eclipse.jetty.server.ServerConnector.openAcceptChannel(ServerConnector.java:351)
                                                                                                                 
```

去网上查的时候发现大部分都是netstat grep一下自己的端口号是否被占用什么的，查完之后发现并没有。

直到最近发现问题，在我的yarn-site.xml文件中配置的

```bash
    <!-- 指定ResourceManager的地址-->
    <property>
        <name>yarn.resourcemanager.hostname</name>
        <value>hadoop-slave2</value>
    </property>
```

才明白，启动yarn的时候必须在你这个resoucemanager的地址才可以。由于以前懒省事，想在一台机子上直接start-all.sh。但是最近考虑到负载均衡问题，所以要手动配置一下resourcemanager和namenode的地址。而start-all.sh = （在一台机子上）start-dfs.sh + start-yarn.sh 这样很容易导致负载不均衡.

根据搭建时候集群的分布情况：

|      | hadoop-master                           | hadoop-slave1                  | hadoop-slave2                   |
| ---- | --------------------------------------- | ------------------------------ | ------------------------------- |
| HDFS | **nameNode**,**historyServer**,datanode | **SecondaryNameNode**,DataNode | DataNode                        |
| YARN | NodeManager                             | NodeManager                    | **ResourceManager,NodeManager** |

可知要在hadoop-master中启动：start-dfs.sh脚本，在hadoop-slave2中启动start-yarn.sh脚本，在hadoo-master中启动mr-jobhistory-daemon.sh脚本



### 6.4正确的启动集群的方法

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
         /bigdata/hadoop/buildhadoop/hadoop-3.3.1/sbin/start-dfs.sh
         echo " --------------- 启动 historyserver ---------------"
         /bigdata/hadoop/buildhadoop/hadoop-3.3.1/sbin/mr-jobhistory-daemon.sh start historyserver
        echo " --------------- 启动 yarn ---------------"
        ssh root@hadoop-slave2 "/bigdata/hadoop/buildhadoop/hadoop-3.3.1/sbin/start-yarn.sh"
;;
"stop")
        echo " =================== 关闭 hadoop集群 ==================="
        echo " --------------- 关闭 yarn ---------------"
        ssh root@hadoop-slave2 "/bigdata/hadoop/buildhadoop/hadoop-3.3.1/sbin/stop-yarn.sh"
         echo " --------------- 关闭 historyserver ---------------"
        /bigdata/hadoop/buildhadoop/hadoop-3.3.1/sbin/mr-jobhistory-daemon.sh stop historyserver
        echo " --------------- 关闭 hdfs ---------------"
        /bigdata/hadoop/buildhadoop/hadoop-3.3.1/sbin/stop-dfs.sh
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

### 6.5启动集群出现“port in use”的问题以及解决方案

#### 6.5.1namenode启动失败

查看hadoop3.3.1/logs/hadoop-root-namenode-VM-16-5-ubuntu.log日志发现有如下提示：

```xml
2021-12-07 16:19:49,459 INFO org.apache.hadoop.http.HttpServer2: HttpServer.start() threw a non Bind IOException
java.net.BindException: Port in use: hadoop-master:9870
        at org.apache.hadoop.http.HttpServer2.constructBindException(HttpServer2.java:1344)
        at org.apache.hadoop.http.HttpServer2.bindForSinglePort(HttpServer2.java:1366)
        at org.apache.hadoop.http.HttpServer2.openListeners(HttpServer2.java:1429)
        at org.apache.hadoop.http.HttpServer2.start(HttpServer2.java:1275)
        at org.apache.hadoop.hdfs.server.namenode.NameNodeHttpServer.start(NameNodeHttpServer.java:170)
        at org.apache.hadoop.hdfs.server.namenode.NameNode.startHttpServer(NameNode.java:950)
        at org.apache.hadoop.hdfs.server.namenode.NameNode.initialize(NameNode.java:761)
        at org.apache.hadoop.hdfs.server.namenode.NameNode.<init>(NameNode.java:1018)
        at org.apache.hadoop.hdfs.server.namenode.NameNode.<init>(NameNode.java:991)
        at org.apache.hadoop.hdfs.server.namenode.NameNode.createNameNode(NameNode.java:1767)
        at org.apache.hadoop.hdfs.server.namenode.NameNode.main(NameNode.java:1832)
Caused by: java.io.IOException: Failed to bind to hadoop-master:9870
        at org.eclipse.jetty.server.ServerConnector.openAcceptChannel(ServerConnector.java:349)
        at org.eclipse.jetty.server.ServerConnector.open(ServerConnector.java:310)
        at org.apache.hadoop.http.HttpServer2.bindListener(HttpServer2.java:1331)

          
```

**解决方案**：**将namenode所在的主机master的/etc/hosts中的master的公网IP替换为内网ip即可**,**而slave1和slave2服务器上的/etc/hosts则使用的是master的公网ip**

```bash
master内网ip  hadoop-master
slave1公网IP  hadoop-slave1
slave2公网IP   hadoop-slave2
```

#### 6.5.2ResourceManager和NodeManager启动失败

查看/bigdata/hadoop/buildhadoop/hadoop-3.3.1/logs/ hadoop-root-resourcemanager-i-e8lf0fjm.log日志发现有报错如下：

```bash
2021-12-07 16:28:12,432 INFO org.apache.hadoop.yarn.webapp.WebApps: Registered webapp guice modules
2021-12-07 16:28:12,497 INFO org.apache.hadoop.http.HttpServer2: HttpServer.start() threw a non Bind IOException
java.net.BindException: Port in use: hadoop-slave2:8088
        at org.apache.hadoop.http.HttpServer2.constructBindException(HttpServer2.java:1344)
        at org.apache.hadoop.http.HttpServer2.bindForSinglePort(HttpServer2.java:1366)
        at org.apache.hadoop.http.HttpServer2.openListeners(HttpServer2.java:1429)

```

查看/bigdata/hadoop/buildhadoop/hadoop-3.3.1/logs/hadoop-root-nodemanager-i-e8lf0fjm.log日志发现有如下报错：

```
2021-12-07 16:28:18,730 INFO org.apache.hadoop.yarn.webapp.WebApps: Registered webapp guice modules
2021-12-07 16:28:18,737 INFO org.apache.hadoop.http.HttpServer2: HttpServer.start() threw a non Bind IOException
java.net.BindException: Port in use: hadoop-slave2:8042
        at org.apache.hadoop.http.HttpServer2.constructBindException(HttpServer2.java:1344)
        at org.apache.hadoop.http.HttpServer2.bindForSinglePort(HttpServer2.java:1366)
        at org.apache.hadoop.http.HttpServer2.openListeners(HttpServer2.java:1429)
        at org.apache.hadoop.http.HttpServer2.start(HttpServer2.java:1275)
        at org.apache.hadoop.yarn.webapp.WebApps$Builder.start(WebApps.java:472)
        at org.apache.hadoop.yarn.webapp.WebApps$Builder.start(WebApps.java:461)
        at org.apache.hadoop.yarn.server.nodemanager.webapp.WebServer.serviceStart(WebServer.java:125)
        at org.apache.hadoop.service.AbstractService.start(AbstractService.java:
```



而ResourceManager和NodeManager都是安装在hadoop-slave上，所以需要在hadoop-slave2所在的服务器上修改/etc/hosts文件。

**解决方案：将ResourceManager和NodeManager所在的主机hadoop-slave2的/etc/hosts中的slave2的公网IP替换为内网ip即可**,而slave1和master服务器上的/etc/hosts则使用的是公网ip

```bash
master公网ip  hadoop-master
slave1公网IP  hadoop-slave1
slave2内网ip   hadoop-slave2
```

#### 6.5.3总结

如果配置为公网IP地址，就会出现集群启动不了，namenode和secondarynamenode启动不了，如果将主机的映射文件配置为内网IP集群就可以正常启动了。但通过idea开发工具访问会出错，使用服务器的内网的ip地址来访问datanode，这肯定访问不了啊，这问题真实醉了，就这样想了找了好久一致没有思路。

　　最终发现需要在hdfs-site.xml中修改配置项dfs.client.use.datanode.hostname设置为true，就是说客户端访问datanode的时候是通过主机域名访问，就不会出现通过内网IP来访问了

参考资料：

 [Hadoop启动时遇见Port in use: master:50070 和Cannot assign requested address解决办法](https://blog.csdn.net/hhy1107786871/article/details/85246560)

[**阿里云搭建hadoop集群服务器，内网、外网访问问题（详解。。。）**](https://www.cnblogs.com/ya-qiang/p/10076424.html)
