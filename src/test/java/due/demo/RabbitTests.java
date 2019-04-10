package due.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import due.demo.services.RabbitSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitTests {
    @Autowired
    private RabbitSender sender;

    /**
     * 发送消息会添加 Properties：content_type=text/plain
     */
    @Test
    public void test1() {
        System.out.println("hello");
        sender.send("hello");
        //exchange: due_ex, bingdings : {queue: due_qu, Routing key: due}
        sender.send("due_ex","due", "hello");
        sender.send("due_ex","due2", "hello2");
        //exchange: due_ex_topic, bingdings : [{queue: due_qu, Routing key: due.*}, {queue:due_qu2, Routing key: due.#}]
        sender.send("due_ex_topic","due.ha", "hello3");
        sender.send("due_ex_topic","due", "hello9");//due.#
        sender.send("due_ex_topic","due.", "hello10");//due.# & due.*
        sender.send("due_ex_topic","due..", "hello11");//due.#
        sender.send("due_ex_topic","due..due.", "hello12");//due.#
        sender.send("due_ex_topic",".due..", "hello13");// none
        sender.send("due_ex_topic","due.haa", "hello4");
        sender.send("due_ex_topic","due.due2.due", "hello5");
        sender.send("due_ex_topic","due.*", "hello6");
        sender.send("due_ex_topic","due.#", "hello7");
        sender.send("due_ex_fanout","due.dueddd.dd", "hello8");
//        sender.send("due_ex_fanout","due.dueddd.dd", new User("a", 2));
    }

    @Test
    public void test2() throws Exception {
        HashMap<String, String> content = new HashMap<>(3);
        content.put("to", "due@gshopper.com");
        content.put("subject", "test-rabbitmq-email");
        content.put("content", "rabbitmq-send-email");
        sender.send("due_ex_topic", "email.erpservice", new ObjectMapper().writeValueAsString(content));
    }
}
