package due.demo.bean;

import due.demo.model.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RabbitSender {
    @Autowired
    private AmqpTemplate template;

    public void send(String content) {
        content = content + new Date();
        template.convertAndSend("due_ex", "due", content);
    }

    public void send(String exch, String route, String content) {
        template.convertAndSend(exch, route, content);
    }

    public void send(String exch, String route, Object content) {
        template.convertAndSend(exch, route, content);
    }
}
