import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class A {
    public static void main(String[] args) {

        ApplicationContext ac = new ClassPathXmlApplicationContext ( "classpath:spring-rabbitmq-producer.xml" );

        RabbitTemplate rabbitTemplate = ac.getBean ( RabbitTemplate.class );

        testConfirm( rabbitTemplate );

//        testReturn ( rabbitTemplate );

        /*ClassPathXmlApplicationContext cac = (ClassPathXmlApplicationContext) ac;
        cac.close ();*/
    }

    public static void testConfirm(RabbitTemplate rabbitTemplate){
        rabbitTemplate.setConfirmCallback ( new RabbitTemplate.ConfirmCallback ( ) {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println ( "confirm方法被执行了" );

                if (ack){
                    System.out.println ("接收消息成功：" + cause );
                }else {
                    System.out.println ("接收消息失败：" + cause );
                }
            }
        } );

        rabbitTemplate.convertAndSend ( "test_exchange_confirm","confirm","message confirm" );
    }

    public static void testReturn(RabbitTemplate rabbitTemplate){
        rabbitTemplate.setMandatory ( true );

        rabbitTemplate.setReturnCallback ( new RabbitTemplate.ReturnCallback ( ) {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println ("return执行了" );

                System.out.println(message);
                System.out.println(replyCode);
                System.out.println(replyText);
                System.out.println(exchange);
                System.out.println(routingKey);
            }
        } );

        for (int i = 0; i < 10; i++) {

        }
        rabbitTemplate.convertAndSend ( "test_exchange_confirm","confirm11","message confirm" );
    }
}
