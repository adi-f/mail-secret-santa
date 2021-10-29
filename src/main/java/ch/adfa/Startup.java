package ch.adfa;


import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ch.adfa.dto.Mail;
import ch.adfa.dto.SantaTemplateMail;

@SpringBootApplication
public class Startup implements CommandLineRunner {

    @Autowired
    private MailService mailService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private RuleService ruleService;

    @Value("${ch.adfa.testtarget}")
    private String to;

    public static void main(String[] args) {
        SpringApplication.run(Startup.class, args);
    }

    @Override
    public void run(String... args) {

        System.out.println("Sending Email...");

        try {
            System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(ruleService.readRules("rules.json")));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            SantaTemplateMail mailTemplate = SantaTemplateMail.builder()
            .to(to)
            .subject("NextTest")
            .santaFrom("Adrian")
            .santaTo("Anton")
            .currentYear(LocalDate.now().getYear())
            .build();

            Mail mail = templateService.transform(mailTemplate);

            mailService.send(mail);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Done");

    }
}
