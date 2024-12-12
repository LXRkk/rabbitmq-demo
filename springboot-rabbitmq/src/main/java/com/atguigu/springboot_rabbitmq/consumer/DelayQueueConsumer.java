package com.atguigu.springboot_rabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 *基于插件的延迟队列消费者
 *
 * @author : LXRkk
 * @date : 2024/12/12 21:44
 */
@Component
@Slf4j
public class DelayQueueConsumer {
    @RabbitListener(queues = "delayed.queue")
    public void receiveD(Message message) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("{},来自延迟队列的消息:{} ", new Date(),msg);
    }
}
