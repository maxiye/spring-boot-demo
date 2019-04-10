package due.demo.utils;

import org.springframework.context.ApplicationContext;

/**
 * 手动获取bean工具类
 * @author due
 */
@SuppressWarnings("unused")
public class SpringContextUtil {
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * {@code 第30条：优先使用泛型方法}
     * @param requiredType Class
     * @param <T> T
     * @return T
     */
    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }
}
