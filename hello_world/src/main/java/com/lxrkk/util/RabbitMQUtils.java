package com.lxrkk.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


/**
 * 此类为创建连接工厂、信道的工具类
 *
 * @author : LXRkk
 * @date : 2024/12/7 16:53
 */
public class RabbitMQUtils {
    private static final String HOST = "10.159.0.101";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    public static Channel getChannel() {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置工厂地址
        connectionFactory.setHost(HOST);
        // 设置用户名
        connectionFactory.setUsername(USERNAME);
        // 设置密码
        connectionFactory.setPassword(PASSWORD);
        try {
            // 创建一个连接
            Connection connection = connectionFactory.newConnection();
            // 创建信道
            return connection.createChannel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
