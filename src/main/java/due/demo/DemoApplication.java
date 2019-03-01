package due.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class DemoApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    //部署tomcat必须
    //重写父类方法configure，使springboot容器以DemoApplication类作为入口
    //基于servlet3.0，已经可以不需要web.xml了。spring为我们提供了WebApplicationInitializer接口，由servlet3.0自动引导。
    //我们继承实现了WebApplicationInitializer接口的的SpringBootServletInitializer，使用其configure来加载SpringBoot的入口配置
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DemoApplication.class);
    }
}
