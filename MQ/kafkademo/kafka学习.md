# kafka学习

## 1.kafka的简介

[大白话kafka架构](https://mp.weixin.qq.com/s?__biz=MzU1NDA0MDQ3MA==&mid=2247483958&idx=1&sn=dffaad318b50f875eea615bc3bdcc80c&chksm=fbe8efcfcc9f66d9ff096fbae1c2a3671f60ca4dc3e7412ebb511252e7193a46dcd4eb11aadc&scene=21#wechat_redirect)

[秒懂kafka HA(高可用)](https://mp.weixin.qq.com/s?__biz=MzU1NDA0MDQ3MA==&mid=2247483965&idx=1&sn=20dd02c4bf3a11ff177906f0527a5053&chksm=fbe8efc4cc9f66d258c239fefe73125111a351d3a4e857fd8cd3c98a5de2c18ad33aacdad947&scene=21#wechat_redirect)

## 2.Docker安装kafka集群（单机版）

[使用Docker-compose安装](https://hub.docker.com/r/bitnami/kafka)



### 2.1书写docker-compose.yml文件

```yaml
version: '3'
# 创建自定义网络
networks:
  kafka-zook-net:
    driver: bridge
services:
  # zookeeper服务
  zookeeper:
    image: 'bitnami/zookeeper:latest'
    container_name: zookeeper
    ports:
      - '2181:2181'
    environment:
      - ALLOW_ANONYMOUS_LOGIN=yes
    networks:
      - kafka-zook-net
  # kafka服务
  kafka:
    image: 'bitnami/kafka:latest'
    container_name:kafka
    ports:
      - '9092:9092'
      - '9093:9093'
    environment:
      - KAFKA_BROKER_ID=1
      - KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181
      - ALLOW_PLAINTEXT_LISTENER=yes
      - KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CLIENT:PLAINTEXT,EXTERNAL:PLAINTEXT
      # 允许内部和外部连接
      - KAFKA_CFG_LISTENERS=CLIENT://:9092,EXTERNAL://:9093
      # 内部，外部代理地址
      - KAFKA_CFG_ADVERTISED_LISTENERS=CLIENT://kafka:9092,EXTERNAL://localhost:9093
      - KAFKA_CFG_INTER_BROKER_LISTENER_NAME=CLIENT
    depends_on:
      - zookeeper
    networks:
      - kafka-zook-net
  kafka-manager:
    image: kafkamanager/kafka-manager
    container_name: kafka-manager
    depends_on:
      - zookeeper
      - kafka
    environment:
      ZK_HOSTS: zookeeper:2181 
    ports:
      - 9000:9000
    links:
      - zookeeper
      - kafka
    networks:
      - kafka-zook-net

```



### 2.2构建容器

```bash
#构建容器 
docker-compose up -d
```

### 2.3测试kafka

```bash
# 进入kafka容器，注意要加上  -u root以root用户访问，或者指定一个用户
docker exec -it -u root kafka bash
# 进入容器后，创建一个topic，名称为test
kafka-console-producer.sh --broker-list 127.0.0.1:9093 --topic test
```

![image-20211221145252432](https://gitee.com/ljf2402901363/picgo-images/raw/master/typora/20211221145331.png)





```bash
# 消费test这个topic的消息
 kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9093 --topic test --from-beginning
```





> 
>
> **Using Non-Root Containers as Root Containers**
>
> If you wish to run a Bitnami non-root container image as a root container image, you can do so by adding the line *user: root* right after the *image:* directive in the container's *docker-compose.yml*. After making this change, simply restart the container and it will run as the *root* user with all privileges instead of an unprivileged user



### 2.3服务器开放端口

### 2.4访问kafka manager

```html
http://服务器ip:9000/
```

## 3.springboot整合kafka

采用父子工程（父工程kafkademo,子工程kafkaproducer和kafkaconsumer）

### 3.1父工程kafkademo的pom.xml引入依赖

父工程kafkademo控制版本依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.6.1</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.moyisuiying</groupId>
    <artifactId>kafkademo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>kafkademo</name>
    <packaging>pom</packaging>
    <description>Demo project for Spring Boot</description>
    <modules>
        <module>kafka-producer</module>
        <module>kafka-consumer</module>
    </modules>
    <properties>
        <java.version>11</java.version>
        <spring.version>2.6.1</spring.version>
        <spring.kafka.version>2.8.1</spring.kafka.version>
        <lombok.version>1.18.16</lombok.version>

    </properties>

   <dependencyManagement>
       <dependencies>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter</artifactId>
           </dependency>
           <dependency>
               <groupId>org.springframework.kafka</groupId>
               <artifactId>spring-kafka</artifactId>
           </dependency>

           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-test</artifactId>
               <scope>test</scope>
           </dependency>
           <dependency>
               <groupId>org.springframework.kafka</groupId>
               <artifactId>spring-kafka-test</artifactId>
               <scope>test</scope>
           </dependency>
           <dependency>
               <groupId>org.projectlombok</groupId>
               <artifactId>lombok</artifactId>
           </dependency>
       </dependencies>

   </dependencyManagement>

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

### 3.2kafkaproducer工程的配置和代码书写

#### 3.2.1的pom.xml引入依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <artifactId>kafkademo</artifactId>
        <groupId>com.moyisuiying</groupId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <groupId>com.example</groupId>
    <artifactId>kafka-producer</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>kafka-producer</name>
    <description>Demo project for Spring Boot</description>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
            <version>${spring.kafka.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <version>${spring.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka-test</artifactId>
            <version>${spring.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
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

#### 3.2.2kafkaproducer的application.yml配置文件

```yaml
spring:
  kafka:
    producer:
      # kafka集群的地址的ip:port对，多个则以逗号分开
      bootstrap-servers: 111.229.11.173:9093
      # 生产者重试次数
      retries: 2
      # 批量大小
      batch-size: 16384
      # 应答级别:多少个分区副本备份完成时向生产者发送ack确认(可选0、1、all/-1)
      acks: 1
      # 提交延时,当生产端积累的消息达到batch-size或接收到消息linger.ms后,生产者就会将消息提交给kafka
      # linger.ms为0表示每接收到一条消息就提交给kafka,这时候batch-size其实就没用了
      properties:
        linger:
          ms: 0
      # 生产端缓冲区大小
      buffer-memory: 33554432
    listener:
      # 消费端监听的topic不存在时，项目启动会报错(关掉)
      missing-topics-fatal: false

```

#### 3.2.3producer的config

```java
package com.moyisuiying.kafkaproducer.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Classname:KafkaProducerConfig
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-12-21 16:45
 * @Version: 1.0
 **/
@Configuration
@EnableKafka
public class KafkaProducerConfig {
    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String servers;
    @Value("${spring.kafka.producer.retries}")
    private int retries;
    @Value("${spring.kafka.producer.batch-size}")
    private int batchSize;
    @Value("${spring.kafka.producer.buffer-memory}")
    private int bufferMemory;


    public Map<String, Object> byteProducerConfigs() {
        Map<String, Object> props = basicProducerConfigs();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        return props;
    }

    public Map<String, Object> stringProducerConfigs() {
        Map<String, Object> props = basicProducerConfigs();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    public Map<String, Object> basicProducerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.RETRIES_CONFIG, retries);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        return props;
    }
    public ProducerFactory<String, String> stringProducerFactory() {
        return new DefaultKafkaProducerFactory<>(stringProducerConfigs());
    }

    @Bean
    public KafkaTemplate<String, String> stringKafkaTemplate() {

        return new KafkaTemplate<>(stringProducerFactory());
    }

    public ProducerFactory<String, Byte[]> byteProducerFactory() {
        return new DefaultKafkaProducerFactory<>(byteProducerConfigs());
    }

    @Bean
    public KafkaTemplate<String, Byte[]> byteKafkaTemplate() {
        return new KafkaTemplate<>(byteProducerFactory());
    }
}

```

#### 3.2.4消息的生产者

```java
package com.moyisuiying.kafkaproducer.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Classname:MsgProducer
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-12-21 16:12
 * @Version: 1.0
 **/
@Component
@Slf4j
public class MsgProducer {
    @Resource(name = "stringKafkaTemplate")
    private KafkaTemplate<String,String> stringKafkaTemplate;
    public void sendMsg(String topic,String msg){
        ListenableFuture<SendResult<String, String>> resultListenableFuture = stringKafkaTemplate.send(topic, msg);
        resultListenableFuture.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("发送失败：{}", ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("发送到主题：{}成功！", result.getProducerRecord().value());
            }
        });
        try {
            // 因为是异步发送，所以我们等待，最多10s
            resultListenableFuture.get(2L, TimeUnit.SECONDS);

        } catch (InterruptedException | ExecutionException | TimeoutException | java.util.concurrent.TimeoutException e) {
            log.error("waiting for kafka send finish failed!", e);
        }

    }


}

```

#### 3.2.5测试类

```java
package com.moyisuiying.kafkaproducer.producer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Classname:MsgProducerTest
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-12-21 21:30
 * @Version: 1.0
 **/
@SpringBootTest
public class MsgProducerTest {
    @Autowired
    private MsgProducer msgProducer;
    @Test
    public void testSendMsg(){
        msgProducer.sendMsg("test","我爱你");
    }
}

```

### 3.3kafkaconsumer工程的配置和代码书写

#### 3.3.1kafkaconsumer的 pom.xml引入依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <artifactId>kafkademo</artifactId>
        <groupId>com.moyisuiying</groupId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <groupId>com.moyisuiying</groupId>
    <artifactId>kafka-consumer</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>kafka-consumer</name>
    <description>Demo project for Spring Boot</description>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
            <version>${spring.kafka.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <version>${spring.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka-test</artifactId>
            <version>${spring.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
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

#### 3.3.2kafkaconsumer的application.yml配置文件

```yaml
spring:
  kafka:
    # 默认消费组
    consumer:
      # kafka集群的地址的ip:port对，多个则以逗号分开
      bootstrap-servers: 111.229.11.173:9093
      client-id: test
      # 当kafka中没有初始offset或offset超出范围时将自动重置offset
      # earliest:重置为分区中最小的offset;
      # latest:重置为分区中最新的offset(消费分区中新产生的数据);
      # none:只要有一个分区不存在已提交的offset,就抛出异常
      auto-offset-reset: latest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      # 消费者个数
      concurrency: 3
      group-id: consumer_test
        # kafka提供的序列化和反序列化类
      key-serializer: org.apache.kafka.common.serialization.StringDeserializer
      value-serializer: org.apache.kafka.common.serialization.StringDeserializer
    listener:
      # 消费端监听的topic不存在时，项目启动会报错(关掉)
      missing-topics-fatal: false

```

#### 3.3.3consumer的config

```java
package com.moyisuiying.kafkaconsumer.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String servers;
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;
    @Value("${spring.kafka.consumer.client-id}")
    private String clientId;
    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;
    @Value("${spring.kafka.consumer.concurrency}")
    private int concurrency;
    @Value("${spring.kafka.consumer.key-serializer}")
    private String key_serializer;
    @Value("${spring.kafka.consumer.key-serializer}")
    private String value_serializer;
    /**
     * @Description :手动提交的监听器工厂 (使用的消费组工厂必须 kafka.consumer.enable-auto-commit = false)
     * @Date 0:35 2021/12/22 0022
     * @Param * @param  ：
     * @return ConcurrentMessageListenerContainer
     **/
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaUnAutoCommitListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaManualConsumerFactory());
        factory.setConcurrency(concurrency);
        //设置提交偏移量的方式 当Acknowledgment.acknowledge()侦听器调用该方法时，立即提交偏移量
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setPollTimeout(1500);
        return factory;
    }

    /**
     * @Description :自动提交的监听器工厂 (使用的消费组工厂必须 kafka.consumer.enable-auto-commit = true)
     * @Date 0:35 2021/12/22 0022
     * @Param * @param  ：
     * @return ConcurrentMessageListenerContainer
     **/
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaAutoCommitListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setPollTimeout(1500);
        return factory;
    }
    /**
     * @Description :自动提交的ConsumerFactory
     * 创建多个工厂的时候 SpringBoot就不会自动帮忙创建工厂了；所以默认的还是自己创建一下
     * @Date 0:43 2021/12/22 0022
     * @Param * @param  ：
     * @return org.springframework.kafka.core.ConsumerFactory<java.lang.String,java.lang.String>
     **/
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(autoCommitConsumerConfigs());
    }
   /**
    * @Description :禁止自动提交的ConsumerFactory
    * @Date 0:43 2021/12/22 0022
    * @Param * @param  ：
    * @return org.springframework.kafka.core.ConsumerFactory<java.lang.Object,java.lang.Object>
    **/
    @Bean
    public ConsumerFactory<Object, Object> kafkaManualConsumerFactory() {
        DefaultKafkaConsumerFactory<Object, Object> factory = new DefaultKafkaConsumerFactory<>( unAutoCommitConsumerConfigs());
        return factory;
    }
    /**
     * @Description :禁止自动提交的配置map
     * @Date 0:44 2021/12/22 0022
     * @Param * @param  ：
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public Map<String, Object> unAutoCommitConsumerConfigs() {
        Map<String, Object> propsMap = basicConsumerFactoryProperties();
        //禁止自动提交
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return propsMap;
    }
    /**
     * @Description :自动提交的配置map
     * @Date 0:44 2021/12/22 0022
     * @Param * @param  ：
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public Map<String, Object> autoCommitConsumerConfigs() {
        Map<String, Object> propsMap = basicConsumerFactoryProperties();
        //自动提交
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        return propsMap;
    }
    /**
     * @Description :基础配置map
     * @Date 0:44 2021/12/22 0022
     * @Param * @param  ：
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public Map<String, Object> basicConsumerFactoryProperties() {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, key_serializer);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, value_serializer);
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        propsMap.put(ConsumerConfig.CLIENT_ID_CONFIG,clientId);
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        return propsMap;
    }
}
```

#### 3.3.4消息的消费者

```java
package com.moyisuiying.kafkaconsumer.consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Classname:MsgConsumer
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-12-21 16:33
 * @Version: 1.0
 **/
@Component
@Slf4j
public class MsgConsumer {
    /**
     * @Description :接收到消息后手动提交
     * @Date 0:23 2021/12/22 0022
     * @Param * @param record 接收到的消息
     * @param acknowledgment ：确认接收
     * @return void
     **/
    @KafkaListener(topics = "test",containerFactory = "kafkaUnAutoCommitListenerContainerFactory")
    public void handlerMessage(ConsumerRecord record, Acknowledgment acknowledgment){
        try {
            //手动接收消息
            String value = (String) record.value();
            log.info("手动接收<<接收到消息,进行消费>>:{}",value);
        } catch (Exception e) {
            log.error("手动接收<<消费异常信息>>>:{}",e.getMessage());
        }finally {
            //最终提交确认接收到消息  手动提交 offset
            acknowledgment.acknowledge();
        }
    }
}

```

#### 3.3.5测试类

```java
package com.moyisuiying.kafkaconsumer.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * Classname:MyConsumerTesst
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-12-21 17:32
 * @Version: 1.0
 **/
@SpringBootTest
public class MyConsumerTest {

    @Test
    public void  testGetMsg(){
    }
}

```

### 
