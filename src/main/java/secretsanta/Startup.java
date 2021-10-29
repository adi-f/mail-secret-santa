package secretsanta;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import secretsanta.dto.Gifting;
import secretsanta.dto.Mail;
import secretsanta.dto.Santa;
import secretsanta.dto.SantaTemplateMail;

@SpringBootApplication
public class Startup implements CommandLineRunner {

    @Autowired
    private MailService mailService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private MailPreparer mailPreparer;

    @Value("${secretsanta.rules}")
    private String rulesFilePath;

    @Value("${secretsanta.sendMail}")
    private boolean sendMail;

    @Value("${secretsanta.logSecret}")
    private boolean logSecret;

    @Value("${secretsanta.logSecretMail}")
    private boolean logSecretMail;

    public static void main(String[] args) {
        SpringApplication.run(Startup.class, args);
    }

    @Override
    public void run(String... args) {

        System.out.println("Running Secret Santa");

        runSecretSanta();

        System.out.println("Done");

    }

    private void runSecretSanta() {
        List<Santa> rules = ruleService.readRules(rulesFilePath);
        SecretSantaEngine engine = SecretSantaEngine.fromRules(rules);

        System.out.println("Start sending mails");

        for(Gifting gifting : engine) {
            try {
                SantaTemplateMail mailTemplate = mailPreparer.prepareMail(gifting);
                Mail mail = templateService.transform(mailTemplate);
                if(logSecret) {
                    System.out.printf(" - %s -> %s (mail to: %s)\n", mailTemplate.getSantaFrom(), mailTemplate.getSantaTo(), mailTemplate.getTo());
                }
                if(logSecretMail) {
                    logMail(mail);
                }
                if(sendMail) {
                    mailService.send(mail);
                    System.out.println(" - Sent a mail");
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed at Santa " + gifting.getFrom().getName(), e);
            }
        }
        System.out.println("Sent all mails" + (sendMail ? "" : " (just simulated)"));
    }

    private void logMail(Mail mail) {
        System.out.printf(
            "To: %s\nSubject: %s\n> > > > > > > >\n%s\n< < < < < < < <\n\n",
            mail.getTo(), mail.getSubject(), mail.getMessage()
        );
    }
}
