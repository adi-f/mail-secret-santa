package ch.adfa;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ch.adfa.dto.Mail;

@SpringBootApplication
public class Startup implements CommandLineRunner {

    @Autowired
    private MailService mailService;

    @Value("${ch.adfa.testtarget}")
    private String to;

    public static void main(String[] args) {
        SpringApplication.run(Startup.class, args);
    }

    @Override
    public void run(String... args) {

        System.out.println("Sending Email...");

        try {
            Mail mail = Mail.builder()
            .to(to)
            .subject("NextTest")
            .message("<h1>HTML Test</h1> Das ist eine <b>HTML</b> Nachricht!")
            .build();

            mailService.send(mail);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Done");

    }
}
