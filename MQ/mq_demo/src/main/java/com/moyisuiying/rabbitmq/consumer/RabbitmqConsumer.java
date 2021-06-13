package com.moyisuiying.rabbitmq.consumer;

import com.moyisuiying.rabbitmq.util.RabbitMqUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * Classname:RabbitmqConsumer
 *
 * @description: 测试消费者
 * @author: 陌意随影
 * @Date: 2021-06-13 16:59
 * @Version: 1.0
 **/
public class RabbitmqConsumer {
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
            //声明一个队列：String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
            channel.queueDeclare(QUEUE_NAME,true,false,false,null);
            //创建消费者
            DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
                /**
                 * @Description :处理消息
                 * @Date 17:16 2021/6/13 0013
                 * @Param * @param consumerTag  消息者标签，在channel.basicConsume时候可以指定
                 * @param envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志(收到消息失败后是否需要重新发送)
                 * @param properties 属性信息
                 * @param body ： 消息
                 * @return void
                 **/
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String routingKey = envelope.getRoutingKey();
                    String exchange = envelope.getExchange();
                    long deliveryTag = envelope.getDeliveryTag();
                    System.out.println("路由key:"+routingKey);
                    System.out.println("交换机为：" + exchange);
                    System.out.println("消息id"+ deliveryTag);
                    //处理接收到的消息
                    String message = new String(body, StandardCharsets.UTF_8);
                    System.out.println("接收到的消息为：" + message);

                }
            };
            //监听消息 String queue, boolean autoAck, Consumer callback
            /**
             * queue：队列名称
             * autoAck：是否自动确认，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置为false则需要手动确认
             * callback：消息接收到后回调
             */
            channel.basicConsume(QUEUE_NAME,true,defaultConsumer);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //不关闭资源，应该一直监听消息
            //channel.close();
            //connection.close();
        }


    }
}
