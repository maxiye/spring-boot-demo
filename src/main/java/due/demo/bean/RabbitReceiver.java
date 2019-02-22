package due.demo.bean;

import due.demo.model.User;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitReceiver {

    @RabbitListener(queues = "due_qu")
    public void processQ1(String hello) {
        System.out.println("Receiver from due_qu  : " + hello);
    }
    @RabbitListener(queues = "due_qu2")
    public void processQ2(String content) {
        System.out.println("Receiver from due_qu2  : " + content);
    }

    /*@RabbitListener(queues = "due_qu2")
    public void processUser(Object content) {
        System.out.println("Receiver from due_qu2  : " + content.toString());
    }*/
}
