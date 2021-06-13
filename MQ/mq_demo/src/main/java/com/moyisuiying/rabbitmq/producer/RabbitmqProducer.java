package com.moyisuiying.rabbitmq.producer;

import com.moyisuiying.rabbitmq.util.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Classname:Rabbitmq
 *
 * @description:测试rabbitmq
 * @author: 陌意随影
 * @Date: 2021-06-13 16:09
 * @Version: 1.0
 **/
public class RabbitmqProducer{
   //mq队列的名称
    private static final  String QUEUE_NAME  = "queue_test";
    public static void main(String[] args) {
        Connection connection = null;
        Channel channel = null;
        try {
            //创建连接
            connection = RabbitMqUtil.getConnection("121.4.41.89",5672,"/","guest","guest");
             //创建channel
             channel = connection.createChannel();
            //声明队列 String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
            /**
             * queue:队列名称
             * durable： 是否持久化队列
             * exclusive： 是否独占本次连接
             * autoDelete： 是否自动删除
             * arguments：队列的其他参数
             **/
            channel.queueDeclare(QUEUE_NAME,true,false,false,null);
            //定义发送的消息
            String message = "hello moyisuying 222! ";
            // String exchange, String routingKey, boolean mandatory, boolean immediate, BasicProperties props, byte[] body
            /*
            * exchange : 交换机的名字
            * routingKey: 路由的key，简单模式时候可以传递队列名称
            * mandatory： 是否强制发送
            * immediate： 是否立即发送
            * props：队列的其他属性
            * body：发送消息的二进制字节
            * */
            channel.basicPublish("",QUEUE_NAME,false,false,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("已发送消息：" + message);
        } catch (Exception e) {
           e.printStackTrace();
        }finally {
            try {
                channel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
