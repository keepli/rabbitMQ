package cn.itcast.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener implements ChannelAwareMessageListener {

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
            System.out.println ("根据id查询订单状态" );
            System.out.println ("判断订单是否为支付成功" );
            channel.basicAck ( deliveryTag,true );
        }catch (Exception e){
            System.out.println ("出现异常拒绝签收" );
            channel.basicNack ( deliveryTag,true,true );
        }

    }
}
