package com.lxrkk.consumer;

import com.lxrkk.util.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 消费者
 * topic 交换机
 *
 * @author : LXRkk
 * @date : 2024/12/10 20:20
 */
public class ReceiveLogsTopic02 {
    // 交换机名称
    public static final String EXCHAGE_NAME = "topic_logs";

    public static void main(String[] args) {
        //获取信道
        Channel channel = RabbitMQUtils.getChannel();
        try {
            // 声明交换机
            channel.exchangeDeclare(EXCHAGE_NAME, BuiltinExchangeType.TOPIC);
            // 声明队列
            String queueName = "Q2";
            channel.queueDeclare(queueName, false, false, false, null);
            // 队列交换机绑定
            String routingKey = "*.*.rabbit";
            String routingKey2 = "lazy.#";
            channel.queueBind(queueName, EXCHAGE_NAME, routingKey);
            channel.queueBind(queueName, EXCHAGE_NAME, routingKey2);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("队列" + queueName + "接收到消息：" + delivery.getEnvelope().getRoutingKey() + message);
            };
            CancelCallback cancelCallback = consumerTag -> {
            };
            // 消费消息
            channel.basicConsume(queueName, false, deliverCallback, cancelCallback);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
