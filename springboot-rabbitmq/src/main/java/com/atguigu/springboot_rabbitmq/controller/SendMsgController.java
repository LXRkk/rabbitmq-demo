package com.atguigu.springboot_rabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 生产者
 *
 * @author : LXRkk
 * @date : 2024/12/12 15:48
 */
@RequestMapping("/ttl")
@RestController
@Slf4j
public class SendMsgController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("{},发送一条消息给 QA、QB：{}",new Date(),message);
        rabbitTemplate.convertAndSend("X","XA","来自 ttl 为 10s的队列消息：" + message);
        rabbitTemplate.convertAndSend("X","XB","来自 ttl 为 40s的队列消息：" + message);
    }

    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message, @PathVariable String ttlTime) {
        rabbitTemplate.convertAndSend("X","XC",message,correlationData -> {
            correlationData.getMessageProperties().setExpiration(ttlTime);
            return correlationData;
        });
        log.info("{},发送一条 ttl 为：{}ms信息给 QC 队列：{}",new Date(),ttlTime,message);
    }

    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable String message, @PathVariable Integer delayTime) {
        rabbitTemplate.convertAndSend("delayed.exchange","delayed.routingkey",message,correlationData -> {
            correlationData.getMessageProperties().setDelay(delayTime);
            return correlationData;
        });
        log.info("{},发送一条 ttl 为：{}ms信息给延迟队列：{}",new Date(),delayTime,message);
    }
}
