package due.demo.services;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author due
 */
@Component
public class RabbitSender {
    private final AmqpTemplate template;

    @Autowired
    public RabbitSender(AmqpTemplate template) {
        this.template = template;
    }

    public void send(String content) {
        content = content + new Date();
        template.convertAndSend("due_ex", "due", content);
    }

    public void send(String exch, String route, Object content) {
        template.convertAndSend(exch, route, content);
    }
}
