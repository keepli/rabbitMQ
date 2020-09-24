import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class B {
    public static void main(String[] args) throws IOException, TimeoutException, IOException, TimeoutException {
        // 声明一个连接工厂
        ConnectionFactory factory = new ConnectionFactory ();
        factory.setHost ( "121.196.161.193" ); //主机，不设置默认为localhost
        factory.setPort ( 5672 );//端口，不设置默认为5672
        factory.setVirtualHost ( "/itcast" );//虚拟主机，不设置默认为/
        factory.setUsername ( "itcast" );//用户名，不设置默认为guest
        factory.setPassword ( "itcast" );//密码，不设置默认为guest
        // 创建一个与rabbitmq服务器的连接
        Connection connection = factory.newConnection();
        // 创建一个Channel
        Channel channel = connection.createChannel();
        // 通过Channel定义队列
        channel.queueDeclare("multiple", false, false, false, null);
        // 异步回调处理
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("multiple Received '" + message + "'" + delivery.getEnvelope().getDeliveryTag());
            if (delivery.getEnvelope().getDeliveryTag() % 3 == 0) {
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), true);
            }
        };
        // 接收消息
        channel.basicConsume("multiple", false, deliverCallback, consumerTag -> {
        });
    }
}
