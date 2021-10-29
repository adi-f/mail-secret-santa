package secretsanta;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import secretsanta.dto.Mail;
import secretsanta.dto.SantaTemplateMail;

@Service
public class TemplateService {

    @Value("${secretsanta.mailtemplate}")
    private String mailtemplateFilePath;

    private String template;

    public Mail transform(SantaTemplateMail mail) {
        String message = getTemplate()
        .replace("${santaFrom}", mail.getSantaFrom())
        .replace("${santaTo}", mail.getSantaTo())
        .replace("${currentYear}", String.valueOf(mail.getCurrentYear()))
        .replace("${banner}", mail.getBanner());

        return Mail.builder()
        .to(mail.getTo())
        .subject(mail.getSubject())
        .message(message)
        .build();
    }

    private String getTemplate() {
        if(template == null) {
            try (InputStream is = new BufferedInputStream(new FileInputStream(mailtemplateFilePath))) {
                template = IOUtils.toString(is, StandardCharsets.UTF_8);
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }
        return template;
    }
}