package com.lxrkk.producer;

import com.lxrkk.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;

/**
 * 生产者 发送大量消息
 *
 * @author : LXRkk
 * @date : 2024/12/7 19:28
 */
public class Task1 {

    // 队列名称
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        // 获取信道
        Channel channel = RabbitMQUtils.getChannel();
        try {
            // 声明队列
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String message = scanner.next();
                channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
                System.out.println(message + " 已发送");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
