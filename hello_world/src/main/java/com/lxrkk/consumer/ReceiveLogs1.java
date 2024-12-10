package com.lxrkk.consumer;

import com.lxrkk.util.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 发布 / 订阅（fanout）消费者1
 *
 * @author : LXRkk
 * @date : 2024/12/10 15:43
 */
public class ReceiveLogs1 {
    // 交换机名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) {
        Channel channel = RabbitMQUtils.getChannel();
        try {
            channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
            /**
             * 生成一个临时队列，队列的名称是随机的
             * 当消费者断开和该队列的连接时 队列自动删除
             */
            String queueName = channel.queueDeclare().getQueue();
            // 将临时队列与交换机绑定，RoutingKey 为空字符串
            channel.queueBind(queueName,EXCHANGE_NAME,"");
            System.out.println("消息接收者 1 正等待消息……");
            // 接收消息
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("接收到消息：" + message);
            };
            // 未接收消息回调接口
            CancelCallback cancelCallback = consumerTag -> {};
            channel.basicConsume(queueName,false,deliverCallback,cancelCallback);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
