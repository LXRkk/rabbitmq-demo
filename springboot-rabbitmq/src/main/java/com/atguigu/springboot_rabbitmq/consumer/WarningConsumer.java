package com.atguigu.springboot_rabbitmq.consumer;

import com.atguigu.springboot_rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 备份交换机消费者（告警）
 *
 * @author : LXRkk
 * @date : 2024/12/13 19:48
 */
@Component
@Slf4j
public class WarningConsumer {

    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWarningQueue(Message msg) {
        String message = new String(msg.getBody(), StandardCharsets.UTF_8);
        log.warn("不可路由信息：{}", message);
    }
}
