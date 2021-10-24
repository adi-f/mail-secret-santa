package ch.adfa;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import ch.adfa.dto.Mail;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    void send(Mail mail) {

        try {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom(from);
        message.setSubject(mail.getSubject());

        MimeMessageHelper helper = new MimeMessageHelper(message, false);
        helper.setTo(mail.getTo());
        helper.setText(mail.getMessage(), true);


        javaMailSender.send(message);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }    
}
