import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-rabbitmq-producer.xml")
public class ProducerTest {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void testHelloWorld(){
        //简单模式发送消息
        rabbitTemplate.convertAndSend ( "spring_queue","Hello World Spring!" );
    }

    @Test
    public void testFanout(){
        //扇出模式发送消息
        rabbitTemplate.convertAndSend ( "spring_fanout_exchange","","Exchange FanoutType!" );
    }

    @Test
    public void testTopic(){
        //通配符模式发送消息
        rabbitTemplate.convertAndSend ( "spring_topic_exchange","itcast.good","Exchange TopicType Profix itcast!" );
        rabbitTemplate.convertAndSend ( "spring_topic_exchange","heima.good","Exchange TopicType Profix heima!" );
    }
}
