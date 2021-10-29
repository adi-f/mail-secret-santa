package ch.adfa;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ch.adfa.dto.Gifting;
import ch.adfa.dto.SantaTemplateMail;

@Service
public class MailPreparer {
    
    @Value("${ch.adfa.mailSubject}")
    private String mailSubject;

    public SantaTemplateMail prepareMail(Gifting gifting) {
        return SantaTemplateMail.builder()
        .to(gifting.getFrom().getEmail())
        .subject(mailSubject)
        .santaFrom(gifting.getFrom().getName())
        .santaTo(gifting.getTo().getName())
        .currentYear(LocalDate.now().getYear())
        .build();
    }
}
