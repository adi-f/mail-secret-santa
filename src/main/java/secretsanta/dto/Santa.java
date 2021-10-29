package secretsanta.dto;

import java.util.Set;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Santa {
    @NonNull
    String name;

    @NonNull
    String email;

    @NonNull
    Set<String> exclusionsByName;   
}
