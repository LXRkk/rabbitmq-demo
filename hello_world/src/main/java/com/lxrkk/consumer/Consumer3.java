package com.lxrkk.consumer;

import com.lxrkk.util.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 消费者2 处理死信队列
 * 死信队列
 *
 * @author : LXRkk
 * @date : 2024/12/11 15:32
 */
public class Consumer3 {
    //死信队列名称
    public static final String DEAD_QUEUE = "dead_queue";
    //死信交换机名称
    public static final String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] args) {
        //获取信道
        Channel channel = RabbitMQUtils.getChannel();
        try {
            //声明死信交换机
            channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
            //声明死信队列
            channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
            //队列交换机绑定
            String deadRoutingKey = "lisi";
            channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, deadRoutingKey);
            System.out.println("Consumer3 正在等待消息……");
            //消费消息
                // 消费消息回调接口
            DeliverCallback deliverCallback = (consumerTag,delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("Consumer3 接收到消息：" + message);
            };
            channel.basicConsume(DEAD_QUEUE,true,deliverCallback, consumerTag -> {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
