package ch.adfa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class RuleServiceTest {

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
            () -> rulesService.readRules("src/test/resources/" + file)
        );
        assertEquals(expectedMessage, exception.getMessage());
    }
}
