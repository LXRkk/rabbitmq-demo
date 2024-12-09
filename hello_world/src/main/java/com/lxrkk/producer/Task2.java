package com.lxrkk.producer;

import com.lxrkk.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 生产者 消息手动应答时不丢失、未应答重新入队
 *
 * @author : LXRkk
 * @date : 2024/12/7 20:13
 */
public class Task2 {

    // 队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) {
        // 获取信道
        Channel channel = RabbitMQUtils.getChannel();
        try {
            // 开启发布确认
            channel.confirmSelect();
            // 声明队列
            boolean durable = true; // 消息队列持久化
            channel.queueDeclare(TASK_QUEUE_NAME,durable,false,false,null);
            // 控制台输入信息
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                System.out.println("请输入内容：");
                String message = scanner.nextLine();
                // 消息持久化 MessageProperties.PERSISTENT_TEXT_PLAIN
                channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes(StandardCharsets.UTF_8));
                System.out.println(message + " 已发送！");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
