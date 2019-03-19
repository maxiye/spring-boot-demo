package due.demo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;


/**
 * @author due
 */
@Service
public class MailService  {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JavaMailSender sender;

    @Value("${due.email.from}")
    private String from;

    public SimpleMailMessage getMsg(String to, String subject, String content) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(content);
        return msg;
    }

    public MimeMessageHelper getMimeMsgHelper(String to, String subject, String content) {
        MimeMessage msg = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            return helper;
        } catch (Exception e) {
            logger.error("Html email err:", e);
        }
        return null;
    }

    public void send(MimeMessage msg) {
        try {
            sender.send(msg);
            logger.info("MimeEmail sent: " + msg.toString());
        } catch (Exception e) {
            logger.error("Email sent err:", e);
        }
    }

    public void send(SimpleMailMessage msg) {
        try {
            sender.send(msg);
            logger.info("SimpleEmail sent: " + msg.toString());
        } catch (Exception e) {
            logger.error("Email sent err:", e);
        }
    }

}
