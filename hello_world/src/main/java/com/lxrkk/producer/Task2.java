package com.lxrkk.producer;

import com.lxrkk.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;

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
            // 声明队列
            channel.queueDeclare(TASK_QUEUE_NAME,false,false,false,null);
            // 控制台输入信息
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                System.out.println("请输入内容：");
                String message = scanner.nextLine();
                channel.basicPublish("",TASK_QUEUE_NAME,null,message.getBytes(StandardCharsets.UTF_8));
                System.out.println(message + " 已发送！");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
