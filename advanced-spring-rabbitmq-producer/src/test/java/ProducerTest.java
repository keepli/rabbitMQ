import com.rabbitmq.client.BuiltinExchangeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Scanner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-rabbitmq-producer.xml")
public class ProducerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 确认模式：开启后一定会执行
     * 步骤：
     * 1. 确认模式开启：ConnectionFactory中开启publisher-confirms="true"
     * 2. 在rabbitTemplate定义ConfirmCallBack回调函数
     */
    @Test
    public void testConfirm() throws InterruptedException {
        /**
         *
         * @param correlationData 相关配置信息
         * @param ack   exchange交换机 是否成功收到了消息。true 成功，false代表失败
         *              注意事项：
         *                  因为是异步回调，可能在连接关闭后在执行的回调，会导致消息成功发送而ack为false的情况，可以让线程睡眠两秒
         * @param cause 失败原因
         */
        rabbitTemplate.setConfirmCallback ( new RabbitTemplate.ConfirmCallback ( ) {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println ( "confirm方法被执行了" );
                if (ack) {
                    System.out.println ( "接收消息成功：" + cause );
                } else {
                    System.out.println ( "接收消息失败：" + cause );
                    //失败后做一些处理，让消息再次发送
                }
            }
        } );

        rabbitTemplate.convertAndSend ( "test_exchange_confirm", "confirm", "message confirm" );
        //防止明明消息发送成功还出现ack为false的情况
        Thread.sleep ( 2000 );
    }

    /**
     * 回退模式： 当消息发送给Exchange后，Exchange路由到Queue失败时才会执行 ReturnCallBack
     * 步骤：
     * 1. 开启回退模式:publisher-returns="true"
     * 2. 设置ReturnCallBack
     * 3. 设置Exchange处理消息的模式：
     *  1. 如果消息没有路由到Queue，则丢弃消息（默认）
     *  2. 如果消息没有路由到Queue，返回给消息发送方ReturnCallBack
     */

    @Test
    public void testReturn() throws InterruptedException {

        //设置交换机处理失败消息的模式
        rabbitTemplate.setMandatory ( true );

        rabbitTemplate.setReturnCallback ( new RabbitTemplate.ReturnCallback ( ) {
            /**
             *
             * @param message   消息对象
             * @param replyCode 错误码
             * @param replyText 错误信息
             * @param exchange  交换机
             * @param routingKey 路由键
             */

            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println ( "return执行了" );

                System.out.println ( message );
                System.out.println ( replyCode );
                System.out.println ( replyText );
                System.out.println ( exchange );
                System.out.println ( routingKey );

            }
        } );
        //设置不存在的routingKey进行测试
        rabbitTemplate.convertAndSend ( "test_exchange_confirm", "confirm1", "message confirm" );
        Thread.sleep ( 2000 );
    }

    @Test
    public void testSend() {

        for (int i = 1; i <= 10; i++) {
            rabbitTemplate.convertAndSend ( "test_exchange_confirm", "confirm", "message confirm" + i );
        }
    }


    /**
     * TTL:过期时间
     * 1. 队列统一过期
     * <p>
     * 2. 消息单独过期
     * <p>
     * <p>
     * 如果设置了消息的过期时间，也设置了队列的过期时间，它以时间短的为准。
     * 队列过期后，会将队列所有消息全部移除。
     * 消息过期后，只有消息在队列顶端，才会判断其是否过期(移除掉)
     */
    @Test
    public void tesTtl() {

        //消息后处理对象，设置一些消息的参数信息
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor ( ) {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //1.设置Message的信息
                message.getMessageProperties ( ).setExpiration ( "5000" ); //消息的过期时间
                //2.返回该消息
                return message;
            }
        };

        //rabbitTemplate.convertAndSend ( "test_exchange_ttl", "ttl.good", "message ttl", messagePostProcessor );

        for (int i = 1; i <= 10; i++) {
            if (i==5){
                rabbitTemplate.convertAndSend ( "test_exchange_ttl", "ttl.good", "message ttl"+i,
                        messagePostProcessor );
            }else {
                rabbitTemplate.convertAndSend ( "test_exchange_ttl", "ttl.good", "message ttl"+i );
            }
        }
    }


    /**
     * 发送测试死信消息：
     *  1. 过期时间
     *  2. 长度限制
     *  3. 消息拒收
     */
    @Test
    public void tesDlx() {

        //1.测试自动过期
        //rabbitTemplate.convertAndSend ( "test_exchange_dlx","timeOut.dlx","message dlx 死信自动过期" );

        //2.测试长度限制
        /*for (int i = 1; i <= 15; i++) {
            rabbitTemplate.convertAndSend ( "test_exchange_dlx","longMax.dlx","message dlx 死信长度限制" + i );
        }*/

        //3.配合消费端的监听器完成拒绝签收测试
        for (int i = 1; i <= 9; i++) {
            rabbitTemplate.convertAndSend ( "test_exchange_dlx","longMax.dlx","message dlx 死信长度限制" + i );
        }

    }

    /**
     * 延迟队列
     */
    @Test
    public void tesDelay() throws InterruptedException {
        //1.发送订单消息。 将来是在订单系统中，下单成功后，发送消息
        rabbitTemplate.convertAndSend("order_exchange","order.msg","订单信息：id=1,time=2019年8月17日16:41:47");

        //2.打印倒计时10秒
        for (int i = 1; i <= 10; i++) {
            System.out.println ("倒计时：" + i);
            Thread.sleep ( 1000 );
        }
    }
}
