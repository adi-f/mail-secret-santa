package ch.adfa.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.Builder.Default;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class Rules {
    @NonNull
    List<Santa> santas;

    @NonNull
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    @Default
    List<List<String>> exclusions = List.of();

    @Value
    @Jacksonized
    @Builder
    public static class Santa {
        @NonNull
        String name;
        @NonNull
        String email;
    }
}
