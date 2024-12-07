package com.lxrkk.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者 接收消息
 *
 * @author : LXRkk
 * @date : 2024/12/7 16:15
 */
public class Consumer1 {
    private static final String QUEUE_NAME = "hello";
    private static final String HOST = "10.159.0.101";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    public static void main(String[] args) {
        // 创建一个连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置工厂地址
        connectionFactory.setHost(HOST);
        // 设置用户名
        connectionFactory.setUsername(USERNAME);
        // 设置密码
        connectionFactory.setPassword(PASSWORD);
        // 推送的消息如何进行消费的接口回调
        DeliverCallback deliverCallback = (consumerTag,delivery) -> {
            String message = new String(delivery.getBody());
            System.out.println(message);
        };
        // 取消消费的一个回调接口，如在消费的时候队列背删除了
        CancelCallback cancelCallback = consumerTag -> System.out.println("消息接收被中断！");
        try {
            // 创建连接
            Connection connection = connectionFactory.newConnection();
            // 创建信道
            Channel channel = connection.createChannel();
            /**
             * 消费者接收消息
             * basicConsume()参数解释：1.消费哪个队列
             * 2.消费成功之后是否自动应答？true 代表自动应答；false 代表手动应答
             * 3.消费未成功成功接口回调 4.消费取消的接口回调
             */
            channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
