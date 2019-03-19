package due.demo.bean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import due.demo.services.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author due
 */
@Component
public class RabbitReceiver {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RabbitListener(queues = "due_qu")
    public void processQ1(String hello) {
        System.out.println("Receiver from due_qu  : " + hello);
    }

    @RabbitListener(queues = "due_qu2")
    public void processQ2(String content) {
        System.out.println("Receiver from due_qu2  : " + content);
    }

    @Autowired
    private MailService mailService;

    @RabbitListener(queues = "due_qu3")
    public void processQ3(String content) {
        logger.info("Get msg from due_que3: " + content);
        try {
            HashMap<String, String> msg = new ObjectMapper().readValue(content, new TypeReference<HashMap<String, String>>() {});
            mailService.send(mailService.getMsg(msg.get("to"), msg.get("subject"), msg.get("content")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //多接受一次，多一个消费者
    /*@RabbitListener(queues = "due_qu2")
    public void processUser(Object content) {
        System.out.println("Receiver from due_qu2  : " + content.toString());
    }*/
}
