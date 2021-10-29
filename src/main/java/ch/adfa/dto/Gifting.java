package ch.adfa.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Gifting {
    @NonNull
    Santa from;
    @NonNull
    Santa to;
}
