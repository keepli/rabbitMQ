package cn.itcast.rabbitmq.drict;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQDirect {

    public static final String EXCHANGE_NAME = "boot_direct_exchange";
    public static final String Queue_NAME1 = "boot_direct_queue1";
    public static final String Queue_NAME2 = "boot_direct_queue2";

    //1.交换机
    @Bean(EXCHANGE_NAME)
    public Exchange bootExchange(){
        return ExchangeBuilder.directExchange ( EXCHANGE_NAME ).durable ( true ).build ();
    }

    //2.队列
    @Bean(Queue_NAME1)
    public Queue bootQueue(){
        return QueueBuilder.durable ( Queue_NAME1 ).build ( );
    }

    @Bean(Queue_NAME2)
    public Queue bootQueue2(){
        return QueueBuilder.durable ( Queue_NAME2 ).build ();
    }

    //3.绑定队列和交换机
    /*
        1.队列
        2.交换机
        3.routingKey
     */
    @Bean(Queue_NAME1+EXCHANGE_NAME)
    public Binding bindingQueueExchange(@Qualifier(Queue_NAME1) Queue queue, @Qualifier(EXCHANGE_NAME) Exchange exchange){
        return BindingBuilder.bind ( queue ).to ( exchange ).with ( "error" ).noargs ();
    }

    @Bean(Queue_NAME2+EXCHANGE_NAME)
    public Binding bindingQueueExchange2(@Qualifier(Queue_NAME2) Queue queue, @Qualifier(EXCHANGE_NAME) Exchange exchange){
        return BindingBuilder.bind ( queue ).to ( exchange ).with ( "info" ).noargs ();
    }
}
