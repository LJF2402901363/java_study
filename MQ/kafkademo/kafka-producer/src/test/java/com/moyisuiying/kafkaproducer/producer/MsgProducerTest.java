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
