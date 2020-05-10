package com.wzp.module.user.demo.rabbitmq.direct;

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

            // 发送消息
            channel.basicPublish("direct-exchange","direct-key-1",null,"你好，direct-1".getBytes());
            System.out.println("消息：你好，direct-1 发送成功");

            channel.basicPublish("direct-exchange","direct-key-2",null,"你好，direct-2".getBytes());
            System.out.println("消息：你好，direct-2 发送成功");

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
