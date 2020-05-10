package com.wzp.module.user.demo.rabbitmq.spring;

import com.rabbitmq.client.Channel;
import com.wzp.module.core.config.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class DirectConsumer {

    String template = "%s 队列收到消息：%s";

    @RabbitListener(queues = RabbitMQConfig.DIRECT_QUEUE_1)
    @RabbitHandler
    public void directConsumerOne(Message message, Channel channel) throws IOException {
        println(RabbitMQConfig.DIRECT_QUEUE_1,new String(message.getBody(), StandardCharsets.UTF_8));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        /**
         * 不确认消息
         * @param: deliveryTag 该消息的index
         * @param: multiple 是否批量 true将一次性拒绝所有小于deliveryTag的消息
         * @param: requeue 被拒绝的是否重新入队列
         */
//        channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        /**
         * 拒绝消息
         * channel.basicNack 与 channel.basicReject 的区别在于basicNack可以拒绝多条消息，而basicReject一次只能拒绝一条消息
         * @param: requeue 被拒绝的是否重新入队列
         */
//        channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(queues = RabbitMQConfig.DIRECT_QUEUE_2)
    @RabbitHandler
    public void directConsumerTwo(@Payload String body,Channel channel,@Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        println(RabbitMQConfig.DIRECT_QUEUE_2,body);
        channel.basicAck(deliveryTag,false);
    }

    private void println(String queue,String body) {
        System.out.println(String.format(template,queue,body));
    }
}
