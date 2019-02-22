package due.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public Queue Queue1() {
        return new Queue("due_qu");
    }

    @Bean
    public Queue Queue2() {
        return new Queue("due_qu2");
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("due_ex");
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("due_ex_topic");
    }
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("due_ex_fanout");
    }

    /**
     * 自动创建 exchange和queue，并且绑定
     * @param queue Queue
     * @param directExchange Exchange
     * @return Bingding
     */
    @Bean
    Binding bindingDQ1(@Qualifier("Queue1") Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with("due");
    }

    @Bean
    Binding bindingTQ1(@Qualifier("Queue1") Queue queue, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with("due.*");
    }

    @Bean
    Binding bindingTQ2(@Qualifier("Queue2") Queue queue, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with("due.#");
    }

    @Bean
    Binding bindingFQ1(@Qualifier("Queue1") Queue queue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    @Bean
    Binding bindingFQ2(@Qualifier("Queue2") Queue queue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }
}
