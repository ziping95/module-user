package com.wzp.module.user.demo.rabbitmq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

    public static void main(String[] args) {

        // 创建一个连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置连接属性
        connectionFactory.setHost("server.ip");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("wangziping");
        connectionFactory.setPassword("wangziping");

        Connection connection = null;
        Channel channel = null;

        try {
            // 获取一个连接
            connection = connectionFactory.newConnection("生产者");

            // 从连接中创建通道
            channel = connection.createChannel();

            /**
             * 声明创建的队列
             * 如果队列不存在，才会创建
             * 通过这种方式创建的队列，后台会自动将自动绑定到一个名称为空的 Direct Exchange上，绑定 RoutingKey 与队列名称相同。
             * @param queue 队列名称
             * @param durable 队列是否持久化
             * @param exclusive 是否排他，即是否为私有的，如果为true，会对当前队列加锁，其他通道不能访问，并且在连接关闭后自动删除
             * @param autoDelete 是否自动删除，当最后一个消费者断开连接之后自动删除
             * @param arguments 队列参数，设置队列的有效期，消息最大长度，队列中所有消息的生命周期等等
             */
            channel.queueDeclare("queue1",false,false,false,null);

            // 消息内容
            String message = "hello world";
            // 由于是简单模式因此exchange传的是空字符串，而routingKey传的是队列名
            // 发送消息
            for (int i = 0; i < 10; i++) {
                channel.basicPublish("","queue1",null,message.getBytes());
            }

        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭通道
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }

            // 关闭连接
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
