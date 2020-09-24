package cn.itcast.rabbitmq.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class RabbitMQListener {

    @RabbitListener(queues = "hello world")
    public void simple(Message message){
        System.out.println (new String ( message.getBody () ) );
    }

    @RabbitListeners({
            @RabbitListener(queues = "boot_topic_queue1"),
            @RabbitListener(queues = "boot_topic_queue2")})
    public void topic(Message message){
        System.out.println (new String ( message.getBody () ) );
    }

    @RabbitListeners({
            @RabbitListener(queues = "boot_fanout_queue1"),
            @RabbitListener(queues = "boot_fanout_queue2")})
    public void fanout(Message message){
        System.out.println (new String ( message.getBody () ) );
    }

    @RabbitListeners({
            @RabbitListener(queues = "boot_direct_queue1"),
            @RabbitListener(queues = "boot_direct_queue2")})
    public void direct(Message message){
        System.out.println (new String ( message.getBody () ) );
    }
}
