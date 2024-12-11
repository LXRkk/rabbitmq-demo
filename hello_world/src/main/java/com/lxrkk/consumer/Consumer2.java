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
 * 消费者1
 * 死信队列
 *
 * @author : LXRkk
 * @date : 2024/12/11 15:32
 */
public class Consumer2 {
    //正常队列名称
    public static final String NORMAL_QUEUE = "normal_queue";
    //死信队列名称
    public static final String DEAD_QUEUE = "dead_queue";
    //正常交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
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
            //声明正常交换机
            channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
            Map<String, Object> properties = new HashMap<>();
            //配置死信交换机
            properties.put("x-dead-letter-exchange", DEAD_EXCHANGE);
            //配置死信RoutingKey
            properties.put("x-dead-letter-routing-key", deadRoutingKey);
            //设置正常队列长度的限制
            //properties.put("x-max-length",6);
            //声明正常队列
            channel.queueDeclare(NORMAL_QUEUE, false, false, false, properties);
            //队列交换机绑定
            String normalRoutingKey = "zhangsan";
            channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, normalRoutingKey);
            System.out.println("Consumer2 正在等待消息……");
            //消费消息
            // 消费消息回调接口
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                if ("消息5".equals(message)) {
                    //拒绝该消息,不重新入队
                    channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
                    System.out.println(message + "被 Consumer2 拒绝接收");
                } else {
                    System.out.println("Consumer2 接收到消息：" + message);
                }
            };
            //开启手动应答
            boolean ack = false;
            channel.basicConsume(NORMAL_QUEUE, ack, deliverCallback, consumerTag -> {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
