package ch.adfa.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Mail {
    @NonNull
    String to;
    @NonNull
    String subject;
    @NonNull
    String message;
}
