package com.atguigu.springboot_rabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.atguigu.springboot_rabbitmq.config.ConfirmConfig.CONFIRM_EXCHANGE_NAME;
import static com.atguigu.springboot_rabbitmq.config.ConfirmConfig.CONFIRM_ROUTING_KEY;

/**
 * 发布确认高级生产者
 *
 * @author : LXRkk
 * @date : 2024/12/13 15:28
 */
@RestController
@RequestMapping("/confirm")
@Slf4j
public class ConfirmProducerController {

    @Resource
    private RabbitTemplate rabbitTemplate;


    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {
        CorrelationData correlationData = new CorrelationData("1");
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME,CONFIRM_ROUTING_KEY,message + "1",correlationData);
        log.info("消息：{} 已发送！", message);

        CorrelationData correlationData2 = new CorrelationData("2");
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME,CONFIRM_ROUTING_KEY + "2",message + "2",correlationData2);
        log.info("消息：{} 已发送！", message);
    }
}
