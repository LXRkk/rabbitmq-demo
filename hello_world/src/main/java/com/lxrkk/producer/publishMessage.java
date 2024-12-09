package com.lxrkk.producer;

import com.lxrkk.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 发布确认
 *  耗时对比
 *      1.单个确认
 *      2.批量确认
 *      3.异步确认
 *
 * @author : LXRkk
 * @date : 2024/12/9 16:26
 */
public class publishMessage {
    // 消息发送数量
    private static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) {
        //1.单个确认
        //publishMessageIndividually(); //发布1000个单独确认消息，耗时：601ms
        //2.批量确认
        //publishMessageBatch(); //发布1000个批量确认消息，耗时：150ms
        //3.异步确认
        publishMessageAsync(); // 发布1000个异步确认消息，耗时：38ms；发布1000个异步确认消息，耗时：50ms
    }

    // 单个确认发布
    public static void publishMessageIndividually() {
        // 获取信道
        Channel channel = RabbitMQUtils.getChannel();
        try {
            // 开启发布确认
            channel.confirmSelect();
            // 声明队列
            // 队列名称
            String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName,true,false,false,null);
            long begin = System.currentTimeMillis();
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String message = i + "";
                channel.basicPublish("",queueName,null,message.getBytes());
                // 返回 false 或超时，消息重发
                boolean result = channel.waitForConfirms();
                if (result) {
                    System.out.println("消息发送成功！");
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时：" + (end - begin) + "ms");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 批量确认发布
    public static void publishMessageBatch() {
        // 获取信道
        Channel channel = RabbitMQUtils.getChannel();
        try {
            // 开启发布确认
            channel.confirmSelect();
            // 声明队列
            // 队列名称
            String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName,true,false,false,null);
            // 批量确认大小
            int batchSize = 100;
            long begin = System.currentTimeMillis();
            for (int i = 1; i <= MESSAGE_COUNT; i++) {
                String message = i + "";
                channel.basicPublish("",queueName,null,message.getBytes());
                if (i % batchSize == 0) {
                    // 返回 false 或超时，消息重发
                    boolean result = channel.waitForConfirms();
                    if (result) {
                        System.out.println("第" + i + "条消息发送成功！");
                    }
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时：" + (end - begin) + "ms");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 异步确认发布
    public static void publishMessageAsync() {
        // 获取信道
        Channel channel = RabbitMQUtils.getChannel();
        try {
            // 开启发布确认
            channel.confirmSelect();
            // 声明队列
            // 队列名称
            String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName,true,false,false,null);
            /**
             * 线程安全、有序的哈希集合；适用于高并发的场景
             * 能够轻松将序号和消息进行关联
             * 只要有序号，能够批量删除条目
             */
            ConcurrentSkipListMap<Long, String> concurrentSkipListMap = new ConcurrentSkipListMap<>();
            /**
             * 消息确认的 回调函数
             * 参数：1.消息的标记 2.是否为批量确认
             */
            ConfirmCallback ackCallback = (deliveryTag,multiple) -> {
                // 删掉确认的消息，剩余未确认的消息
                if (multiple) {
                    ConcurrentNavigableMap<Long, String> confirmedMessageMap = concurrentSkipListMap.headMap(deliveryTag);
                    confirmedMessageMap.clear(); // 批量删除【易出现消息丢失】
                }else {
                    concurrentSkipListMap.remove(deliveryTag); // 单个删除【推荐】
                }
            };
            /**
             * 消息未确认的 回调函数
             * 参数：1.消息的标记 2.是否批量为批量确认
             */
            ConfirmCallback nackCallback = (deliveryTag,multiple) -> System.out.println("消息 tag 为:" + deliveryTag + " 的消息未被确认！");
            /**
             * 添加一个异步确认的监听器
             * 参数：1.监听确认的消息 2.监听未确认的消息
             */
            channel.addConfirmListener(ackCallback,nackCallback); //异步通知
            long begin = System.currentTimeMillis();
            // 批量发送消息
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String message = i + "";
                channel.basicPublish("",queueName,null,message.getBytes());
                // 记录发送的信息
                concurrentSkipListMap.put(channel.getNextPublishSeqNo(),message);
            }
            long end = System.currentTimeMillis();
            System.out.println("发布" + MESSAGE_COUNT + "个异步确认消息，耗时：" + (end - begin) + "ms");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
