package com.lxrkk.producer;

import com.lxrkk.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 发布 / 订阅（fanout）生产者
 *
 * @author : LXRkk
 * @date : 2024/12/10 15:31
 */
public class EmitLog {
    // 交换机名称
    public static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) {
        //获取信道
        Channel channel = RabbitMQUtils.getChannel();
        try {
            /**
             * 声明交换机
             * 参数：1.名称 2. 类型
             */
            channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
            //控制台输入
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String message = scanner.nextLine();
                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println(message + "已发送！");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
