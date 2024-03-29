package cn.itcast.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发送消息的生产者
 */
public class Producer_WorkQueues {
    public static void main(String[] args) throws IOException, TimeoutException {

        //1.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory ( );

        //2.设置参数
        factory.setHost ( "121.196.161.193" ); //主机，不设置默认为localhost
        factory.setPort ( 5672 );//端口，不设置默认为5672
        factory.setVirtualHost ( "/itcast" );//虚拟主机，不设置默认为/
        factory.setUsername ( "itcast" );//用户名，不设置默认为guest
        factory.setPassword ( "itcast" );//密码，不设置默认为guest

        //3.创建连接
        Connection conn = factory.newConnection ( );

        //4.创建channel
        Channel channel = conn.createChannel ( );

        //5.创建队列queue
        /*
        queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
        参数：
            1.queue: 队列名称
            2.durable: 是否持久化，当mq重启之后还在
            3.exclusive:
                *是否独占，只能有一个消费者监听这个队列
                *当connection关闭时，是否删除队列
            4.autoDelete: 自动删除，当没有consumer时自动删除掉
            5.arguments: 参数
         */
        //如果没有一个名字叫hello_world的队列，则会自动创建一个
        channel.queueDeclare ( "work_queues", true, false, false, null );

        //6.发送消息
        /*
        basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body)
        参数：
            1.exchange: 交换机名称，简单模式使用默认的""
            2.routingKey: 路由信息，如果使用默认的交换机，那么它的名称要跟队列名一致
            3.props: 配置信息
            4.body: 发送的消息数据
         */
        for (int i = 1; i <= 10; i++) {
            String body = i + "hello rabbitmq~";
            channel.basicPublish ( "", "work_queues", null, body.getBytes ( ) );
        }

        //7.释放资源
        channel.close ( );
        conn.close ( );
    }
}
