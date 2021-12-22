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
