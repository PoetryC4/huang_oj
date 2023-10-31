package com.yioj.commentservice;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
public class InitRabbitMq {
    public static void doInit() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            String exchange_name = "yioj_exchange";
            channel.exchangeDeclare(exchange_name, "direct");
            String queueName = "yioj_queue";
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchange_name, "yioj_routingKey");
            log.error("RabbitMQ启动成功");
        } catch (IOException | TimeoutException e) {
            log.error("RabbitMQ启动失败");
            throw new RuntimeException(e);
        }
    }
}
