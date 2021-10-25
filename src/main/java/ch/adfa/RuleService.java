package ch.adfa;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import ch.adfa.dto.Rules;
import ch.adfa.dto.Santa;

@Service
public class RuleService {
    
    List<Santa> readRules() {
        Rules rules = readFile();
        validate(rules);
        return transform(rules);
    }

    private Rules readFile() {
        ObjectMapper mapper = new ObjectMapper();
        try{
            return mapper.readValue(new File("rules.json"), Rules.class);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void validate(Rules rules) {
        Set<String> names = new HashSet<>();
        for(Rules.Santa santa : rules.getSantas()) {
            if(santa.getName().isEmpty()) {
                throw new RuntimeException("Invalid Santa wihout name.");
            }
            if(santa.getEmail().isEmpty()) {
                throw new RuntimeException("Santa '" + santa.getName() + "'  has no email.");
            }
            if(!names.add(santa.getName())) {
                throw new RuntimeException("Santa '" + santa.getName() + "' has no unique name");
            }
        }

            String invalidExclusionNames =  rules.getExclusions().stream()
            .flatMap(Collection::stream)
            .filter(name -> !names.contains(name))
            .collect(Collectors.joining(", "));
            if(!invalidExclusionNames.isEmpty()) {
                throw new RuntimeException("Unknown names in exclusion lists: " + invalidExclusionNames);
            }   
    }

    private List<Santa> transform(Rules rules) {
        Map<String, Set<String>> exclusions = buildExclusionMap(rules.getExclusions());
        return rules.getSantas().stream().map(santa -> {
            Set<String> exclusionsByName = exclusions.computeIfAbsent(santa.getName(), name -> Set.of(name));
            exclusionsByName = Collections.unmodifiableSet(exclusionsByName);
            return Santa.builder()
                .name(santa.getName())
                .email(santa.getEmail())
                .exclusionsByName(exclusionsByName)
                .build();

        })
        .collect(Collectors.toUnmodifiableList());
    }

    private Map<String, Set<String>>  buildExclusionMap(List<List<String>> exclusions) {
        Map<String, Set<String>> exclusionMap = new HashMap<>();
        for(List<String> exclusionGroup : exclusions) {
            for(String name : exclusionGroup) {
                Set<String> mapExclusionGroup = exclusionMap.computeIfAbsent(name, n -> new HashSet<>());
                mapExclusionGroup.addAll(exclusionGroup);
            }
        }
        return exclusionMap;
    }
}
