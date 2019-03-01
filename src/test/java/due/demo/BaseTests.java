package due.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

@RunWith(JUnit4.class)
public class BaseTests {

    @Test
    public void test() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        Class<?> x = Class.forName("[I");//基础类
//        Class<?> x = Class.forName("[I",true, classLoader);
        System.out.println(x);
//        x = ClassLoader.getSystemClassLoader().loadClass("[I");//java.lang.ClassNotFoundException: [I
        x = ClassLoader.getSystemClassLoader().loadClass("due.demo.BaseTests");//必须写包名
        Object o = x.getConstructor().newInstance();
        x.getMethod("test2").invoke(o);
        System.out.println(x);
        x = classLoader.loadClass("due.demo.MailTests");
        System.out.println(x);
    }

    @Test
    public void test2() throws Exception {
        System.out.println(new File(".\\src\\").toURI().toURL());//file:/E:/java/spring-demo/./src/
        Class<?> x = new URLClassLoader(new URL[]{new File(".\\src\\test").toURI().toURL()}).loadClass("Test");
        //反射机制的应用必须要求该类是public访问权限的
        x.getMethod("test").invoke(x.newInstance());//类必须为public 否则：can not access a member of class Test with modifiers "public"
        //Method method = x.getMethod("test2");//java.lang.NoSuchMethodException
        Method method = x.getDeclaredMethod("test2");
        method.setAccessible(true);
        method.invoke(x.newInstance());
        System.out.println(x);
    }
}
