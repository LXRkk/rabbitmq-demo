package com.lxrkk.producer;

import com.lxrkk.util.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * 生产者
 * topic 交换机
 *
 * @author : LXRkk
 * @date : 2024/12/10 20:36
 */
public class EmitLogTopic {
    // 交换机名称
    public static final String EXCHAGE_NAME = "topic_logs";

    public static void main(String[] args) {
        // 获取信道
        Channel channel = RabbitMQUtils.getChannel();
        try {
            // 声明交换机
            channel.exchangeDeclare(EXCHAGE_NAME, BuiltinExchangeType.TOPIC);
            HashMap<String, String> routingKeyMessageMap = new HashMap<>();
            routingKeyMessageMap.put("quick.orange.rabbit","被队列 Q1Q2 接收到");
            routingKeyMessageMap.put("lazy.orange.elephant","被队列 Q1Q2 接收到");
            routingKeyMessageMap.put("quick.orange.fox","被队列 Q1 接收到");
            routingKeyMessageMap.put("lazy.brown.fox","被队列 Q2 接收到");
            routingKeyMessageMap.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2 接收一次");
            routingKeyMessageMap.put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
            routingKeyMessageMap.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
            routingKeyMessageMap.put("lazy.orange.male.rabbit","是四个单词但匹配 Q2");
            for (String routingKey : routingKeyMessageMap.keySet()) {
                String message = routingKeyMessageMap.get(routingKey);
                channel.basicPublish(EXCHAGE_NAME,routingKey , null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println(message + "已发送！");
            }
            // 发送消息
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
