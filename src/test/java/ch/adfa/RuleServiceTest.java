package ch.adfa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import ch.adfa.dto.Santa;

public class RuleServiceTest {


    private static final String RELATIVE_PATH = "src/test/resources/";
    private RuleService rulesService = new RuleService();
    
    @ParameterizedTest
    @CsvSource({
        "rules-invalidDuplicateName.json, Santa 'A' has no unique name",
        "rules-invalidMissingEmail.json, Santa 'C'  has no email.",
        "rules-invalidMissingName.json, Invalid Santa wihout name.",
        "rules-invalidUnknownNameInExclusions.json, Unknown names in exclusion lists: E",

    })
    void testReadRules_invalid(String file, String expectedMessage) {

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> rulesService.readRules(RELATIVE_PATH + file)
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testReadRules_validNoExclusions() {
        List<Santa> expected = List.of(
            newSanta("A").exclusionsByName(Set.of("A")).build(),
            newSanta("B").exclusionsByName(Set.of("B")).build()
        );
        List<Santa> result = rulesService.readRules(RELATIVE_PATH + "rules-validNoExclusions.json");

        assertEquals(expected, result);
    }

    @Test
    void testReadRules_valid() {
        List<Santa> expected = List.of(
            newSanta("A").exclusionsByName(Set.of("A", "B", "C", "D")).build(),
            newSanta("B").exclusionsByName(Set.of("A", "B")).build(),
            newSanta("C").exclusionsByName(Set.of("A", "C", "D")).build(),
            newSanta("D").exclusionsByName(Set.of("A", "C", "D")).build()

        );
        List<Santa> result = rulesService.readRules(RELATIVE_PATH + "rules-valid.json");

        assertEquals(expected, result);
    }

    private Santa.SantaBuilder newSanta(String name) {
        return Santa.builder()
        .name(name)
        .email(name.toLowerCase() + "@invalid.void");
    }
}
