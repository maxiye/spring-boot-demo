package due.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author due
 */
@Component
public class Config {
    @Value("${server.port}")
    public int port;

    @Value("${due.random}")
    public int random;

    @Value("${due.env}")
    public String env;
}
