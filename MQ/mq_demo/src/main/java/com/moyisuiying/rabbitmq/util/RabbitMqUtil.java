package com.moyisuiying.rabbitmq.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Classname:RabbitMqUtil
 *
 * @description: rabbitmq的工具类
 * @author: 陌意随影
 * @Date: 2021-06-13 17:40
 * @Version: 1.0
 **/
public class RabbitMqUtil {

    public static Connection getConnection(String host,int port,String virtualHost,String userName,String password){
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置主机地址,默认为 localhost
        connectionFactory.setHost(host);
        //设置连接端口,默认为5672
        connectionFactory.setPort(port);
        //设置虚拟主机名,默认为 /
        connectionFactory.setVirtualHost(virtualHost);
        //设置连接用户名和密码，默认为 guest:guest
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
