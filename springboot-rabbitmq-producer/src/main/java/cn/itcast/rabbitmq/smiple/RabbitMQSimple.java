package cn.itcast.rabbitmq.smiple;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQSimple {

    public static final String Queue_NAME = "hello world";

    //1.声明队列
    @Bean(Queue_NAME)
    public Queue bootQueue(){
        return QueueBuilder.durable (Queue_NAME).build ();
    }

}
