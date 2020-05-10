package com.wzp.module.user.demo.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Topic--生产者
 *
 * 生产者将消息发送到topic类型的交换器上，和routing的用法类似，都是通过routingKey路由，但topic类型交换器的routingKey支持通配符
 */
public class Producer {

    public static void main(String[] args) {
        // 1、创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 2、设置连接属性
        factory.setHost("server.ip");
        factory.setPort(5672);
        factory.setUsername("wangziping");
        factory.setPassword("wangziping");

        Connection connection = null;
        Channel channel = null;

        try {
            // 3、从连接工厂获取连接
            connection = factory.newConnection("生产者");

            // 4、从链接中创建通道
            channel = connection.createChannel();

            // 路由关系如下：com.# --> queue-1     *.order.* ---> queue-2
            // 消息内容
            String message = "Hello 1";
            // 发送消息到topic_test交换器上
            channel.basicPublish("topic-exchange", "topic.queue.1", null, message.getBytes());
            System.out.println("消息 " + message + " 已发送！");

            // 消息内容
            message = "Hello 2";
            // 发送消息到topic_test交换器上
            channel.basicPublish("topic-exchange", "topic.key", null, message.getBytes());
            System.out.println("消息 " + message + " 已发送！");

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            // 7、关闭通道
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }

            // 8、关闭连接
            if (connection != null && connection.isOpen()) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
