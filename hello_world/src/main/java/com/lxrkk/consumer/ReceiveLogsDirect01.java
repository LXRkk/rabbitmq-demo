package com.lxrkk.consumer;

import com.lxrkk.util.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 消费者1
 * 交换机类型：direct
 *
 * @author : LXRkk
 * @date : 2024/12/10 17:03
 */
public class ReceiveLogsDirect01 {
    // 交换机名称
    public static final String EXCHAGE_NAME = "direct_logs";

    public static void main(String[] args) {
        Channel channel = RabbitMQUtils.getChannel();
        try {
            // 声明交换机
            channel.exchangeDeclare(EXCHAGE_NAME, BuiltinExchangeType.DIRECT);
            // 声明队列
            String queueName = "disk";
            channel.queueDeclare(queueName, false, false, false, null);
            // 绑定交换机
            String routingKey = "error";
            channel.queueBind(queueName, EXCHAGE_NAME, routingKey);
            System.out.println("ReceiveLogsDirect01 正在等待消息……");
            DeliverCallback deliverCallback = (consumerTag,delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("接收到消息：" + message);
            };
            CancelCallback cancelCallback = consumerTag -> {};
            channel.basicConsume(queueName,false,deliverCallback,cancelCallback);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}