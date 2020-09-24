package cn.itcast.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

@Component
public class DlxListener implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        //测试拒绝签收

        //1.获取deliveryTag
        long deliveryTag = message.getMessageProperties ( ).getDeliveryTag ( );
        System.out.println (deliveryTag );
        try{
            //2.业务逻辑
            System.out.println ("业务逻辑" );
            //3.签收
            int i = 3/0;//手动制造异常
            channel.basicAck ( deliveryTag,true );
        }catch (Exception e){
            channel.basicNack ( deliveryTag,true,false );
        }

    }
}
