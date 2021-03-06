package due.demo;

import due.demo.config.MyAnnotation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.*;

@RunWith(JUnit4.class)
public class BaseTests {

    public static void main(String[] args) {
        new BaseTests().test4();
    }

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
        // 反射机制的应用必须要求该类是public访问权限的
        x.getMethod("test").invoke(x.newInstance());//类必须为public 否则：can not access a member of class Test with modifiers "public"
        //Method method = x.getMethod("test2");//java.lang.NoSuchMethodException
        Method method = x.getDeclaredMethod("test2");
        method.setAccessible(true);
        method.invoke(x.newInstance());
        System.out.println(x);
    }

    @Test
    public void test3() {
        String str = "1234567";
        byte[] bytes = str.getBytes();
        long hash = 0;
        int lft = 40;
        for (byte aByte : bytes) {
            hash |= ((long)aByte << lft);
            if (lft >= 8) {
                lft -= 8;
            } else {
                break;
            }
        }
        Class<? extends Integer> cI = Integer.class;
        System.out.println(hash);
        System.out.println(Arrays.toString(cI.getMethods()));
    }
    //socket test
    @Test
    public void test4() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(9999);Socket socket = serverSocket.accept();InputStream is = socket.getInputStream()) {
                byte[] bytes = new byte[2048];
                int len;
                //输入流，输入到内存中的
                while ((len = is.read(bytes)) != -1) {
                    String get = new String(bytes, 0, len);
                    System.out.println("get msg: " + get);
                    if (get.equals("quit")) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        //客户端
        try (Socket socket = new Socket(InetAddress.getLocalHost(), 9999);PrintWriter printWriter = new PrintWriter(socket.getOutputStream())) {
            printWriter.println("hello world!");//发出去的
            printWriter.flush();
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String send = scanner.nextLine();
                printWriter.write(send);
                printWriter.flush();//不加缓存，不发
                if (send.equals("quit")) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //NIO Socket
    @Test
    public void test5() {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(4, 4, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
        pool.execute(() -> {
            try (Selector selector = Selector.open();
                 ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
                serverSocketChannel.bind(new InetSocketAddress(InetAddress.getLocalHost(), 9999));
                serverSocketChannel.configureBlocking(false);
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                while (true) {
                    selector.select();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    System.out.println("-------------------------------");
                    for (SelectionKey key : selectionKeys) {
                        // key.readyOps() == SelectionKey.OP_ACCEPT   readyOps 也会等于5（READ | WRITE）
                        if (key.isAcceptable()) {
                            try {
                                SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();// 放到try中会自动关闭
                                channel.configureBlocking(false);
                                // channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, ByteBuffer.allocate(2048));// 同时注册可读的key，每次有一个readyOps = 4的key选择出来
                                // attachment bytebuffer
                                channel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(2048));
                                System.out.println("accepted");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (key.isReadable()) {
                            try {
                                SocketChannel channel = (SocketChannel) key.channel();
                                ByteBuffer byteBuffer = (ByteBuffer)key.attachment();
                                byteBuffer.clear();
                                // 非阻塞模式下，read()方法在尚未读取到任何数据时可能就返回了。所以需要关注它的int返回值，它会告诉你读取了多少字节。
                                if (channel.read(byteBuffer) != -1) {// 写入buffer
                                    // 将缓冲器转换为读状态
                                    // byteBuffer.flip();
                                    // System.out.println("from client: " + Charset.defaultCharset().decode(byteBuffer).toString());
                                    System.out.println("from client: " + new String(byteBuffer.array(), 0, byteBuffer.limit()));
                                    writeBuffer.clear();
                                    writeBuffer.put("geted".getBytes());
                                    writeBuffer.flip();
                                    // 非阻塞模式下，write()在尚未写入任何内容时就可能返回了。所以需要在循环中调用write()。
                                    while (writeBuffer.hasRemaining()) {
                                        channel.write(writeBuffer);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        // key.cancel();// 使用cancle移除，不再被选择
                        selectionKeys.remove(key);// !!!不移除，下次还在selectKeys中
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        try (Selector selector = Selector.open();
             SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(InetAddress.getLocalHost(), 9999))) {
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ, ByteBuffer.allocate(2048));
            int i = 0;
            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey key : selectionKeys) {
                    if (key.isReadable()) {
                        SocketChannel socket = (SocketChannel) key.channel();// 不能在try的（）中使用，自动关闭
                        ByteBuffer buffer = (ByteBuffer)key.attachment();
                        socket.read(buffer);
                        System.out.println("from server: " + new String(buffer.array(), 0, buffer.limit()));
                        // 读完清空，下次重新填充
                        buffer.clear();
                    } else if (key.isConnectable()) {
                        System.out.println("connected");
                    }
                    selectionKeys.remove(key);
                }
                socketChannel.write(Charset.defaultCharset().encode("hello:" + i++));
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test7() {
        System.out.println(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now()));
        System.out.println(DateTimeFormatter.ofPattern("YYYYMMdd_HHmmss").format(LocalDateTime.now()));
        System.out.println(DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()));
    }

    /**
     * aio
     * @throws InterruptedException e
     */
    @Test
    public void test8() throws InterruptedException {
        new Thread(() -> {
            AsynchronousChannelGroup group;
            try {
                group = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(4));
                AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open(group)
                        .bind(new InetSocketAddress(InetAddress.getLocalHost(), 9999));
                ByteBuffer buffer = ByteBuffer.allocate(2048);
                server.accept(buffer, new CompletionHandler<AsynchronousSocketChannel, ByteBuffer>() {
                    @Override
                    public void completed(AsynchronousSocketChannel result, ByteBuffer attachment) {
                        try {
                            buffer.clear();
                            buffer.put("hello,world".getBytes());
                            // 写状态反转为读状态，不反转无法读取写入的数据
                            buffer.flip();
                            // 实际为读取buffer中的数据
                            Future<Integer> f = result.write(buffer);
                            f.get();
                            System.out.println("server sent at：" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
                            result.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        server.accept(buffer, this);
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {
                        exc.printStackTrace();
                    }
                });
                group.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        try {
            AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
            Future<Void> future = channel.connect(new InetSocketAddress(InetAddress.getLocalHost(), 9999));
            future.get();
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            channel.read(buffer, null, new CompletionHandler<Integer, Void>() {
                @Override
                public void completed(Integer result, Void attachment) {
                    System.out.println("client accept:" + new String(buffer.array()));
                }

                @Override
                public void failed(Throwable exc, Void attachment) {
                    exc.printStackTrace();
                    try {
                        channel.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread.sleep(10 * 1000);
    }

    @MyAnnotation(name = "test9", age = 9)
    @Test(timeout = 1, expected = Exception.class)
    public void test9() throws Exception {
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method m : methods) {
            boolean hasAnnotation = m.isAnnotationPresent(Test.class);
            if (hasAnnotation) {
                Test annotation = m.getAnnotation(Test.class);
                System.out.println(m.getName() + ": annotation: " + annotation.annotationType() + "timeout:" + annotation.timeout() +";expected:" + annotation.expected() + ";");
//                System.out.println(Arrays.toString(m.getAnnotations()));
            }
            if (m.isAnnotationPresent(MyAnnotation.class)) {
                MyAnnotation annotation = m.getAnnotation(MyAnnotation.class);
                System.out.println(annotation + "name:" + annotation.name() + ";age:" + annotation.age());
            }
        }
        throw new Exception("ha");
    }

    /**
     * 第6条：避免创建不必要的对象
     * String实例
     * String.matches方法，内部创建了一个Pattern实例，创建一个Pattern实例是昂贵的，因为它需要将正则表达式编译成一个有限状态机
     * 自动装箱（autoboxing）
     * 另一种会创建不必要对象的方式是自动装箱（autoboxing）， 这种方式允许程序员混用基本类型和装箱基本类型，然后按需自动装箱和解箱。
     * 自动装箱模糊了基本类型和装箱基本类型之间的区别，但并没有消除这种区别
     */
    @Test
    public void test10() {
        // Long：9s，long：845ms
        long sum = 0L;
        for (long i = 0; i <= Integer.MAX_VALUE; i++)
            sum += i;
        System.out.println(sum);
    }
}
