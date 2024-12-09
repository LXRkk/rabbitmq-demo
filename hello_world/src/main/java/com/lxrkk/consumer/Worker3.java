package com.lxrkk.consumer;

import com.lxrkk.util.RabbitMQUtils;
import com.lxrkk.util.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 消费者 消息手动应答时不丢失、未应答重新入队
 *
 * @author : LXRkk
 * @date : 2024/12/7 20:39
 */
public class Worker3 {
    // 队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) {
        // 获取信道
        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("消费者 1 处理消息效率较高，等待消息中……");
        // 消息消费的时候如何处理消息
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            // 模拟消息处理，睡眠 1 秒
            SleepUtils.sleep(1L);
            System.out.println("接收到消息：" + message);
            /**
             * basicAck()参数解释：
             * 1.消息标记 Tag 2.是否批量应答未应答消息
             */
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        // 消费者取消消费接口回调逻辑
        CancelCallback cancelCallback = consumerTag -> System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        try {
            // 设置不公平分发（之前是轮训分发）
            //int prefetchCount = 1;
            // 预取值
            int prefetchCount = 2;
            channel.basicQos(prefetchCount);
            // 采用手动应答
            boolean autoAck = false;
            channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
