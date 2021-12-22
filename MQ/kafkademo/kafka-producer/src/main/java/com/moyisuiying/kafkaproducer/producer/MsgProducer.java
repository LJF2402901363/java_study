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
