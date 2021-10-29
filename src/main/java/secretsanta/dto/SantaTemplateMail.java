package secretsanta.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.Builder.Default;

@Value
@Builder
public class SantaTemplateMail {
    @NonNull
    String to;
    @NonNull
    String subject;
    @NonNull
    String santaFrom;
    @NonNull
    String santaTo;
    int currentYear;
    @Default
    @NonNull
    String banner = "<h1 style=\"color: red\">THIS IS A TEST MAIL, IGNORE IT!</h1>";
}
