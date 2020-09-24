package cn.itcast.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 接收信息的消费者
 */
public class Consumer_Topic2 {
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

        //5.接收消息
        /*
        basicConsume(String queue, boolean autoAck, Consumer callback)
        参数：
            1.queue：队列名称
            2.autoAck：是否自动确认（确认消息是否收到）
            3.callback：回调对象
         */

        Consumer consumer = new DefaultConsumer ( channel ){
            /*
            回调方法：当收到消息后，自动执行该方法
                参数：
                    1.consumerTag：消息的标识
                    2.envelope：获取一些信息，交换机，路由key...
                    3.properties：配置信息
                    4.body：数据
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                /*super.handleDelivery ( consumerTag, envelope, properties, body );
                System.out.println ("ConsumerTag：" + consumerTag );
                System.out.println ("Exchange：" + envelope.getExchange () );
                System.out.println ("RoutingKey：" + envelope.getRoutingKey () );
                System.out.println ("Properties：" + properties );*/
                System.out.println ("Body：" + new String ( body ) );
                System.out.println ("将日志信息打印到控制台" );
            }
        };
        String queueName2 = "test_topic_queueName2";
        channel.basicConsume ( queueName2,true,consumer);

        //消费者不要关闭资源，因为它要时刻监听消息
    }
}
