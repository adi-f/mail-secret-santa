package secretsanta;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import secretsanta.dto.Gifting;
import secretsanta.dto.SantaTemplateMail;

@Service
public class MailPreparer {
    
    @Value("${secretsanta.mailSubject}")
    private String mailSubject;

    @Value("${secretsanta.mailBanner}")
    private String banner;

    public SantaTemplateMail prepareMail(Gifting gifting) {
        return SantaTemplateMail.builder()
        .to(gifting.getFrom().getEmail())
        .subject(mailSubject)
        .santaFrom(gifting.getFrom().getName())
        .santaTo(gifting.getTo().getName())
        .currentYear(LocalDate.now().getYear())
        .banner(banner)
        .build();
    }
}
