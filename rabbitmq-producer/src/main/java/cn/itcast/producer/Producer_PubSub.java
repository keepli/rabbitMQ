package cn.itcast.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发送消息的生产者
 */
public class Producer_PubSub {
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

        //5.创建交换机
        /*
        exchangeDeclare(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments)
        参数：
            1.exchange：交换机名称
            2.type：交换机类型（有四种）
                DIRECT("direct")：定向
                FANOUT("fanout")：扇形（广播），发送消息到每一个与之绑定的队列
                TOPIC("topic")：通配符的方式
                HEADERS("headers")：参数匹配（不常用）

            3.durable：是否持久化
            4.autoDelete：自动删除
            5.internal：内部使用，一般都是false
            6.arguments：参数
         */
        String exchangeName = "test_fanout";
        channel.exchangeDeclare ( exchangeName, BuiltinExchangeType.FANOUT, true, false, false, null );

        //6.创建队列
        String queueName1 = "test_fanout_queueName1";
        String queueName2 = "test_fanout_queueName2";
        channel.queueDeclare ( queueName1, true, false, false, null );
        channel.queueDeclare ( queueName2, true, false, false, null );

        //7.绑定队列到交换机
        /*
        queueBind(String queue, String exchange, String routingKey)
        参数：
            1.queue：队列名称
            2.exchange：交换机名称
            3.routingKey：路由键，绑定规则
                注意：如果交换机的类型为fanout，routingKey值设置为"",因为交换机会把消息分发到每个与之绑定的队列
         */
        channel.queueBind ( queueName1, exchangeName, "" );
        channel.queueBind ( queueName2, exchangeName, "" );

        //8.发送消息
        String body = "日志信息：李狗蛋调用了findAll方法... 日志级别：info...";
        channel.basicPublish ( exchangeName, "", null, body.getBytes ( ) );

        //9.释放资源
        channel.close ( );
        conn.close ( );
    }
}
