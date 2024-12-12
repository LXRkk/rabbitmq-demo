package com.atguigu.springboot_rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * 基于死信的延迟队列配置类
 *
 * @author : LXRkk
 * @date : 2024/12/12 15:03
 */
@Configuration
public class TtlQueueConfig {
    public static final String X_EXCHANGE = "X";
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    public static final String DEAD_LETTER_QUEUE_D = "QD";
    //普通队列
    public static final String QUEUE_C = "QC";

    //声明 x 交换机
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }
    //声明 Y 交换机
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }
    //声明 QA 队列
    @Bean("queueA")
    public Queue queueA() {
        HashMap<String, Object> argumentsMap = new HashMap<>();
        //配置当前队列绑定的死信交换机
        argumentsMap.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //配置当前队列的死信 RoutingKey
        argumentsMap.put("x-dead-letter-routing-key","YD");
        //配置队列的 TTL 10s
        argumentsMap.put("x-message-ttl",10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(argumentsMap).build();
    }
    //QA 绑定 X 交换机
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,@Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }
    // 声明队列 QB
    @Bean("queueB")
    public Queue queueB() {
        HashMap<String, Object> argumentsMap = new HashMap<>();
        //为队列 QB 配置死信交换机
        argumentsMap.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //配置 RoutingKey
        argumentsMap.put("x-dead-letter-routing-key","YD");
        //给队列配置 TTL 40s
        argumentsMap.put("x-message-ttl",40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(argumentsMap).build();
    }
    //队列 QB 绑定 x 交换机
    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }
    //声明死信队列 QD
    @Bean("queueD")
    public Queue queueD() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE_D).build();
    }
    //队列 QD 绑定交换机 Y
    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,@Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }

    //声明普通队列 QC,不设置 TTL
    @Bean("queueC")
    public Queue queueC() {
        HashMap<String, Object> argumentsMap = new HashMap<>();
        //配置死信交换机
        argumentsMap.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //配置 RoutingKey
        argumentsMap.put("x-dead-letter-routing-key","YD");
        return QueueBuilder.durable(QUEUE_C).withArguments(argumentsMap).build();
    }
    //绑定 x 交换机
    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }
}
