import cn.itcast.ProducerApplication;
import cn.itcast.rabbitmq.drict.RabbitMQDirect;
import cn.itcast.rabbitmq.fanout.RabbitMQFanout;
import cn.itcast.rabbitmq.topic.RabbitMQTopic;
import cn.itcast.rabbitmq.smiple.RabbitMQSimple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ProducerApplication.class)
@RunWith ( SpringRunner.class )
public class ProducerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void send(){
        rabbitTemplate.convertAndSend ( "", RabbitMQSimple.Queue_NAME,"简单模式！" );
    }

    @Test
    public void sendTopic(){
        rabbitTemplate.convertAndSend ( RabbitMQTopic.EXCHANGE_NAME,"itcast.good","Topic模式 itcast！" );
        rabbitTemplate.convertAndSend ( RabbitMQTopic.EXCHANGE_NAME,"good.good","Topic模式 good！" );
    }

    @Test
    public void sendFanout(){
        rabbitTemplate.convertAndSend ( RabbitMQFanout.EXCHANGE_NAME,"","Fanout模式！" );
    }

    @Test
    public void sendDirect(){
        rabbitTemplate.convertAndSend ( RabbitMQDirect.EXCHANGE_NAME,"error","Direct模式 error！" );
        rabbitTemplate.convertAndSend ( RabbitMQDirect.EXCHANGE_NAME,"info","Direct模式 info！" );
    }
}
