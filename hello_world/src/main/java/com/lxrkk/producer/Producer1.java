package com.lxrkk.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 生产者 发送消息
 *
 * @author : LXRkk
 * @date : 2024/12/7 15:17
 */
public class Producer1 {

    private static final String QUEUE_NAME = "hello";
    private static final String HOST = "10.159.0.101";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    public static void main(String[] args) {
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
            Channel channel = connection.createChannel();
            /**
             * 生成一个队列
             * queueDeclare()参数解释：
             * 1.队列名称 2.队列里面的消息是否持久化（磁盘），默认存在内存中
             * 3.该队列是否只供一个消费者进行消费，true 表示可以有多个消费者消费
             * 4.最后一个消费者断开连接后，是否自动删除，true 表示自动删除
             * 5.其他参数
             */
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            String message = "Hello~,rabbitmq!";
            /**
             * 发送消息
             * 参数解释：1.交换机（""表示使用默认的）2.哪个队列（传入队列的 key）
             * 3.其他信息 4.待发送的消息（需转成 byte 数组）
             */
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("消息发送完毕！");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
