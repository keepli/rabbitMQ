package cn.itcast.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareBatchMessageListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
/**
 * Consumer ACK机制：
 *  1. 设置手动签收。acknowledge="manual"
 *  2. 让监听器类实现ChannelAwareMessageListener接口
 *  3. 如果消息成功处理，则调用channel的 basicAck()签收
 *  4. 如果消息处理失败，则调用channel的basicNack()拒绝签收，broker重新发送给consumer
 *
 *
 */
@Component
public class AckListener implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        Thread.sleep ( 1200 );

        long deliveryTag = message.getMessageProperties ( ).getDeliveryTag ( );
        System.out.println (deliveryTag );

        try {
            //1.接收消息
            System.out.println (new String ( message.getBody () ) );

            //2.处理业务逻辑
            System.out.println ( "处理业务逻辑" );
            //int i = 3/0; //手动模拟异常

            //3.收到签收
            channel.basicAck ( deliveryTag,true );
        } catch (Exception e) {
            //e.printStackTrace ( );

            //4.出现异常拒绝签收
             /*
            第三个参数：requeue：重回队列。如果设置为true，则消息重新回到queue，broker会重新发送该消息给消费端
             */
            channel.basicNack ( deliveryTag,true,true );
        }
    }
}
