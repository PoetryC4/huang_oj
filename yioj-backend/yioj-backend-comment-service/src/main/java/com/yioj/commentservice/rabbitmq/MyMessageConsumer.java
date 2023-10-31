package com.yioj.commentservice.rabbitmq;

import com.rabbitmq.client.Channel;
import com.yioj.commentservice.service.EmailService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class MyMessageConsumer {
    @Resource
    private EmailService emailService;

    @SneakyThrows
    @RabbitListener(queues = {"yioj_queue"}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        emailService.sendVerificationCode(message);
        channel.basicAck(deliveryTag, false);
        channel.basicNack(deliveryTag, false, true);
    }
}
