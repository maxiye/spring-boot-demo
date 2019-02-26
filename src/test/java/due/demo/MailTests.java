package due.demo;

import due.demo.services.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailTests {

    @Autowired
    private MailService sender;

    @Value("${due.email.to}")
    private String to;

    @Test
    public void test1() {
        sender.send(sender.getMsg(to, "spring-test-mail", "test"));
    }

    //html test
    @Test
    public void test2() {
        MimeMessage msg = sender.getMimeMsgHelper(to, "spring-test-htmlmail", "<html><body><p style='color: red'>testhtml</p></body></html>").getMimeMessage();
        sender.send(msg);
    }

    //attachment test
    @Test
    public void test3() throws Exception {
        MimeMessageHelper helper = sender.getMimeMsgHelper(to, "spring-test-attchmentmail", "<p style='color: red'>attachment</p>");
        FileSystemResource file = new FileSystemResource(new File("E:\\java\\dmc5.jpg"));
        String fName = "dmc5.jpg";
        helper.addAttachment(fName, file);//可重复设置多条
        sender.send(helper.getMimeMessage());
    }

    //静态资源 test
    @Test
    public void test4() throws Exception {
        String rscId = "dmc5";
        MimeMessageHelper helper = sender.getMimeMsgHelper(to, "spring-test-attchmentmail", "<p style='color: red'>attachment</p><img src='cid:" + rscId + "' >");
        FileSystemResource file = new FileSystemResource(new File("E:\\java\\dmc5.jpg"));
        helper.addInline(rscId, file);
        sender.send(helper.getMimeMessage());
    }

    @Autowired
    private TemplateEngine engine;

    //thymeleaf 模板邮件
    @Test
    public void test5() {
        Context context = new Context();
        context.setVariable("hello", "world");
        String content = engine.process("/email/testEmailTpl", context);
        MimeMessageHelper helper = sender.getMimeMsgHelper(to, "spring-tpl-email", content);
        sender.send(helper.getMimeMessage());
    }
}
