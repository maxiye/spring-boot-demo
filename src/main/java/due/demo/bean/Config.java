package due.demo.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {
    @Value("${server.port}")
    public int port;

    @Value("${due.random}")
    public int random;

    @Value("${due.env}")
    public String env;
}
