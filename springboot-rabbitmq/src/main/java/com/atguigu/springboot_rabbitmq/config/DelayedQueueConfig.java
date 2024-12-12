package com.atguigu.springboot_rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于插件的延迟队列配置类
 *
 * @author : LXRkk
 * @date : 2024/12/12 21:12
 */
@Configuration
public class DelayedQueueConfig {
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    //自定义延迟交换机
    @Bean("delayedExchange")
    public CustomExchange delayedExchange() {
        Map<String, Object> arguments = new HashMap<>();
        //定义交换机的类型
        arguments.put("x-delayed-type","direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME,"x-delayed-message",true,false,arguments);
    }
    //声明延迟队列
    @Bean("delayedQueue")
    public Queue delayedQueue() {
        return QueueBuilder.durable(DELAYED_QUEUE_NAME).build();
    }
    //绑定
    @Bean
    public Binding delayedQueueBindingDelayedExchange(@Qualifier("delayedQueue") Queue delayedQueue,@Qualifier("delayedExchange") CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
