package com.lxrkk.producer;

import com.lxrkk.util.RabbitMQUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 死信队列
 * 生产者
 *
 * @author : LXRkk
 * @date : 2024/12/11 16:07
 */
public class Producer2 {
    //正常交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) {
        Channel channel = RabbitMQUtils.getChannel();
        try {
            channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
            // 设置消息的 TTL（过期时间）单位 ms
            //AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
            String normalRoutingKey = "zhangsan";
            //该信息用作演示队列个数限制
            for (int i = 1; i < 11; i++) {
                String message = "消息" + i;
                channel.basicPublish(NORMAL_EXCHANGE, normalRoutingKey, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println(message + "已发送！");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
