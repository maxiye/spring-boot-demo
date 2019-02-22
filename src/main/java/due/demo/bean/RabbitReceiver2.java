package due.demo.bean;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

//@Component
//@RabbitListener(queues = "due_qu2")
public class RabbitReceiver2 {
    /*@RabbitHandler
    public void process(String hello) {
        System.out.println("Receiver from due_qu2  : " + hello);
    }*/
}
