package ch.adfa.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Mail {
    String to;
    String subject;
    String message;
}
