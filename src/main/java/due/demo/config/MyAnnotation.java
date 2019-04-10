package due.demo.config;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.LOCAL_VARIABLE, ElementType.METHOD})
public @interface MyAnnotation {
    /**
     * 注解名称
     * @return String name
     */
    String name() default "due";

    /**
     * 年龄
     * @return int age
     */
    int age() default 24;
}
