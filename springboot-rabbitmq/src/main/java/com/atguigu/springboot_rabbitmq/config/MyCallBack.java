package com.atguigu.springboot_rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 消费者不管是否接收到消息 回调接口
 *
 * @author : LXRkk
 * @date : 2024/12/13 15:58
 */
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostConstruct //注入，调用该实现类的方法
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     *交换机确认消息回调方法
     * @param correlationData 保存回调信息的 id 及相关信息
     * @param b 交换机是否接收到消息
     * @param s 未接收到消息的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        String id = correlationData == null ? "" : correlationData.getId();
        if (b) {
            log.info("交换机接收到 id 为{}的消息",id);
        }else {
            log.info("交换机没有接收到 id 为{}的消息，原因是：{}",id,s);
        }
    }

    /**
     * 当消息在传递过程中不能到达目的地时，消息会返回给生产者
     * @param returnedMessage 返回消息相关信息
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        String message = new String(returnedMessage.getMessage().getBody());
        log.error("消息：{} 被交换机：{} 退回，退回原因：{}，路由 key：{}",message,
                returnedMessage.getExchange(),
                returnedMessage.getReplyText(),
                returnedMessage.getRoutingKey());
    }
}
