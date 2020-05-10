package com.wzp.module.user.controller;

import com.wzp.module.core.config.RabbitMQConfig;
import com.wzp.module.core.dto.ResultDataModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RabbitmqController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/open/rabbitmq/direct")
    public ResultDataModel rabbitmqDirect(@RequestParam("massage") String massage,@RequestParam("routingKey") String routingKey) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE,routingKey,massage);
        return ResultDataModel.handleSuccessResult();
    }

    @GetMapping("/open/rabbitmq/topic")
    public ResultDataModel rabbitmqTopic(@RequestParam("massage") String massage,@RequestParam("routingKey") String routingKey) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE,routingKey,massage);
        return ResultDataModel.handleSuccessResult();
    }

    @GetMapping("/open/rabbitmq/fanout")
    public ResultDataModel rabbitmqFanout(@RequestParam("massage") String massage) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE,massage);
        return ResultDataModel.handleSuccessResult();
    }


}
