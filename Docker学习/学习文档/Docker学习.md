

# Docker学习

## 1.Docker概述

### 1.1为什么会出现

 一款产品： 开发--->上线， 两套配置环境，应用配置。

开发--->运维。问题： 我的电脑上可以运行，但是版本更新后导致服务不可用！

环境配置是十分麻烦的，每个机器都要部署环境（集群Redis，ES，Hadoop），费时费力。

发布一个项目：jar（Redis，MySQL，jdk，es），项目不能带上环境安装打包。

之前在服务器配置一个应用的环境，Redis，MySQL，jdk，es，配置超麻烦，不能跨平台。比如Windows平台上开发，但是最后发布到Linux上。

传统：开发jar或者war，运维来做。

现在：开发打包，部署上线，一套流程搞定。

Docker 是一个开源的应用容器引擎，基于 [Go 语言](https://www.runoob.com/go/go-tutorial.html) 并遵从 Apache2.0 协议开源。

Docker 可以让开发者打包他们的应用以及依赖包到一个轻量级、可移植的容器中，然后发布到任何流行的 Linux 机器上，也可以实现虚拟化。

容器是完全使用沙箱机制，相互之间不会有任何接口（类似 iPhone 的 app）,更重要的是容器性能开销极低。

### 1.2Docker给以上问题给出了解决方案。

Java ---->apk--->发布（应用商店）----->使用者下载 ----->安装apk

Java------>jar（环境）---->打包项目带上环境------>(Docker仓库：商店)---------->下载我们发布的镜像------>开始使用。

网址：https://www.docker.com/

Docker的思想就是来自于集装箱。

JRE----->多个应用(端口冲突) ----->原来都是交叉的。

隔离：Docker的核心思想就是打包装箱，每个箱子都是互相隔离的。

### 1.3docker的历史

2010年，几个搞IT的年轻人在美国成立了一个公司dotcloud。做一些pass的云计算开发，LXC的有关容器技术。他们将自己的技术（容器化技术）命名就是Docker。Docker刚刚诞生的时候没有引起行业的注意！dotCloud就活不下去。

2013年，Docker开源。越来越多的人发现了Docker的优点。每个月都会更新一个版本。

2014年4月9日，Docker1.0发布。

Docker为啥那么火？十分轻巧。

在容纳器技术出来之前，我们都是使用虚拟机技术。

虚拟机：在Windows中安装一个VMware，通过这个软件我们可以虚拟出来一台或多台电脑，但是内存大，笨重。

虚拟机也是属于虚拟化技术，Docker容器技术也是一种虚拟化技术。

VMware：Linux centos 原生镜像（一台电脑），隔离需要开启多个虚拟机。几个G大小，几分钟启动。

Docker：隔离，镜像（最核心的环境4m +jdk+mysql）十分小巧，运行镜像就可以了。几个M大小，秒级启动。

Docker是基于go语言开发的开源项目。

Docker文档地址：https://docs.docker.com/ 很详细。

仓库地址：https://hub.docker.com/ 

### 1.4Docker的作用。

虚拟机技术：

①资源占用十分多。

②冗余步骤多。

③启动很慢。

容器化技术：

容器技术不是模拟一个完整的操作系统。



![image-20201106233153901](image\image-1.png)

比较Docker和虚拟机的不同：

①传统虚拟机，虚拟出一套硬件，运行一个完整的系统，然后在这个系统上安装和运行软件。

②容器内的应用直接运行在宿主机的内容，容器是没有自己的内核的，也没有虚拟我们的硬件，所以轻便。

③每个容器之剑都是相互隔离的，每个容器内都有属于一个自己的文件系统，互不影响。

> DevOps（开发，运维）

应用更快捷的交互和部署。

传统：一堆帮助文档，安装程序。

Docker：打包镜像发布测试，一键运行。

**更快捷的升级和扩容**

使用Docker后我们部署应用就像搭积木一样。

**更简单的系统运维**

在容器化后，我们的开发，测试环境都是高度一致的。

**更高效的计算资源利用**

Docker是内核级别的虚拟化，可以在一个武理机上运行很多容器实例。服务器的性能可以压榨到极致。

## 2.Docker的组成

### 2.1架构图

![image-20201106234341925](image\image-2.png)

**镜像（image）：**

好比是一个模板，可以通过这个模板来创建容器服务。

**容器（Container）：**

Docker利用容器技术，独立运行一个护着一组应用，通过镜像来创建的。

**仓库（repository）：**

存放镜像的地方。

### 2.2安装Docker

环境准备：

①需要会一点Linux

②一台服务器Linux系统

③使用xshell操作。

环境：

```
NAME="Ubuntu"
VERSION="18.04.4 LTS (Bionic Beaver)"
ID=ubuntu
ID_LIKE=debian
PRETTY_NAME="Ubuntu 18.04.4 LTS"
VERSION_ID="18.04"
HOME_URL="https://www.ubuntu.com/"
SUPPORT_URL="https://help.ubuntu.com/"
BUG_REPORT_URL="https://bugs.launchpad.net/ubuntu/"
PRIVACY_POLICY_URL="https://www.ubuntu.com/legal/terms-and-policies/privacy-policy"
VERSION_CODENAME=bionic
UBUNTU_CODENAME=bionic
```

![image-20201106235545667](image\image-33.png)

安装步骤：

①卸载之前的安装

```
$ sudo apt-get remove docker docker-engine docker.io containerd runc
```

②若想使用官网安装方法则移步至官网Ubuntu安装DockerCE方法

安装这些包来允许apt通过https使用存储库。

```
 sudo apt-get install \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg-agent \
    software-properties-common
```

③添加Docker的官方GPG密钥。

```
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
```

④通过搜索指纹的后8个字符，验证是否拥有带指纹的密钥 。

```
sudo apt-key fingerprint 0EBFCD88

# 结果类似如下：
pub   rsa4096 2017-02-22 [SCEA]
      9DC8 5822 9FC7 DD38 854A  E2D8 8D81 803C 0EBF CD88
uid           [ unknown] Docker Release (CE deb) 
sub   rsa4096 2017-02-22 [S]
```

⑤使用以下命令设置稳定存储库。

```
sudo add-apt-repository "deb [arch=amd64] http://mirrors.aliyun.com/docker-ce/linux/ubuntu $(lsb_release -cs) stable"
```

⑥再次重新同步包的索引文件。

```
sudo apt update
```

⑦安装最新版本Docker。

```
sudo apt-get  install docker-ce
```

⑧检验是否安装成功。

```
sudo docker version
```

```
Client: Docker Engine - Community
 Version:           19.03.13
 API version:       1.40
 Go version:        go1.13.15
 Git commit:        4484c46d9d
 Built:             Wed Sep 16 17:02:36 2020
 OS/Arch:           linux/amd64
 Experimental:      false

Server: Docker Engine - Community
 Engine:
  Version:          19.03.13
  API version:      1.40 (minimum version 1.12)
  Go version:       go1.13.15
  Git commit:       4484c46d9d
  Built:            Wed Sep 16 17:01:06 2020
  OS/Arch:          linux/amd64
  Experimental:     false
 containerd:
  Version:          1.3.7
  GitCommit:        8fba4e9a7d01810a393d5d25a3621dc101981175
 runc:
  Version:          1.0.0-rc10
  GitCommit:        dc9208a3303feef5b3839f4323d9beb36df0a9dd
 docker-init:
  Version:          0.18.0
  GitCommit:        fec3683

```

⑨镜像加速器（阿里云）

```
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://bk2vzbex.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```

### 2.3运行hello-world分析

![image-20201107102811341](image\image-4.png)

### 2.4Docker的的底层原理

Docker是一个client-Server结构的系统，Docker的守护进程运行在主机上，通过socker从客户端访问，DockerServer接收到DockerClient命令，然后执行这个命令。

![image-20201107103212005](image\image-5.png)

**Docker为啥比VMware快**

1.Docker有着比虚拟机更少的抽象层。

2.Docker利用的是宿主机的内核，而VMware需要的是GuestOs。所以新建一个容器的时候，Docker不需要像虚拟机一样重新加载一个操作系统内核，避免引导。而虚拟机是加载GuestOS，分钟级别启动，而Docker利用的是宿主机的操作系统，所以启动是秒级的。

![image-20201107103410238](image\image-6.png)

![image-20201107104313236](image\image-7.png)

## 3.Docker的常用命令

### 3.1常用命令

```
docker version
docker --help
docker info 
```

### 3.2镜像命令：docker images  #查看所有镜像

```
 docker images;
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
hello-world         latest              bf756fb1ae65        10 months ago       13.3kB
```

REPOSITORY：镜像的仓库

TAG：镜像标签。

IMAGE ID： 镜像的ID。

CREATED：创建的时间。

SIZE : 镜像大小。

可选项：

## Options

| Name, shorthand | Default | Description                                         |
| --------------- | ------- | --------------------------------------------------- |
| `--all , -a`    |         | Show all images (default hides intermediate images) |
| `--digests`     |         | Show digests                                        |
| `--filter , -f` |         | Filter output based on conditions provided          |
| `--format`      |         | Pretty-print images using a Go template             |
| `--no-trunc`    |         | Don’t truncate output                               |
| `--quiet , -q`  |         | Only show numeric IDs                               |

### 3.2搜索镜像：docker search [OPTIONS] TERM

**Options**

| Name, shorthand | Default | Description                                                  |
| --------------- | ------- | ------------------------------------------------------------ |
| `--automated`   |         | [**deprecated**](https://docs.docker.com/engine/deprecated/) Only show automated builds |
| `--filter , -f` |         | Filter output based on conditions provided                   |
| `--format`      |         | Pretty-print search using a Go template                      |
| `--limit`       | `25`    | Max number of search results                                 |
| `--no-trunc`    |         | Don’t truncate output                                        |
| `--stars , -s`  |         | [**deprecated**](https://docs.docker.com/engine/deprecated/) Only displays with at least x stars |

### 3.3拉取镜像 docker pull  镜像名[:tag]

```
docker pull mysql   #拉取镜像
Using default tag: latest  #默认拉取最新版
latest: Pulling from library/mysql 
bb79b6b2107f: Pull complete #分层下载，Docker image的核心，联合文件系统
49e22f6fb9f7: Pull complete 
842b1255668c: Pull complete 
9f48d1f43000: Pull complete 
c693f0615bce: Pull complete 
8a621b9dbed2: Pull complete 
0807d32aef13: Pull complete 
a56aca0feb17: Pull complete 
de9d45fd0f07: Pull complete 
1d68a49161cc: Pull complete 
d16d318b774e: Pull complete 
49e112c55976: Pull complete 
Digest: sha256:8c17271df53ee3b843d6e16d46cff13f22c9c04d6982eb15a9a47bd5c9ac7e2d #签名
Status: Downloaded newer image for mysql:latest 
docker.io/library/mysql:lates   #镜像的真是地址

#等价于
docker pull docker.io/library/mysql:latest

#指定版本下载
docker pull docker.io/library/mysql:5.7
```

### 3.4删除镜像  docker rmi -f   镜像ID  容器ID  

 

```
docker rmi -f 1b12f2e9257b   #根据容器ID删除
Untagged: mysql:5.7
Untagged: mysql@sha256:4d2b34e99c14edb99cdd95ddad4d9aa7ea3f2c4405ff0c3509a29dc40bcb10ef
Deleted: sha256:1b12f2e9257be96da5c075f186dc371b18442b0f9281728ac64c9a69c6e0e264
Deleted: sha256:b21d85dcc43f7db4e532c3ca07e5adbc1e02beb37646b7079217ea2b1922af91
Deleted: sha256:603c9faa831d58a3f60eb377cd6246a281a97b906406a92bf3452726a6f15b69
Deleted: sha256:2d927a6eb4cc26ba5454193a3f4abf503b6cea710293d4f0e6d061c08578b687
Deleted: sha256:b96c2c2176ca7b5223469ad39c9e17509505233dd81030878bc12b03074baef4
```

```
docker rmi -f $(docker images -aq) #删除所有ID对应的容器
```

```
docker rmi -f 容器ID 容器ID  #删除多个容器
```

### 3.5镜像操作的基本命令

#### 3.5.1run命令：docker run [OPTIONS] IMAGE [COMMAND] [ARG...]

```
#参数说明
--name ="容器名字"  #给运行的容器一个名字用于区分容器
-d      后台方式运行
-it    使用交互式方式运行，进入容器查看内容
-p     指定容器的端口
	-p  ip:主机端口：容器端口
	-p   主机端口：容器端口 (常用)
	-p   容器端口
	容器端口
-P  随机指定端口
```



## Options

| Name, shorthand           | Default   | Description                                                  |
| ------------------------- | --------- | ------------------------------------------------------------ |
| `--add-host`              |           | Add a custom host-to-IP mapping (host:ip)                    |
| `--attach , -a`           |           | Attach to STDIN, STDOUT or STDERR                            |
| `--blkio-weight`          |           | Block IO (relative weight), between 10 and 1000, or 0 to disable (default 0) |
| `--blkio-weight-device`   |           | Block IO weight (relative device weight)                     |
| `--cap-add`               |           | Add Linux capabilities                                       |
| `--cap-drop`              |           | Drop Linux capabilities                                      |
| `--cgroup-parent`         |           | Optional parent cgroup for the container                     |
| `--cidfile`               |           | Write the container ID to the file                           |
| `--cpu-count`             |           | CPU count (Windows only)                                     |
| `--cpu-percent`           |           | CPU percent (Windows only)                                   |
| `--cpu-period`            |           | Limit CPU CFS (Completely Fair Scheduler) period             |
| `--cpu-quota`             |           | Limit CPU CFS (Completely Fair Scheduler) quota              |
| `--cpu-rt-period`         |           | [**API 1.25+**](https://docs.docker.com/engine/api/v1.25/) Limit CPU real-time period in microseconds |
| `--cpu-rt-runtime`        |           | [**API 1.25+**](https://docs.docker.com/engine/api/v1.25/) Limit CPU real-time runtime in microseconds |
| `--cpu-shares , -c`       |           | CPU shares (relative weight)                                 |
| `--cpus`                  |           | [**API 1.25+**](https://docs.docker.com/engine/api/v1.25/) Number of CPUs |
| `--cpuset-cpus`           |           | CPUs in which to allow execution (0-3, 0,1)                  |
| `--cpuset-mems`           |           | MEMs in which to allow execution (0-3, 0,1)                  |
| `--detach , -d`           |           | Run container in background and print container ID           |
| `--detach-keys`           |           | Override the key sequence for detaching a container          |
| `--device`                |           | Add a host device to the container                           |
| `--device-cgroup-rule`    |           | Add a rule to the cgroup allowed devices list                |
| `--device-read-bps`       |           | Limit read rate (bytes per second) from a device             |
| `--device-read-iops`      |           | Limit read rate (IO per second) from a device                |
| `--device-write-bps`      |           | Limit write rate (bytes per second) to a device              |
| `--device-write-iops`     |           | Limit write rate (IO per second) to a device                 |
| `--disable-content-trust` | `true`    | Skip image verification                                      |
| `--dns`                   |           | Set custom DNS servers                                       |
| `--dns-opt`               |           | Set DNS options                                              |
| `--dns-option`            |           | Set DNS options                                              |
| `--dns-search`            |           | Set custom DNS search domains                                |
| `--domainname`            |           | Container NIS domain name                                    |
| `--entrypoint`            |           | Overwrite the default ENTRYPOINT of the image                |
| `--env , -e`              |           | Set environment variables                                    |
| `--env-file`              |           | Read in a file of environment variables                      |
| `--expose`                |           | Expose a port or a range of ports                            |
| `--gpus`                  |           | [**API 1.40+**](https://docs.docker.com/engine/api/v1.40/) GPU devices to add to the container (‘all’ to pass all GPUs) |
| `--group-add`             |           | Add additional groups to join                                |
| `--health-cmd`            |           | Command to run to check health                               |
| `--health-interval`       |           | Time between running the check (ms\|s\|m\|h) (default 0s)    |
| `--health-retries`        |           | Consecutive failures needed to report unhealthy              |
| `--health-start-period`   |           | [**API 1.29+**](https://docs.docker.com/engine/api/v1.29/) Start period for the container to initialize before starting health-retries countdown (ms\|s\|m\|h) (default 0s) |
| `--health-timeout`        |           | Maximum time to allow one check to run (ms\|s\|m\|h) (default 0s) |
| `--help`                  |           | Print usage                                                  |
| `--hostname , -h`         |           | Container host name                                          |
| `--init`                  |           | [**API 1.25+**](https://docs.docker.com/engine/api/v1.25/) Run an init inside the container that forwards signals and reaps processes |
| `--interactive , -i`      |           | Keep STDIN open even if not attached                         |
| `--io-maxbandwidth`       |           | Maximum IO bandwidth limit for the system drive (Windows only) |
| `--io-maxiops`            |           | Maximum IOps limit for the system drive (Windows only)       |
| `--ip`                    |           | IPv4 address (e.g., 172.30.100.104)                          |
| `--ip6`                   |           | IPv6 address (e.g., 2001:db8::33)                            |
| `--ipc`                   |           | IPC mode to use                                              |
| `--isolation`             |           | Container isolation technology                               |
| `--kernel-memory`         |           | Kernel memory limit                                          |
| `--label , -l`            |           | Set meta data on a container                                 |
| `--label-file`            |           | Read in a line delimited file of labels                      |
| `--link`                  |           | Add link to another container                                |
| `--link-local-ip`         |           | Container IPv4/IPv6 link-local addresses                     |
| `--log-driver`            |           | Logging driver for the container                             |
| `--log-opt`               |           | Log driver options                                           |
| `--mac-address`           |           | Container MAC address (e.g., 92:d0:c6:0a:29:33)              |
| `--memory , -m`           |           | Memory limit                                                 |
| `--memory-reservation`    |           | Memory soft limit                                            |
| `--memory-swap`           |           | Swap limit equal to memory plus swap: ‘-1’ to enable unlimited swap |
| `--memory-swappiness`     | `-1`      | Tune container memory swappiness (0 to 100)                  |
| `--mount`                 |           | Attach a filesystem mount to the container                   |
| `--name`                  |           | Assign a name to the container                               |
| `--net`                   |           | Connect a container to a network                             |
| `--net-alias`             |           | Add network-scoped alias for the container                   |
| `--network`               |           | Connect a container to a network                             |
| `--network-alias`         |           | Add network-scoped alias for the container                   |
| `--no-healthcheck`        |           | Disable any container-specified HEALTHCHECK                  |
| `--oom-kill-disable`      |           | Disable OOM Killer                                           |
| `--oom-score-adj`         |           | Tune host’s OOM preferences (-1000 to 1000)                  |
| `--pid`                   |           | PID namespace to use                                         |
| `--pids-limit`            |           | Tune container pids limit (set -1 for unlimited)             |
| `--platform`              |           | [**experimental (daemon)**](https://docs.docker.com/engine/reference/commandline/dockerd/#daemon-configuration-file)[**API 1.32+**](https://docs.docker.com/engine/api/v1.32/) Set platform if server is multi-platform capable |
| `--privileged`            |           | Give extended privileges to this container                   |
| `--publish , -p`          |           | Publish a container’s port(s) to the host                    |
| `--publish-all , -P`      |           | Publish all exposed ports to random ports                    |
| `--read-only`             |           | Mount the container’s root filesystem as read only           |
| `--restart`               | `no`      | Restart policy to apply when a container exits               |
| `--rm`                    |           | Automatically remove the container when it exits             |
| `--runtime`               |           | Runtime to use for this container                            |
| `--security-opt`          |           | Security Options                                             |
| `--shm-size`              |           | Size of /dev/shm                                             |
| `--sig-proxy`             | `true`    | Proxy received signals to the process                        |
| `--stop-signal`           | `SIGTERM` | Signal to stop a container                                   |
| `--stop-timeout`          |           | [**API 1.25+**](https://docs.docker.com/engine/api/v1.25/) Timeout (in seconds) to stop a container |
| `--storage-opt`           |           | Storage driver options for the container                     |
| `--sysctl`                |           | Sysctl options                                               |
| `--tmpfs`                 |           | Mount a tmpfs directory                                      |
| `--tty , -t`              |           | Allocate a pseudo-TTY                                        |
| `--ulimit`                |           | Ulimit options                                               |
| `--user , -u`             |           | Username or UID (format: <name\|uid>[:<group\|gid>])         |
| `--userns`                |           | User namespace to use                                        |
| `--uts`                   |           | UTS namespace to use                                         |
| `--volume , -v`           |           | Bind mount a volume                                          |
| `--volume-driver`         |           | Optional volume driver for the container                     |
| `--volumes-from`          |           | Mount volumes from the specified container(s)                |
| `--workdir , -w`          |           | Working directory inside the container                       |



#### 3.5.2查看运行的容器

```
docker ps [OPTIONS]
```

## Options

| Name, shorthand | Default | Description                                             |
| --------------- | ------- | ------------------------------------------------------- |
| `--all , -a`    |         | Show all containers (default shows just running)        |
| `--filter , -f` |         | Filter output based on conditions provided              |
| `--format`      |         | Pretty-print containers using a Go template             |
| `--last , -n`   | `-1`    | Show n last created containers (includes all states)    |
| `--latest , -l` |         | Show the latest created container (includes all states) |
| `--no-trunc`    |         | Don’t truncate output                                   |
| `--quiet , -q`  |         | Only display numeric IDs                                |
| `--size , -s`   |         | Display total file sizes                                |

#### 3.5.3退出容器

~~~shell
exit  #直接结束容器并退出
Ctrl + Q + P #三个按键同时按下(在大写模式下)退出容器但是不结束容器运行
~~~

#### 3.5.4删除容器：docker rm [OPTIONS] CONTAINER [CONTAINER...]

```
docker rm 容器id   #移除ID对应的容器。不能删除正在运行的容器。
docker rm -f  容器ID #，强制删除ID对应的容器ID
docker rm $(docker ps -a -q) #移除所有的容器。但是正在运行的命令不能删除
docker rm  -f $(docker ps -a -q) #移除所有的容器
docker ps -a -q|xargs docker rm  #删除所有容器
```



## Options

| Name, shorthand  | Default | Description                                             |
| ---------------- | ------- | ------------------------------------------------------- |
| `--force , -f`   |         | Force the removal of a running container (uses SIGKILL) |
| `--link , -l`    |         | Remove the specified link                               |
| `--volumes , -v` |         | Remove anonymous volumes associated with the container  |

#### 3.5.5启动和停止容器的操作

```
docker start   容器ID   #启动容器

docker restart 容器ID  # 重新启动容器
 
docker stop 容器ID   # 停止当前运行的容器
docker kill 容器ID   # 强制停止当前运行的容器
```

#### 3.5.6查看日志：docker logs [OPTIONS] CONTAINER

## Options

| Name, shorthand     | Default | Description                                                  |
| ------------------- | ------- | ------------------------------------------------------------ |
| `--details`         |         | Show extra details provided to logs                          |
| `--follow , -f`     |         | Follow log output                                            |
| `--since`           |         | Show logs since timestamp (e.g. 2013-01-02T13:23:37) or relative (e.g. 42m for 42 minutes) |
| `--tail`            | `all`   | Number of lines to show from the end of the logs             |
| `--timestamps , -t` |         | Show timestamps                                              |
| `--until`           |         | [**API 1.35+**](https://docs.docker.com/engine/api/v1.35/) Show logs before a timestamp (e.g. 2013-01-02T13:23:37) or relative (e.g. 42m for 42 minutes) |

#### 3.6.7查看镜像的元数据:docker inspect  容器ID

```
docker inspect 07885e4bd698
[
    {
        "Id": "07885e4bd6981a47c3d95a4578ef41edf284af0276f3f6116a93c9d48a2f5817",
        "Created": "2020-11-07T04:02:03.861607647Z",
        "Path": "/bin/bash",
        "Args": [],
        "State": {
            "Status": "running",
            "Running": true,
            "Paused": false,
            "Restarting": false,
            "OOMKilled": false,
            "Dead": false,
            "Pid": 14613,
            "ExitCode": 0,
            "Error": "",
            "StartedAt": "2020-11-07T05:16:47.952482928Z",
            "FinishedAt": "2020-11-07T04:03:47.928215027Z"
        },
        "Image": "sha256:0d120b6ccaa8c5e149176798b3501d4dd1885f961922497cd0abef155c869566",
        "ResolvConfPath": "/var/lib/docker/containers/07885e4bd6981a47c3d95a4578ef41edf284af0276f3f6116a93c9d48a2f5817/resolv.conf",
        "HostnamePath": "/var/lib/docker/containers/07885e4bd6981a47c3d95a4578ef41edf284af0276f3f6116a93c9d48a2f5817/hostname",
        "HostsPath": "/var/lib/docker/containers/07885e4bd6981a47c3d95a4578ef41edf284af0276f3f6116a93c9d48a2f5817/hosts",
        "LogPath": "/var/lib/docker/containers/07885e4bd6981a47c3d95a4578ef41edf284af0276f3f6116a93c9d48a2f5817/07885e4bd6981a47c3d95a4578ef41edf284af0276f3f6116a93c9d48a2f5817-json.log",
        "Name": "/boring_allen",
        "RestartCount": 0,
        "Driver": "overlay2",
        "Platform": "linux",
        "MountLabel": "",
        "ProcessLabel": "",
        "AppArmorProfile": "docker-default",
        "ExecIDs": null,
        "HostConfig": {
            "Binds": null,
            "ContainerIDFile": "",
            "LogConfig": {
                "Type": "json-file",
                "Config": {}
            },
            "NetworkMode": "default",
            "PortBindings": {},
            "RestartPolicy": {
                "Name": "no",
                "MaximumRetryCount": 0
            },
            "AutoRemove": false,
            "VolumeDriver": "",
            "VolumesFrom": null,
            "CapAdd": null,
            "CapDrop": null,
            "Capabilities": null,
            "Dns": [],
            "DnsOptions": [],
            "DnsSearch": [],
            "ExtraHosts": null,
            "GroupAdd": null,
            "IpcMode": "private",
            "Cgroup": "",
            "Links": null,
            "OomScoreAdj": 0,
            "PidMode": "",
            "Privileged": false,
            "PublishAllPorts": false,
            "ReadonlyRootfs": false,
            "SecurityOpt": null,
            "UTSMode": "",
            "UsernsMode": "",
            "ShmSize": 67108864,
            "Runtime": "runc",
            "ConsoleSize": [
                0,
                0
            ],
            "Isolation": "",
            "CpuShares": 0,
            "Memory": 0,
            "NanoCpus": 0,
            "CgroupParent": "",
            "BlkioWeight": 0,
            "BlkioWeightDevice": [],
            "BlkioDeviceReadBps": null,
            "BlkioDeviceWriteBps": null,
            "BlkioDeviceReadIOps": null,
            "BlkioDeviceWriteIOps": null,
            "CpuPeriod": 0,
            "CpuQuota": 0,
            "CpuRealtimePeriod": 0,
            "CpuRealtimeRuntime": 0,
            "CpusetCpus": "",
            "CpusetMems": "",
            "Devices": [],
            "DeviceCgroupRules": null,
            "DeviceRequests": null,
            "KernelMemory": 0,
            "KernelMemoryTCP": 0,
            "MemoryReservation": 0,
            "MemorySwap": 0,
            "MemorySwappiness": null,
            "OomKillDisable": false,
            "PidsLimit": null,
            "Ulimits": null,
            "CpuCount": 0,
            "CpuPercent": 0,
            "IOMaximumIOps": 0,
            "IOMaximumBandwidth": 0,
            "MaskedPaths": [
                "/proc/asound",
                "/proc/acpi",
                "/proc/kcore",
                "/proc/keys",
                "/proc/latency_stats",
                "/proc/timer_list",
                "/proc/timer_stats",
                "/proc/sched_debug",
                "/proc/scsi",
                "/sys/firmware"
            ],
            "ReadonlyPaths": [
                "/proc/bus",
                "/proc/fs",
                "/proc/irq",
                "/proc/sys",
                "/proc/sysrq-trigger"
            ]
        },
        "GraphDriver": {
            "Data": {
                "LowerDir": "/var/lib/docker/overlay2/5509c6ee8711cfb783ab07265aba26c03a5a5316f25301771dc378cdd28eb8ff-init/diff:/var/lib/docker/overlay2/57bc161b50a1ba0c431389815c8e5fcfc61c5df249b2f6741f290e5198334cac/diff",
                "MergedDir": "/var/lib/docker/overlay2/5509c6ee8711cfb783ab07265aba26c03a5a5316f25301771dc378cdd28eb8ff/merged",
                "UpperDir": "/var/lib/docker/overlay2/5509c6ee8711cfb783ab07265aba26c03a5a5316f25301771dc378cdd28eb8ff/diff",
                "WorkDir": "/var/lib/docker/overlay2/5509c6ee8711cfb783ab07265aba26c03a5a5316f25301771dc378cdd28eb8ff/work"
            },
            "Name": "overlay2"
        },
        "Mounts": [],
        "Config": {
            "Hostname": "07885e4bd698",
            "Domainname": "",
            "User": "",
            "AttachStdin": true,
            "AttachStdout": true,
            "AttachStderr": true,
            "Tty": true,
            "OpenStdin": true,
            "StdinOnce": true,
            "Env": [
                "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"
            ],
            "Cmd": [
                "/bin/bash"
            ],
            "Image": "centos",
            "Volumes": null,
            "WorkingDir": "",
            "Entrypoint": null,
            "OnBuild": null,
            "Labels": {
                "org.label-schema.build-date": "20200809",
                "org.label-schema.license": "GPLv2",
                "org.label-schema.name": "CentOS Base Image",
                "org.label-schema.schema-version": "1.0",
                "org.label-schema.vendor": "CentOS"
            }
        },
        "NetworkSettings": {
            "Bridge": "",
            "SandboxID": "931c558dc035950b688e5be559d89451c3b49558539bbcbbb24b42715ced60ad",
            "HairpinMode": false,
            "LinkLocalIPv6Address": "",
            "LinkLocalIPv6PrefixLen": 0,
            "Ports": {},
            "SandboxKey": "/var/run/docker/netns/931c558dc035",
            "SecondaryIPAddresses": null,
            "SecondaryIPv6Addresses": null,
            "EndpointID": "72ac66b6fa3463ba67fa6547676ad68d8fb37ef32f61b276b5890fb1d5949bb3",
            "Gateway": "172.17.0.1",
            "GlobalIPv6Address": "",
            "GlobalIPv6PrefixLen": 0,
            "IPAddress": "172.17.0.2",
            "IPPrefixLen": 16,
            "IPv6Gateway": "",
            "MacAddress": "02:42:ac:11:00:02",
            "Networks": {
                "bridge": {
                    "IPAMConfig": null,
                    "Links": null,
                    "Aliases": null,
                    "NetworkID": "07b800df6fa8bdae2503747af153c337d9981059fa0cb72b15c59d2c0a20e05e",
                    "EndpointID": "72ac66b6fa3463ba67fa6547676ad68d8fb37ef32f61b276b5890fb1d5949bb3",
                    "Gateway": "172.17.0.1",
                    "IPAddress": "172.17.0.2",
                    "IPPrefixLen": 16,
                    "IPv6Gateway": "",
                    "GlobalIPv6Address": "",
                    "GlobalIPv6PrefixLen": 0,
                    "MacAddress": "02:42:ac:11:00:02",
                    "DriverOpts": null
                }
            }
        }
    }
]

```

#### 3.6.8进入当前正在运行的容器： docker exec -it  容器id   bashShell

```

docker exec -it 07885e4bd698  bash     #进入运行的容器，开启一个新的终端，可以 在里面操作
[root@07885e4bd698 /]# dir
bin  etc   lib	  lost+found  mnt  proc  run   srv  tmp  var
dev  home  lib64  media       opt  root  sbin  sys  usr

docker attach 容器id    #进入容器中正在执行的终端，不会启动新的终端
```

#### 3.6.9从容器总拷贝文件到 主机： docker cp 容器id:容器内文件路径    目的主机路径





![image-20201107134400912](image\image-8.png)

## 4.安装常用镜像

### 4.1安装Nginx

①安装

```
docker pull nginx
Using default tag: latest
latest: Pulling from library/nginx
bb79b6b2107f: Already exists 
5a9f1c0027a7: Pull complete 
b5c20b2b484f: Pull complete 
166a2418f7e8: Pull complete 
1966ea362d23: Pull complete 
Digest: sha256:aeade65e99e5d5e7ce162833636f692354c227ff438556e5f3ed0335b7cc2f1b
Status: Downloaded newer image for nginx:latest
docker.io/library/nginx:latest

```

②运行

```
# -d  后台运行
# --name  给容器取个名字
# -p 宿主机端口：容器端口 
docker run -d --name nginx01 -p 3304:80 nginx

03150c62f5605a684debdd7553ae80aa22416b925e4ceecddcda26550324eaf3

```

③测试

```
curl localhost:3304  #访问Nginx首页
```



```

<!DOCTYPE html>
<html>
<head>
<title>Welcome to nginx!</title>
<style>
    body {
        width: 35em;
        margin: 0 auto;
        font-family: Tahoma, Verdana, Arial, sans-serif;
    }
</style>
</head>
<body>
<h1>Welcome to nginx!</h1>
<p>If you see this page, the nginx web server is successfully installed and
working. Further configuration is required.</p>

<p>For online documentation and support please refer to
<a href="http://nginx.org/">nginx.org</a>.<br/>
Commercial support is available at
<a href="http://nginx.com/">nginx.com</a>.</p>

<p><em>Thank you for using nginx.</em></p>
</body>
</html>

```

![image-20201107135824119](image\image-9.png)

### 4.2安装Tomcat

#### ①下载Tomcat安装

```
docker pull tomcat
```

②运行Tomcat

```
docker run -d --name tomcat01 -p 3305:8080 tomcat
b21ced830f4e2af462e1d89bf7df4ab471f0b1696557df74f1083c8578f1c432
```

③进入Tomcat

```
docker exec -it tomcat01 bash
```

④在阿里云安全组规则中开放 3305这个端口

⑤在浏览器中访问Tomcat： http://121.89.208.222:3305/

![image-20201107143435299](image\image-10.png)

发现是404页面。

**原因：**我们下载的这个Tomcat默认是阉割版的，那些没必要的文件是已经被剔除了的。

我们首先进入Tomcat里面：

```
docker exec -it tomcat01 bash
root@b21ced830f4e:/usr/local/tomcat# 

```

![image-20201107144032049](image\image-11.png)

进入webapps目录，该目录是空的，没有相应的资源文件，因为我们在浏览器访问的时候默认就是404页面。但是我们发现这些原有的资源文件保存在了webapps.dist文件夹里：

```
root@b21ced830f4e:/usr/local/tomcat/webapps.dist# dir
ROOT  docs  examples  host-manager  manager
root@b21ced830f4e:/usr/local/tomcat/webapps.dist# 

```

我们需要把这些文件都复制到webapps文件夹中去才能使得在浏览器访问成功。

```
cp -r  webapps.dist/*  webapps
```

```
root@b21ced830f4e:/usr/local/tomcat# cd webapps
root@b21ced830f4e:/usr/local/tomcat/webapps# dir
ROOT  docs  examples  host-manager  manager

```

重新访问成功

![image-20201107144420539](image\image-13.png)

## 5.可视化

## 5.1portainer(先用这个)

①安装运行

```
$ docker run -d -p 8000:8000 -p 9000:9000 --name=portainer --restart=always -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer-ce


```

②访问： http:ip:9000

![image-20201107211648595](image\image-12.png)

## 5.2Rancher（CI/CD再用）



## 6.Docker镜像原理

### 6.1什么是镜像

> 镜像（Mirroring）是一种文件存储形式，是冗余的一种类型，一个[磁盘](https://baike.baidu.com/item/磁盘/2842227)上的数据在另一个磁盘上存在一个完全相同的副本即为镜像。可以把许多文件做成一个镜像文件，与GHOST等程序放在一个盘里用[GHOST](https://baike.baidu.com/item/GHOST/20443952)等软件打开后，又恢复成许多文件，RAID 1和RAID 10使用的就是镜像。常见的镜像[文件格式](https://baike.baidu.com/item/文件格式)有ISO、BIN、IMG、TAO、DAO、CIF、FCD。

所有的应用，直接打包成Docker镜像，就可以直接跑起来。

如何得到镜像：

①从远程仓库下载

②从其它地方拷贝

③自己制作一个DockerFile

### 6.2Docker镜像讲解

#### 6.2.1UnionFS（联合文件系统）

> 联合文件系统（Union File System）：2004年由纽约州立大学石溪分校开发，它可以把多个目录(也叫分支)内容联合挂载到同一个目录下，而目录的物理位置是分开的。UnionFS允许只读和可读写目录并存，就是说可同时删除和增加内容。UnionFS应用的地方很多，比如在多个磁盘分区上合并不同文件系统的主目录，或把几张CD光盘合并成一个统一的光盘目录(归档)。另外，具有写时复制(copy-on-write)功能UnionFS可以把只读和可读写文件系统合并在一起，虚拟上允许只读文件系统的修改可以保存到可写文件系统当中。

### 6.2.2Docker镜像加速原理

docker的镜像实际上由一层一层的文件系统组成，这种层级的文件系统UnionFS。

**bootfs(boot file system)**主要包含bootloader和kernel，bootloader主要是引导加载kernel，Linux刚启动时会加载bootfs文件系统，**在Docker镜像的最底层是bootfs**。这一层与我们典型的Linux/Unix系统是一样的, 包含boot加载器和内核。当boot加载完成之后整个内核就都在内存中了，此时内存的使用权已由bootfs转交给内核，此时系统也会卸载bootfs。**rootfs(root file system)**, 在bootfs之上。包含的就是典型Linux系统中的/dev, /proc, /bin, /etc等标准目录和文件。rootfs就是各种不同的操作系统发行版，比如Ubuntu，Centos等等。

### 6.3镜像的分层结构

#### 6.3.1Docker 支持通过扩展现有镜像，创建新的镜像。

实际上，Docker Hub 中 99% 的镜像都是通过在 base 镜像中安装和配置需要的软件构建出来的。比如我们现在构建一个新的镜像，Dockerfile 如下：

```
# Version: 0.0.1
FROM debian                1.新镜像不再是从 scratch 开始，而是直接在 Debian base 镜像上构建。
MAINTAINER wzlinux
RUN apt-get update && apt-get install -y emacs        2.安装 emacs 编辑器。
RUN apt-get install -y apache2             3.安装 apache2。
CMD ["/bin/bash"]              4.容器启动时运行 bash。
```

构建过程如下图所示：

![Docker镜像的内部结构(四)](image\image-17.png)
可以看到，新镜像是从 base 镜像一层一层叠加生成的。每安装一个软件，就在现有镜像的基础上增加一层。

### 6.3可写的容器层

当容器启动时，一个新的可写层被加载到镜像的顶部。这一层通常被称作“容器层”，“容器层”之下的都叫“镜像层”。

典型的Linux在启动后，首先将 rootfs 置为 readonly, 进行一系列检查, 然后将其切换为 “readwrite” 供用户使用。在docker中，起初也是将 rootfs 以readonly方式加载并检查，然而接下来利用 union mount 的将一个 readwrite 文件系统挂载在 readonly 的rootfs之上，并且允许再次将下层的 file system设定为readonly 并且向上叠加, 这样一组readonly和一个writeable的结构构成一个container的运行目录, 每一个被称作一个Layer。如下图所示。

![Docker镜像的内部结构(四)](https://s4.51cto.com/images/blog/201711/28/bdf6de9d0071f46b259889fca7f333a6.jpg?x-oss-process=image/watermark,size_16,text_QDUxQ1RP5Y2a5a6i,color_FFFFFF,t_100,g_se,x_10,y_10,shadow_90,type_ZmFuZ3poZW5naGVpdGk=)
所有对容器的改动，无论添加、删除、还是修改文件都只会发生在容器层中。只有容器层是可写的，容器层下面的所有镜像层都是只读的。

下面我们深入讨论容器层的细节。

镜像层数量可能会很多，所有镜像层会联合在一起组成一个统一的文件系统。如果不同层中有一个相同路径的文件，比如 /a，上层的 /a 会覆盖下层的 /a，也就是说用户只能访问到上层中的文件 /a。在容器层中，用户看到的是一个叠加之后的文件系统。

1. **添加文件：**在容器中创建文件时，新文件被添加到容器层中。
2. **读取文件：**在容器中读取某个文件时，Docker 会从上往下依次在各镜像层中查找此文件。一旦找到，立即将其复制到容器层，然后打开并读入内存。
3. **修改文件：**在容器中修改已存在的文件时，Docker 会从上往下依次在各镜像层中查找此文件。一旦找到，立即将其复制到容器层，然后修改之。
4. **删除文件：**在容器中删除文件时，Docker 也是从上往下依次在镜像层中查找此文件。找到后，会在容器层中记录下此删除操作。

只有当需要修改时才复制一份数据，这种特性被称作 Copy-on-Write。可见，容器层保存的是镜像变化的部分，不会对镜像本身进行任何修改。

这样就解释了我们前面提出的问题：容器层记录对镜像的修改，所有镜像层都是只读的，不会被容器修改，所以镜像可以被多个容器共享。

### 6.4为什么Docker 镜像要采用这种分层结构呢？

最大的一个好处就是 - 共享资源。

比如：有多个镜像都从相同的 base 镜像构建而来，那么 Docker Host 只需在磁盘上保存一份 base 镜像；同时内存中也只需加载一份 base 镜像，就可以为所有容器服务了。而且镜像的每一层都可以被共享，我们将在后面更深入地讨论这个特性。

这时可能就有人会问了：如果多个容器共享一份基础镜像，当某个容器修改了基础镜像的内容，比如 /etc 下的文件，这时其他容器的 /etc 是否也会被修改？

答案是不会！
修改会被限制在单个容器内。
这就是我们接下来要说的容器 Copy-on-Write 特性。

1. 新数据会直接存放在最上面的容器层。

2. 修改现有数据会先从镜像层将数据复制到容器层，修改后的数据直接保存在容器层中，镜像层保持不变。

3. 如果多个层中有命名相同的文件，用户只能看到最上面那层中的文件

4. 平时我们安装进虚拟机的CentOS都是几个G大小，但是我们在Docker这里安装的CentOS才几百M。

   ![在这里插入图片描述](image\image-15.png)

   ![image-20201107213656765](image\image-16.png)

   这是对于一个精简的OS来说，rootfs可以很小，只需要包含最基本的的命令，工具和工具程序库就可以了，因为底层直接用Host的kernel，自己只需要提供rootfs就可以了。由此可见，对于不同的Linux发型版本，bootfs是基本一致的，rootfs有差别，因此不同的发行版本可以共用bootfs。

### 6.5Docker镜像特点

Docker镜像都是只读的，当容器启动时，一个新的可写的层被加载到镜像的顶部，这一层就是我们通常说的容器层，容器之下的都是镜像层。

**Docker镜像的主要特点：** 

  分层： Docker镜像时采用分层的方式构建的，每个镜像都由一系列的"镜像层"组成。分层结构是Docker镜像如此轻量的重要原因，当需要修改容器镜像内的某个文件时，只对处于最上方的读写层进行变动，不覆写下层已有文件系统的内容，已有文件在只读层中的原始版本仍然存在，但会被读写层中的新版本文件所隐藏。当使用docker commit 提交这个修改过的容器文件系统为一个新的镜像时，保存的内容仅为最上层读写文件系统中被更新过的文件。分层达到了在不同镜像之间共享镜像层的效果。
  写时复制： Docker镜像使用了写时复制（copy-on-write）策略，在多个容器之间共享镜像，每个容器在启动的时候并不需要单独复制一份镜像文件，而是将所有镜像层以只读的方式挂载到一个挂载点，再在上面覆盖一个可读写的容器层。在未更改文件内容时，所有容器都共享一份数据，只有在Docker容器运行过程中文件系统发生变化时，才会把变化的文件内容写到可读写层，并隐藏只读层中的老版本文件。写时复制配合分层机制减少了镜像对磁盘空间的占用和容器启动时间。
  内容寻址： 在Docker 1.10版本后，Docker镜像改动较大，其中最重要的特性便是引入了内容寻址存储（content-addressable storge）的机制，根据文件内容来索引镜像的镜像层。与之前版本对每一个镜像层随机生成一个UUID不同，新模型对镜像层的内容计算校验和，生成一个内容哈希值，并以此哈希值代替之前的UUID作为镜像层的唯一标志。该机制主要提高了镜像的安全性，并在pull、push、load、save操作后检测数据的完整性。另外，基于内容哈希来索引镜像层，在一定程度上减少了ID的冲突并且增强了镜像层的共享。对于来自不同构建的镜像层，只要拥有相同的内容哈希，也能被不同的镜像共享。
  联合挂载： 通俗的讲，联合挂载技术可以在一个挂载点同时挂载多个文件系统，将挂载点的原目录与被挂载内容进行整合，使得最终可见的文件系统将会包含整合之后的各层的文件和目录。实现这种联合挂载技术的文件系统通常被称为联合文件系统（Union FileSystem）

### 6.7coomit 镜像

```
docker commit [OPTIONS] CONTAINER [REPOSITORY[:TAG]]
```

## Options

| Name, shorthand  | Default | Description                                                  |
| ---------------- | ------- | ------------------------------------------------------------ |
| `--author , -a`  |         | Author (e.g., “John Hannibal Smith [hannibal@a-team.com](mailto:hannibal@a-team.com)”) |
| `--change , -c`  |         | Apply Dockerfile instruction to the created image            |
| `--message , -m` |         | Commit message                                               |
| `--pause , -p`   | `true`  | Pause container during commit                                |

```
docker commit -a "陌意随影"   -m  "自己制作的镜像" f3447f29d3cc tomcat02:1.0 
sha256:ff8cac788ff9bed8eeb91a44c75f55952d14088e2352c7611575f9b56032542e

```

## 7.容器数据卷

### 7.1Docker存在的问题

Docker镜像由多个只读层叠加而成，启动容器时，Docker会加载只读镜像层并在镜像栈顶部添加一个读写层，如果运行中的容器修改了现有的一个已经存在的文件，那该文件将会从读写层下面的只读层复制到读写层，该文件的只读版本仍然存在，只是已经被读写层中该文件的副本所隐藏，此即"写时复制(COW)"机制。
在这里插入图片描述

![img](image\image-18.png)
现在我们知道，一个运行的容器有一个或多个只读层和一个读写层。在容器运行过程中，若产生了一些重要的数据或是更改了一些文件，这些更改我们应该怎么保存呢？容器关闭或重启，这些数据不受影响；但删除Docker容器，则数据将会全部丢失。除此之外也还有其他的一些问题。

  ①存储于联合文件系统中，不易于宿主机访问。
  ② 容器间数据共享不便。

### 7.2Docker中数据卷（volume）的作用

​	为了解决这些问题，Docker引入了数据卷（volume）机制。volume是存在于一个或多个容器中的特定文件或文件夹，这个目录以独立于联合文件系统的形式在宿主机中存在，并为数据的共享与持久化提供以下便利。

①volume在容器创建时就会初始化，在容器运行时就可以使用其中的文件。
②volume能在不同的容器之间共享和重用。
③对volume中数据的操作会马上生效。
④ 对volume中数据的操作不会影响到镜像本身。
⑤volume的生存周期独立于容器的生存周期，即使删除容器，volume仍然会存在，没有任何容器使用的volume也不会被Docker删除。

### 7.3卷的类型

 Docker有两种类型的卷，每种类型都在容器中存在一个挂载点，但其在宿主机上的位置有所不同；
 ① Bind mount volume： a volume that points to a user-specified location on the host file system（docker和宿主机上的卷都是用户自己指定）
 ② Docker-managed volume：the Docker daemon creates managed volumes in a portion of the host’s file system that’s owned by Docker（docker管理的卷是用户自己指定，宿主机上的卷是固定的）

### 7.4添加数据卷的方式有两种，第一种是直接通过命令行挂载，第二种是通过dockerFile添加

#### 7.4.1通过命令行挂载的方式

> docker run -it -v /宿主机绝对路径目录: /容器内目录 镜像名

这个命令会在宿主机和容器内分别建立两个目录，两个目录是对接的，里面的数据可以共享。如果我们不知道数据卷是否挂载成功时，我们可以通过以下方式来检查数据卷的挂载结果。

> docker inspect 容器id

上面的命令可以查看容器的详细情况，命令返回的是JSON格式的字符串，运行命令之后我们在返回的JSON字符串中找到Volumes属性，假如挂载成功的话，Volumes里面显示的绑定结果应该是你在挂载时输入的命令参数 （/宿主机绝对路径目录: /容器内目录 ），如果与你们之前输入的一致的话，证明挂载成功。PS: Volumes里面显示的绑定结果可能有多个，但是只要找到目标结果就可以。挂载之后，当容器停止运行的时候，宿主机上对数据卷做的内容修改是会同步到容器内的。

```
docker inspect e833536be4fb
[
    {
        "Id": "e833536be4fb5e108e95fd69370d4c67d85e8c45433720173fc0e1a7a42f5a7f",
        "Created": "2020-11-07T15:54:40.721148397Z",
        "Path": "bash",
        "Args": [],
        "State": {
            "Status": "running",
            "Running": true,
            "Paused": false,
            "Restarting": false,
            "OOMKilled": false,
            "Dead": false,
            "Pid": 20932,
            "ExitCode": 0,
            "Error": "",
            "StartedAt": "2020-11-07T15:54:41.278161245Z",
            "FinishedAt": "0001-01-01T00:00:00Z"
        },
        "Image": "sha256:0d120b6ccaa8c5e149176798b3501d4dd1885f961922497cd0abef155c869566",
        "ResolvConfPath": "/var/lib/docker/containers/e833536be4fb5e108e95fd69370d4c67d85e8c45433720173fc0e1a7a42f5a7f/resolv.conf",
        "HostnamePath": "/var/lib/docker/containers/e833536be4fb5e108e95fd69370d4c67d85e8c45433720173fc0e1a7a42f5a7f/hostname",
        "HostsPath": "/var/lib/docker/containers/e833536be4fb5e108e95fd69370d4c67d85e8c45433720173fc0e1a7a42f5a7f/hosts",
        "LogPath": "/var/lib/docker/containers/e833536be4fb5e108e95fd69370d4c67d85e8c45433720173fc0e1a7a42f5a7f/e833536be4fb5e108e95fd69370d4c67d85e8c45433720173fc0e1a7a42f5a7f-json.log",
        "Name": "/eager_shockley",
        "RestartCount": 0,
        "Driver": "overlay2",
        "Platform": "linux",
        "MountLabel": "",
        "ProcessLabel": "",
        "AppArmorProfile": "docker-default",
        "ExecIDs": [
            "b5f530ecfe48204f4b72dc03196b70c412c1a6edfc4167f0cab9ea88ffbbb3de",
            "d95ea89ab7b458a5b83f50cba0a29f3b42fad9a70ca615538b5fd7c012f5380d"
        ],
        "HostConfig": {
            "Binds": [
                "/ceshi:/ceshi"
            ],
            "ContainerIDFile": "",
            "LogConfig": {
                "Type": "json-file",
                "Config": {}
            },
            "NetworkMode": "default",
            "PortBindings": {},
            "RestartPolicy": {
                "Name": "no",
                "MaximumRetryCount": 0
            },
            "AutoRemove": false,
            "VolumeDriver": "",
            "VolumesFrom": null,
            "CapAdd": null,
            "CapDrop": null,
            "Capabilities": null,
            "Dns": [],
            "DnsOptions": [],
            "DnsSearch": [],
            "ExtraHosts": null,
            "GroupAdd": null,
            "IpcMode": "private",
            "Cgroup": "",
            "Links": null,
            "OomScoreAdj": 0,
            "PidMode": "",
            "Privileged": false,
            "PublishAllPorts": false,
            "ReadonlyRootfs": false,
            "SecurityOpt": null,
            "UTSMode": "",
            "UsernsMode": "",
            "ShmSize": 67108864,
            "Runtime": "runc",
            "ConsoleSize": [
                0,
                0
            ],
            "Isolation": "",
            "CpuShares": 0,
            "Memory": 0,
            "NanoCpus": 0,
            "CgroupParent": "",
            "BlkioWeight": 0,
            "BlkioWeightDevice": [],
            "BlkioDeviceReadBps": null,
            "BlkioDeviceWriteBps": null,
            "BlkioDeviceReadIOps": null,
            "BlkioDeviceWriteIOps": null,
            "CpuPeriod": 0,
            "CpuQuota": 0,
            "CpuRealtimePeriod": 0,
            "CpuRealtimeRuntime": 0,
            "CpusetCpus": "",
            "CpusetMems": "",
            "Devices": [],
            "DeviceCgroupRules": null,
            "DeviceRequests": null,
            "KernelMemory": 0,
            "KernelMemoryTCP": 0,
            "MemoryReservation": 0,
            "MemorySwap": 0,
            "MemorySwappiness": null,
            "OomKillDisable": false,
            "PidsLimit": null,
            "Ulimits": null,
            "CpuCount": 0,
            "CpuPercent": 0,
            "IOMaximumIOps": 0,
            "IOMaximumBandwidth": 0,
            "MaskedPaths": [
                "/proc/asound",
                "/proc/acpi",
                "/proc/kcore",
                "/proc/keys",
                "/proc/latency_stats",
                "/proc/timer_list",
                "/proc/timer_stats",
                "/proc/sched_debug",
                "/proc/scsi",
                "/sys/firmware"
            ],
            "ReadonlyPaths": [
                "/proc/bus",
                "/proc/fs",
                "/proc/irq",
                "/proc/sys",
                "/proc/sysrq-trigger"
            ]
        },
        "GraphDriver": {
            "Data": {
                "LowerDir": "/var/lib/docker/overlay2/732761fbbdd12efc70631b4d0a8eb6f5df2a13fb5ab96c045bd5c4af8659ae6b-init/diff:/var/lib/docker/overlay2/57bc161b50a1ba0c431389815c8e5fcfc61c5df249b2f6741f290e5198334cac/diff",
                "MergedDir": "/var/lib/docker/overlay2/732761fbbdd12efc70631b4d0a8eb6f5df2a13fb5ab96c045bd5c4af8659ae6b/merged",
                "UpperDir": "/var/lib/docker/overlay2/732761fbbdd12efc70631b4d0a8eb6f5df2a13fb5ab96c045bd5c4af8659ae6b/diff",
                "WorkDir": "/var/lib/docker/overlay2/732761fbbdd12efc70631b4d0a8eb6f5df2a13fb5ab96c045bd5c4af8659ae6b/work"
            },
            "Name": "overlay2"
        },
        "Mounts": [
            {
                "Type": "bind",
                "Source": "/ceshi",
                "Destination": "/ceshi",
                "Mode": "",
                "RW": true,
                "Propagation": "rprivate"
            }
        ],
        "Config": {
            "Hostname": "e833536be4fb",
            "Domainname": "",
            "User": "",
            "AttachStdin": true,
            "AttachStdout": true,
            "AttachStderr": true,
            "Tty": true,
            "OpenStdin": true,
            "StdinOnce": true,
            "Env": [
                "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"
            ],
            "Cmd": [
                "bash"
            ],
            "Image": "centos",
            "Volumes": null,
            "WorkingDir": "",
            "Entrypoint": null,
            "OnBuild": null,
            "Labels": {
                "org.label-schema.build-date": "20200809",
                "org.label-schema.license": "GPLv2",
                "org.label-schema.name": "CentOS Base Image",
                "org.label-schema.schema-version": "1.0",
                "org.label-schema.vendor": "CentOS"
            }
        },
        "NetworkSettings": {
            "Bridge": "",
            "SandboxID": "0f6a7598b17857a539d62379bcbba63b1961a9d82799d7b484cb4f75d13e8f56",
            "HairpinMode": false,
            "LinkLocalIPv6Address": "",
            "LinkLocalIPv6PrefixLen": 0,
            "Ports": {},
            "SandboxKey": "/var/run/docker/netns/0f6a7598b178",
            "SecondaryIPAddresses": null,
            "SecondaryIPv6Addresses": null,
            "EndpointID": "300bada8fb7911fcc3f1288ad39048a637cb29129b67600fb0ab914ff838670a",
            "Gateway": "172.17.0.1",
            "GlobalIPv6Address": "",
            "GlobalIPv6PrefixLen": 0,
            "IPAddress": "172.17.0.2",
            "IPPrefixLen": 16,
            "IPv6Gateway": "",
            "MacAddress": "02:42:ac:11:00:02",
            "Networks": {
                "bridge": {
                    "IPAMConfig": null,
                    "Links": null,
                    "Aliases": null,
                    "NetworkID": "07b800df6fa8bdae2503747af153c337d9981059fa0cb72b15c59d2c0a20e05e",
                    "EndpointID": "300bada8fb7911fcc3f1288ad39048a637cb29129b67600fb0ab914ff838670a",
                    "Gateway": "172.17.0.1",
                    "IPAddress": "172.17.0.2",
                    "IPPrefixLen": 16,
                    "IPv6Gateway": "",
                    "GlobalIPv6Address": "",
                    "GlobalIPv6PrefixLen": 0,
                    "MacAddress": "02:42:ac:11:00:02",
                    "DriverOpts": null
                }
            }
        }
    }
]

```

挂载的信息在“Mounts”里：

```
"Mounts": [
            {
                "Type": "bind",  
                "Source": "/ceshi",  #主机内地址
                "Destination": "/ceshi", #Docker容器内地址
                "Mode": "",
                "RW": true,
                "Propagation": "rprivate"
            }
        ],

```



我们再挂载的时候还可以给数据卷加上权限，假如我们要宿主机只能读取容器的数据卷内容不能修改，我们可以添加只读权限

> docker run -it -v /宿主机绝对路径目录 ： /容器内目录 ：ro 镜像名

至于只写的话我们一般不会用到，要么就是读写，要么就是只读，而且我们可以通过docker inspect 来查看容器的volumesRW来查看容器内数据卷的读写权限。

#### 7.4.2利用dockerFile的形式添加

dockerFile对于docker镜像而言就如同java中某个类的.class文件对应上该类的.java文件。

首先在linux服务器根目录上新建docker文件夹并建立DockerFile文件，使用volume命令（出于可移植可分享的的考虑，用以上 -v /宿主机绝对路径目录 ： /容器内目录 的这种方式不能够直接在dockerFile中直接实现，因为宿主机目录是依赖于特定的宿主机的，并不能保证所有的宿主机都存在这样特定的目录）

编写的dockerFile文件如下

> FROM 镜像名
>
> VOLUME ["/生成的目录路径"] -- privileged=true
>
> CMD echo "success build"
>
> CMD /bin/bash

相当于命令行： docker run -it -v /宿主机目录路径 : /生成的目录路径

然后我们通过命令行docker build执行我们写好的dockerFile文件（docker build和docker commit两个命令都可以建立docker镜像，docker commit 需要在容器内进行，docker build 不需要）

> docker build -f /docker/DockerFile -t 命名空间/镜像名

执行后输入docker images就可以发现自己通过DockerFile所build的镜像，里面有挂载数据卷，那么问题来了宿主机所对应的目录是什么呢？同上，我们可以通过docker inspect来查看当前容器的Volumes，里面会有宿主机的数据卷目录。

### 7.5什么是docker数据卷容器？

上面介绍了docker容器数据卷，它的作用相当于生活中的活动硬盘，那么数据卷容器就相当于把多个活动硬盘再挂载到一个活动硬盘上，实现数据的传递依赖。

官网解析：命名的容器挂载数据卷，其他的容器通过挂载这个父容器实现数据共享，挂载数据卷的容器，我们称为数据卷容器。

首先，我们建立父容器

> docker run -it - -name parentContainer 镜像名（可以填上面通过dockerFile建立的镜像，里面有挂载容器卷）

然后建立两个子容器继承父容器

> docker run -it - -name sonContainer1 --volumes -from parentContainer 镜像名
>
> docker run -it - -name sonContainer2 --volumes -from parentContainer 镜像名

假设我们DockerFile里面定义的容器卷目录为dockerVolume，父容器里面有dockerVolume目录，子容器继承了父容器的dockerVolume，在字容器中的dockerVolume目录作出的修改会同步到父容器的dockerVolume目录上，达到了继承和数据共享的目的。

官网上有一句话描述的是，容器之间配置信息的传递，数据卷的生命周期会一致持续到没有容器使用它为止，换言之，只要有一个容器仍在使用该数据卷，该数据卷一直都可以进行数据共享，通俗地来说，如果此时我们把父容器关闭掉，两个字容器之间依旧可以进行数据共享，而且通过继承子容器生成的新容器，一样可以与子容器进行数据共享。这就是docker容器间的数据传递共享。

### 7.6安装MySQL

#### 7.6.1下载并安装

```
docker run --name mysql01 -p 3307:3306 -d -v /mysql/conf:/etc/mysql/conf.d -v /mysql/logs:/logs -v /mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=root mysql:latest
```

- **--name mysql01**：给运行的MySQL取一个名字。
- **-p 3307:3306**：将容器的 3306 端口映射到主机的 3307端口。我们可以通过主机的3307端口访问该数据库。
- **- v  /mysql/conf:/etc/mysql/conf.d**：将主机路径为/mysql/conf的目录 挂载到容器的 /etc/mysql/my.cnf。
- **-v /mysql/logs:/logs**：将主机路径为/mysql/ logs 的目录挂载到容器的 /logs。
- **-v /mysql/data:/var/lib/mysql** ：将主机路径为/mysql/data的目录挂载到容器的 /var/lib/mysql 。
- **-e MYSQL_ROOT_PASSWORD=root：**初始化 root 用户的密码为root

#### 7.6.2进入MySQL容器

```
docker exec -it mysql01 bash
```



#### 7.6.3登录MySQL

```
mysql -hlocalhost -uroot -proot
```

登录成功：

![image-20201108094058505](image\image-36.png)



#### 7.6.4使用Navicat远程连接

使用navicat for mysql连接mysql发现报错：Client does not support authentication protocol requested by server。。。

![img](https://img2018.cnblogs.com/blog/1197133/201907/1197133-20190722164059842-2019031019.png)

这是因为我们的root用户还没有开放远程登录权限，只允许本地登录。我们需要进入将root用户权限开启远程登录。

解决方案：

进入容器：

```
docker exec -it mysql01 /bin/bash
```

进入mysql：

```
mysql -uroot -proot
```

授权：

```
mysql> GRANT ALL ON *.* TO 'root'@'%';
```

刷新权限：

```
mysql> flush privileges;
```

更新加密规则：

```
mysql> ALTER USER 'root'@'localhost' IDENTIFIED BY 'password' PASSWORD EXPIRE NEVER;
```

更新root用户密码：

```
mysql> ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '123456';
```

刷新权限：

```
mysql> flush privileges;
```

重新使用Navicat测试连接：

![image-20201108094814736](image\image-37.png)

#### 7.6.5使用控制台登陆MySQL

```
mysql -h 121.89.208.222 -P 3307 -u root -p
```

**PS：这里的端口指定需要大写的 P才可以，小写p是指定密码**

### 7.8匿名挂载和具名挂载

#### 7.8.1匿名挂载：-v的时候只写了容器内的路径，而没有写容器外的路径

(1）启动容器

docker run -d -P --name nginx01 -v /etc/nginx nginx 

**-v  容器内路径**

```
docker run -d -P  --name nginx02 -v /etc/nginx nginx
```

（2）查看所有卷的情况

```
 docker volume ls
```

![image-20201108100259754](image\image-38.png)

由上图可以看到，VOLUME NAME 有的是随机生成的字符串，对于这种就是匿名挂载，因为-v的时候只写了容器内的路径看，而没有写容器外的路径。

#### 7.8.2具名挂载

(1) 启动容器

docker run -d -P --name nginx02 -v juming-nginx:/etc/nginx nginx 

语法： -v 卷名：容器内路径

```
docker run -d -P --name nginx03 -v juming:/etc/nginx nginx

```

（2）查看所有卷

```
docker volume ls
```

![image-20201108100832025](image\image-39.png)

可以看到具名卷这里会出现我们给定的命名表示。

#### 7.8.3查看匿名和具名挂载的信息

（1）查看匿名卷

```
 docker volume inspect b080282a5e92649dc07bd59f75952705635f3648a3847cb082624462933d7017
```

![image-20201108101356331](image\image-40.png)



(2)查看具名卷

```
docker volume inspect juming

```

![image-20201108101539832](image\image-41.png)

（3）规律

所有docker容器内的卷，没有指定目录的情况下都是在 /var/lib/docker/volumes/XXX

docker volumn ls 查看所有的卷都在这个位置/var/lib/docker/volumns

![image-20201108101741724](image\image-42.png)

我们通过具名挂载可以方便的找到我们的一个卷，大多数情况在使用的，不建议大家使用匿名挂载

#### 7.8.4确定是匿名挂载还是具名挂载

-v 容器内路径         #匿名挂载
-v 卷名：容器内路径      #具名挂载
-v /宿主机路径：容器内路径   #指定路径挂载

#### 7.8.5拓展

（1）通过 -v 容器内路径:ro rw 改变读写权限

ro readonly  #只读
rw readwrite #可读可写

（2）一旦这个设定了容器权限，容器对我们挂载出来的内容就有限定了

docker run -d -P --name nginx02 -v juming-nginx:/etc/nginx:ro  nginx
docker run -d -P --name nginx02 -v juming-nginx:/etc/nginx:rw  nginx

（3）ro

只要看到ro就说明这个路径只能通过宿主机来改变，容器内部是无法操作的

## 8.DockerFile

### 8.1.前言

Dockfile是一种被Docker程序解释的脚本，Dockerfile由一条一条的指令组成，每条指令对应Linux下面的一条命令。Docker程序将这些Dockerfile指令翻译真正的Linux命令。Dockerfile有自己书写格式和支持的命令，Docker程序解决这些命令间的依赖关系，类似于Makefile。Docker程序将读取Dockerfile，根据指令生成定制的image。相比image这种黑盒子，Dockerfile这种显而易见的脚本更容易被使用者接受，它明确的表明image是怎么产生的。有了Dockerfile，当我们需要定制自己额外的需求时，只需在Dockerfile上添加或者修改指令，重新生成image即可，省去了敲命令的麻烦。

### 8.2 Dockerfile的书写规则及指令使用方法

Dockerfile的指令是忽略大小写的，建议使用大写，使用`#`作为注释，每一行只支持一条指令，每条指令可以携带多个参数。

Dockerfile的指令根据作用可以分为两种，**构建指令和设置指令**。构建指令用于构建image，其指定的操作不会在运行image的容器上执行；设置指令用于设置image的属性，其指定的操作将在运行image的容器中执行。

**(1). FROM（指定基础image）**

构建指令，必须指定且需要在Dockerfile其他指令的前面。后续的指令都依赖于该指令指定的image。FROM指令指定的基础image可以是官方远程仓库中的，也可以位于本地仓库。

该指令有两种格式：

> FROM <image>

指定基础image为该image的最后修改的版本。或者：

> FROM <image>:<tag>

指定基础image为该image的一个tag版本。

**(2). MAINTAINER（用来指定镜像创建者信息）**

构建指令，用于将image的制作者相关的信息写入到image中。当我们对该image执行docker inspect命令时，输出中有相应的字段记录该信息。

指令格式：

> MAINTAINER <name>

**(3). RUN（安装软件用）**

构建指令，RUN可以运行任何被基础image支持的命令。如基础image选择了ubuntu，那么软件管理部分只能使用ubuntu的命令。

- RUN命令将在当前image中执行任意合法命令并提交执行结果。命令执行提交后，就会自动执行Dockerfile中的下一个指令。
- 层级 RUN 指令和生成提交是符合Docker核心理念的做法。它允许像版本控制那样，在任意一个点，对image 镜像进行定制化构建。
- RUN 指令缓存不会在下个命令执行时自动失效。比如 RUN apt-get dist-upgrade -y 的缓存就可能被用于下一个指令. --no-cache 标志可以被用于强制取消缓存使用。

指令格式：

> RUN <command> (the command is run in a shell - `/bin/sh -c`)
>  RUN ["executable", "param1", "param2" ... ]  (exec form)

**(4). CMD（设置container启动时执行的操作）**

设置指令，用于container启动时指定的操作。该操作可以是执行自定义脚本，也可以是执行系统命令。该指令只能在文件中存在一次，如果有多个，则只执行最后一条。

该指令有三种格式：

> CMD ["executable","param1","param2"] (like an exec, this is the preferred form)
>  CMD command param1 param2 (as a shell)

当Dockerfile指定了ENTRYPOINT，那么使用下面的格式：

> CMD ["param1","param2"] (as default parameters to ENTRYPOINT)

ENTRYPOINT指定的是一个可执行的脚本或者程序的路径，该指定的脚本或者程序将会以param1和param2作为参数执行。所以如果CMD指令使用上面的形式，那么Dockerfile中必须要有配套的ENTRYPOINT。

**(5). ENTRYPOINT（设置container启动时执行的操作）**

设置指令，指定容器启动时执行的命令，可以多次设置，但是只有最后一个有效。

两种格式:

> ENTRYPOINT ["executable", "param1", "param2"] (like an exec, the preferred form)
>  ENTRYPOINT command param1 param2 (as a shell)

该指令的使用分为两种情况，一种是独自使用，另一种和CMD指令配合使用。

当独自使用时，如果你还使用了CMD命令且CMD是一个完整的可执行的命令，那么CMD指令和ENTRYPOINT会互相覆盖只有最后一个CMD或者ENTRYPOINT有效。



```bash
# CMD指令将不会被执行，只有ENTRYPOINT指令被执行  
CMD echo “Hello, World!”  
ENTRYPOINT ls -l  
```

另一种用法和CMD指令配合使用来指定ENTRYPOINT的默认参数，这时CMD指令不是一个完整的可执行命令，仅仅是参数部分；ENTRYPOINT指令只能使用JSON方式指定执行命令，而不能指定参数。



```objectivec
FROM ubuntu  
CMD ["-l"]  
ENTRYPOINT ["/usr/bin/ls"]  
```

**(6). USER（设置container容器的用户）**

设置指令，设置启动容器的用户，默认是root用户。



```bash
# 指定memcached的运行用户  
ENTRYPOINT ["memcached"]  
USER daemon  
或  
ENTRYPOINT ["memcached", "-u", "daemon"]  
```

**(7). EXPOSE（指定容器需要映射到宿主机器的端口）**

设置指令，该指令会将容器中的端口映射成宿主机器中的某个端口。当你需要访问容器的时候，可以不是用容器的IP地址而是使用宿主机器的IP地址和映射后的端口。要完成整个操作需要两个步骤，首先在Dockerfile使用EXPOSE设置需要映射的容器端口，然后在运行容器的时候指定-p选项加上EXPOSE设置的端口，这样EXPOSE设置的端口号会被随机映射成宿主机器中的一个端口号。也可以指定需要映射到宿主机器的那个端口，这时要确保宿主机器上的端口号没有被使用。EXPOSE指令可以一次设置多个端口号，相应的运行容器的时候，可以配套的多次使用-p选项。

指令格式：

> EXPOSE <port> [<port>...]



```bash
# 映射一个端口  
EXPOSE port1  
# 相应的运行容器使用的命令  
docker run -p port1 image  
  
# 映射多个端口  
EXPOSE port1 port2 port3  
# 相应的运行容器使用的命令  
docker run -p port1 -p port2 -p port3 image  
# 还可以指定需要映射到宿主机器上的某个端口号  
docker run -p host_port1:port1 -p host_port2:port2 -p host_port3:port3 image  
```

端口映射是docker比较重要的一个功能，原因在于我们每次运行容器的时候容器的IP地址不能指定而是在桥接网卡的地址范围内随机生成的。宿主机器的IP地址是固定的，我们可以将容器的端口的映射到宿主机器上的一个端口，免去每次访问容器中的某个服务时都要查看容器的IP的地址。对于一个运行的容器，可以使用docker port加上容器中需要映射的端口和容器的ID来查看该端口号在宿主机器上的映射端口。

**(8). ENV（用于设置环境变量）**

- ENV指令可以用于为docker容器设置环境变量
- ENV设置的环境变量，可以使用docker inspect命令来查看。同时还可以使用docker run --env <key>=<value>来修改环境变量。

格式：

> ENV <key> <value>

设置了后，后续的RUN命令都可以使用，container启动后，可以通过docker inspect查看这个环境变量，也可以通过在docker run --env key=value时设置或修改环境变量。

假如你安装了JAVA程序，需要设置JAVA_HOME，那么可以在Dockerfile中这样写：



```undefined
ENV JAVA_HOME /path/to/java/dirent
```

**(9). ADD（从src复制文件到container的dest路径）**

构建指令，所有拷贝到container中的文件和文件夹权限为0755，uid和gid为0；如果是一个目录，那么会将该目录下的所有文件添加到container中，不包括目录；如果文件是可识别的压缩格式，则docker会帮忙解压缩（注意压缩格式）；如果<src>是文件且<dest>中不使用斜杠结束，则会将<dest>视为文件，<src>的内容会写入<dest>；如果<src>是文件且<dest>中使用斜杠结束，则会<src>文件拷贝到<dest>目录下。

格式：

> ADD <src> <dest>

- <src> 是相对被构建的源目录的相对路径，可以是文件或目录的路径，也可以是一个远程的文件url;
- <dest> 是container中的绝对路径

**(10). VOLUME (指定挂载点)**

创建一个可以从本地主机或其他容器挂载的挂载点，一般用来存放数据库和需要保持的数据等。

Volume设置指令，使容器中的一个目录具有持久化存储数据的功能，该目录可以被容器本身使用，也可以共享给其他容器使用。我们知道容器使用的是AUFS，这种文件系统不能持久化数据，当容器关闭后，所有的更改都会丢失。当容器中的应用有持久化数据的需求时可以在Dockerfile中使用该指令。

格式：

> VOLUME ["<mountpoint>"]

例如：



```csharp
FROM base  
VOLUME ["/tmp/data"] 
```

运行通过该Dockerfile生成image的容器，/tmp/data目录中的数据在容器关闭后，里面的数据还存在。例如另一个容器也有持久化数据的需求，且想使用上面容器共享的/tmp/data目录，那么可以运行下面的命令启动一个容器：



```csharp
docker run -t -i -rm -volumes-from container1 image2 bash 
```

说明：container1为第一个容器的ID，image2为第二个容器运行image的名字。

**(11). WORKDIR（切换目录）**

设置指令，可以多次切换(相当于cd命令)，对RUN,CMD,ENTRYPOINT生效。

格式：

> WORKDIR /path/to/workdir

示例：



```bash
# 在 /p1/p2 下执行 vim a.txt  
WORKDIR /p1 
WORKDIR p2 
RUN vim a.txt  
```

**(12). ONBUILD（在子镜像中执行）**

ONBUILD 指定的命令在构建镜像时并不执行，而是在它的子镜像中执行。

格式：

> ONBUILD <Dockerfile关键字>

**(13). COPY(复制本地主机的src文件为container的dest)**

复制本地主机的src文件（为Dockerfile所在目录的相对路径、文件或目录 ）到container的dest。目标路径不存在时，会自动创建。

格式：

> COPY <src> <dest>

当使用本地目录为源目录时，推荐使用COPY

**(14). ARG(设置构建镜像时变量)**

ARG指令在Docker1.9版本才加入的新指令，ARG 定义的变量只在建立 image 时有效，建立完成后变量就失效消失

格式：

> ARG <key>=<value>

**(15). LABEL(定义标签)**

定义一个 image 标签 Owner，并赋值，其值为变量 Name 的值。

格式：

> LABEL Owner=$Name

### 8.3创建Dockerfile，构建运行环境

Dockerfile文件



```bash
# 指定基于的基础镜像
FROM ubuntu:13.10  

# 维护者信息
MAINTAINER zhangjiayang "zhangjiayang@sczq.com.cn"  
  
# 镜像的指令操作
# 获取APT更新的资源列表
RUN echo "deb http://archive.ubuntu.com/ubuntu precise main universe"> /etc/apt/sources.list
# 更新软件
RUN apt-get update  
  
# Install curl  
RUN apt-get -y install curl  
  
# Install JDK 7  
RUN cd /tmp &&  curl -L 'http://download.oracle.com/otn-pub/java/jdk/7u65-b17/jdk-7u65-linux-x64.tar.gz' -H 'Cookie: oraclelicense=accept-securebackup-cookie; gpw_e24=Dockerfile' | tar -xz  
RUN mkdir -p /usr/lib/jvm  
RUN mv /tmp/jdk1.7.0_65/ /usr/lib/jvm/java-7-oracle/  
  
# Set Oracle JDK 7 as default Java  
RUN update-alternatives --install /usr/bin/java java /usr/lib/jvm/java-7-oracle/bin/java 300     
RUN update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/java-7-oracle/bin/javac 300     

# 设置系统环境
ENV JAVA_HOME /usr/lib/jvm/java-7-oracle/  
  
# Install tomcat7  
RUN cd /tmp && curl -L 'http://archive.apache.org/dist/tomcat/tomcat-7/v7.0.8/bin/apache-tomcat-7.0.8.tar.gz' | tar -xz  
RUN mv /tmp/apache-tomcat-7.0.8/ /opt/tomcat7/  
  
ENV CATALINA_HOME /opt/tomcat7  
ENV PATH $PATH:$CATALINA_HOME/bin  

# 复件tomcat7.sh到容器中的目录 
ADD tomcat7.sh /etc/init.d/tomcat7  
RUN chmod 755 /etc/init.d/tomcat7  
  
# Expose ports.  指定暴露的端口
EXPOSE 8080  
  
# Define default command.  
ENTRYPOINT service tomcat7 start && tail -f /opt/tomcat7/logs/catalina.out
```

tomcat7.sh命令文件



```bash
export JAVA_HOME=/usr/lib/jvm/java-7-oracle/  
export TOMCAT_HOME=/opt/tomcat7  
  
case $1 in  
start)  
  sh $TOMCAT_HOME/bin/startup.sh  
;;  
stop)  
  sh $TOMCAT_HOME/bin/shutdown.sh  
;;  
restart)  
  sh $TOMCAT_HOME/bin/shutdown.sh  
  sh $TOMCAT_HOME/bin/startup.sh  
;;  
esac  
exit 0  
```

### 8.4构建镜像

根据配置完的dockerfile构建Docker镜像，并启动docker容器。



```css
docker build -t wechat-tomcat.  
docker run -d -p 8090:8080 wechat-tomcat 
```

默认情况下，tomcat会占用8080端口，所以在启动container的时候，指定了 -p 8090:8080，映射到宿主机端口就是8090。

### 8.5Dockerfile参考示例

示例1：构建Wordpress + nginx运行环境



```csharp
# 指定基于的基础镜像
FROM ubuntu:14.04

# 维护者信息
MAINTAINER Eugene Ware <eugene@noblesamurai.com>

# Keep upstart from complaining
RUN dpkg-divert --local --rename --add /sbin/initctl
RUN ln -sf /bin/true /sbin/initctl

# Let the conatiner know that there is no tty
ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update
RUN apt-get -y upgrade

# Basic Requirements
RUN apt-get -y install mysql-server mysql-client nginx php5-fpm php5-mysql php-apc pwgen python-setuptools curl git unzip

# Wordpress Requirements
RUN apt-get -y install php5-curl php5-gd php5-intl php-pear php5-imagick php5-imap php5-mcrypt php5-memcache php5-ming php5-ps php5-pspell php5-recode php5-sqlite php5-tidy php5-xmlrpc php5-xsl

# mysql config， 配置MySQL运行参数
RUN sed -i -e"s/^bind-address\s*=\s*127.0.0.1/bind-address = 0.0.0.0/" /etc/mysql/my.cnf

# nginx config， 配置Nginx运行参数
RUN sed -i -e"s/keepalive_timeout\s*65/keepalive_timeout 2/" /etc/nginx/nginx.conf
RUN sed -i -e"s/keepalive_timeout 2/keepalive_timeout 2;\n\tclient_max_body_size 100m/" /etc/nginx/nginx.conf
RUN echo "daemon off;" >> /etc/nginx/nginx.conf

# php-fpm config
RUN sed -i -e "s/;cgi.fix_pathinfo=1/cgi.fix_pathinfo=0/g" /etc/php5/fpm/php.ini
RUN sed -i -e "s/upload_max_filesize\s*=\s*2M/upload_max_filesize = 100M/g" /etc/php5/fpm/php.ini
RUN sed -i -e "s/post_max_size\s*=\s*8M/post_max_size = 100M/g" /etc/php5/fpm/php.ini
RUN sed -i -e "s/;daemonize\s*=\s*yes/daemonize = no/g" /etc/php5/fpm/php-fpm.conf
RUN sed -i -e "s/;catch_workers_output\s*=\s*yes/catch_workers_output = yes/g" /etc/php5/fpm/pool.d/www.conf
RUN find /etc/php5/cli/conf.d/ -name "*.ini" -exec sed -i -re 's/^(\s*)#(.*)/\1;\2/g' {} \;

# nginx site conf，将本地Nginx配置文件复制到容器中的目录
ADD ./nginx-site.conf /etc/nginx/sites-available/default

# Supervisor Config
RUN /usr/bin/easy_install supervisor
RUN /usr/bin/easy_install supervisor-stdout
ADD ./supervisord.conf /etc/supervisord.conf

# Install Wordpress
ADD https://wordpress.org/latest.tar.gz /usr/share/nginx/latest.tar.gz
RUN cd /usr/share/nginx/ && tar xvf latest.tar.gz && rm latest.tar.gz
RUN mv /usr/share/nginx/html/5* /usr/share/nginx/wordpress
RUN rm -rf /usr/share/nginx/www
RUN mv /usr/share/nginx/wordpress /usr/share/nginx/www
RUN chown -R www-data:www-data /usr/share/nginx/www

# Wordpress Initialization and Startup Script
ADD ./start.sh /start.sh
RUN chmod 755 /start.sh

# private expose
EXPOSE 3306
EXPOSE 80

# volume for mysql database and wordpress install
VOLUME ["/var/lib/mysql", "/usr/share/nginx/www"]

# 容器启动时执行命令
CMD ["/bin/bash", "/start.sh"]
```

示例2：构建Ruby on Rails环境



```bash
# 指定基础镜像
FROM fcat/ubuntu-universe:12.04

# development tools
RUN apt-get -qy install git vim tmux

# ruby 1.9.3 and build dependencies
RUN apt-get -qy install ruby1.9.1 ruby1.9.1-dev build-essential libpq-dev libv8-dev libsqlite3-dev

# bundler
RUN gem install bundler

# create a "rails" user
# the Rails application will live in the /rails directory
RUN adduser --disabled-password --home=/rails --gecos "" rails

# copy the Rails app
# we assume we have cloned the "docrails" repository locally
#  and it is clean; see the "prepare" script
ADD docrails/guides/code/getting_started /rails

# Make sure we have rights on the rails folder
RUN chown rails -R /rails

# copy and execute the setup script
# this will run bundler, setup the database, etc.
ADD scripts/setup /setup
RUN su rails -c /setup

# copy the start script
ADD scripts/start /start

EXPOSE 3000

# 创建用户
USER rails

# 设置容器启动命令
CMD /start
```

示例3： 构建Nginx运行环境



```ruby
# 指定基础镜像
FROM sameersbn/ubuntu:14.04.20161014

# 维护者信息
MAINTAINER sameer@damagehead.com

# 设置环境
ENV RTMP_VERSION=1.1.10 \
    NPS_VERSION=1.11.33.4 \
    LIBAV_VERSION=11.8 \
    NGINX_VERSION=1.10.1 \
    NGINX_USER=www-data \
    NGINX_SITECONF_DIR=/etc/nginx/sites-enabled \
    NGINX_LOG_DIR=/var/log/nginx \
    NGINX_TEMP_DIR=/var/lib/nginx \
    NGINX_SETUP_DIR=/var/cache/nginx

# 设置构建时变量，镜像建立完成后就失效
ARG BUILD_LIBAV=false
ARG WITH_DEBUG=false
ARG WITH_PAGESPEED=true
ARG WITH_RTMP=true

# 复制本地文件到容器目录中
COPY setup/ ${NGINX_SETUP_DIR}/
RUN bash ${NGINX_SETUP_DIR}/install.sh

# 复制本地配置文件到容器目录中
COPY nginx.conf /etc/nginx/nginx.conf
COPY entrypoint.sh /sbin/entrypoint.sh

# 运行指令
RUN chmod 755 /sbin/entrypoint.sh

# 允许指定的端口
EXPOSE 80/tcp 443/tcp 1935/tcp

# 指定网站目录挂载点
VOLUME ["${NGINX_SITECONF_DIR}"]

ENTRYPOINT ["/sbin/entrypoint.sh"]
CMD ["/usr/sbin/nginx"]
```

示例4：构建Postgres镜像



```bash
# 指定基础镜像
FROM sameersbn/ubuntu:14.04.20161014

# 维护者信息
MAINTAINER sameer@damagehead.com

# 设置环境变量
ENV PG_APP_HOME="/etc/docker-postgresql"\
    PG_VERSION=9.5 \
    PG_USER=postgres \
    PG_HOME=/var/lib/postgresql \
    PG_RUNDIR=/run/postgresql \
    PG_LOGDIR=/var/log/postgresql \
    PG_CERTDIR=/etc/postgresql/certs

ENV PG_BINDIR=/usr/lib/postgresql/${PG_VERSION}/bin \
    PG_DATADIR=${PG_HOME}/${PG_VERSION}/main

# 下载PostgreSQL
RUN wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | apt-key add - \
 && echo 'deb http://apt.postgresql.org/pub/repos/apt/ trusty-pgdg main' > /etc/apt/sources.list.d/pgdg.list \
 && apt-get update \
 && DEBIAN_FRONTEND=noninteractive apt-get install -y acl \
      postgresql-${PG_VERSION} postgresql-client-${PG_VERSION} postgresql-contrib-${PG_VERSION} \
 && ln -sf ${PG_DATADIR}/postgresql.conf /etc/postgresql/${PG_VERSION}/main/postgresql.conf \
 && ln -sf ${PG_DATADIR}/pg_hba.conf /etc/postgresql/${PG_VERSION}/main/pg_hba.conf \
 && ln -sf ${PG_DATADIR}/pg_ident.conf /etc/postgresql/${PG_VERSION}/main/pg_ident.conf \
 && rm -rf ${PG_HOME} \
 && rm -rf /var/lib/apt/lists/*

COPY runtime/ ${PG_APP_HOME}/
COPY entrypoint.sh /sbin/entrypoint.sh
RUN chmod 755 /sbin/entrypoint.sh

# 指定端口
EXPOSE 5432/tcp

# 指定数据挂载点
VOLUME ["${PG_HOME}", "${PG_RUNDIR}"]

# 切换目录
WORKDIR ${PG_HOME}

# 设置容器启动时执行命令
ENTRYPOINT ["/sbin/entrypoint.sh"]
```

具体用例可以参考Github的Docker文件相应的示例。

- [Github Docker文件参考](https://link.jianshu.com?t=https://github.com/sameersbn?tab=repositories)

### 8.6Dockerfile最佳实践

- 使用.dockerignore文件

为了在docker build过程中更快上传和更加高效，应该使用一个.dockerignore文件用来排除构建镜像时不需要的文件或目录。例如,除非.git在构建过程中需要用到，否则你应该将它添加到.dockerignore文件中，这样可以节省很多时间。

- 避免安装不必要的软件包

为了降低复杂性、依赖性、文件大小以及构建时间，应该避免安装额外的或不必要的包。例如，不需要在一个数据库镜像中安装一个文本编辑器。

- 每个容器都跑一个进程

在大多数情况下，一个容器应该只单独跑一个程序。解耦应用到多个容器使其更容易横向扩展和重用。如果一个服务依赖另外一个服务，可以参考 [Linking Containers Together](https://link.jianshu.com?t=https://docs.docker.com/engine/userguide/networking/default_network/dockerlinks/)。

- 最小化层

我们知道每执行一个指令，都会有一次镜像的提交，镜像是分层的结构，对于 Dockerfile，应该找到可读性和最小化层之间的平衡。

- 多行参数排序

如果可能，通过字母顺序来排序，这样可以避免安装包的重复并且更容易更新列表，另外可读性也会更强，添加一个空行使用 \ 换行:



```csharp
RUN apt-get update && apt-get install -y \
  bzr \
  cvs \
  git \
  mercurial \
  subversion
```

- 创建缓存

镜像构建过程中会按照 Dockerfile 的顺序依次执行，每执行一次指令 Docker 会寻找是否有存在的镜像缓存可复用，如果没有则创建新的镜像。如果不想使用缓存，则可以在docker build 时添加`--no-cache=true`选项。

从基础镜像开始就已经在缓存中了，下一个指令会对比所有的子镜像寻找是否执行相同的指令，如果没有则缓存失效。在大多数情况下只对比 Dockerfile 指令和子镜像就足够了。ADD 和 COPY 指令除外，执行 ADD 和 COPY 时存放到镜像的文件也是需要检查的，完成一个文件的校验之后再利用这个校验在缓存中查找，如果检测的文件改变则缓存失效。RUN apt-get -y update命令只检查命令是否匹配，如果匹配就不会再执行更新了。

> 为了有效地利用缓存，你需要保持你的Dockerfile一致，并且尽量在末尾修改。

