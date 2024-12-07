package com.lxrkk.consumer;

import com.lxrkk.util.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

/**
 * 工作线程（消费者）
 *
 * @author : LXRkk
 * @date : 2024/12/7 17:07
 */
public class Worker1 {

    // 队列名称
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        // 获取信道
        Channel channel = RabbitMQUtils.getChannel();
        // 接收消息
        // 接收消息回调接口
        DeliverCallback deliverCallback = (consumerTag,delivery) -> {
            String message = new String(delivery.getBody());
            System.out.println("接收到的消息：" + message);
        };
        // 取消接收消息回调接口
        CancelCallback cancelCallback = consumerTag -> System.out.println(consumerTag + "消费者取消消费 接口回调逻辑");
        System.out.println("工作线程 3 正在等待消息……");
        try {
            channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
