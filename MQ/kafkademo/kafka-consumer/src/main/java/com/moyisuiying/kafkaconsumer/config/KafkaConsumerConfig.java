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