package com.lxrkk.producer;

import com.lxrkk.enums.RoutingKey;
import com.lxrkk.util.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 *生成者
 * 交换机类型：direct
 * @author : LXRkk
 * @date : 2024/12/10 17:18
 */
public class EmitLogDirect {
    // 交换机名称
    public static final String EXCHAGE_NAME = "direct_logs";

    public static void main(String[] args) {
        Channel channel = RabbitMQUtils.getChannel();
        System.out.print("请输入内容：");
        try {
            // 声明交换机
            channel.exchangeDeclare(EXCHAGE_NAME, BuiltinExchangeType.DIRECT);
            String routingKey = "error";
            String message = RoutingKey.ERROR.getValue();
            channel.basicPublish(EXCHAGE_NAME,routingKey,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println(message + "已发送！");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
