package com.atguigu.springboot_rabbitmq.consumer;

import com.atguigu.springboot_rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 发布确认高级消费者
 *
 * @author : LXRkk
 * @date : 2024/12/13 15:37
 */
@Component
@Slf4j
public class ConfirmConsumer {

    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveConfirmMessage(Message message) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("消费者消费消息：{}",msg);
    }
}
