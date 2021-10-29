package secretsanta;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import org.junit.jupiter.params.provider.MethodSource;

import secretsanta.dto.Gifting;
import secretsanta.dto.Santa;

public class SecretSantaEngineTest {
    
    private static List<Arguments> invalidGiftingOrder() {
        return List.of(
            arguments(
                "empty", List.of(
                newSanta("myself").exclusionsByName(Set.of("myself"))
            )),
            arguments(
                "single entry", List.of(
                newSanta("myself").exclusionsByName(Set.of("myself"))
            )),
            arguments(
                "violation in the middle", List.of(
                newSanta("A").exclusionsByName(Set.of("A")),
                newSanta("B").exclusionsByName(Set.of("B", "C")),
                newSanta("C").exclusionsByName(Set.of("B", "C")),
                newSanta("D").exclusionsByName(Set.of("D"))
            )),
            arguments(
                "violation wrapping around", List.of(
                newSanta("A").exclusionsByName(Set.of("A")),
                newSanta("B").exclusionsByName(Set.of("B", "C")),
                newSanta("C").exclusionsByName(Set.of("B", "C")),
                newSanta("D").exclusionsByName(Set.of("D"))
            )),
            arguments(
                "violation in the middle + wrapping around", List.of(
                newSanta("A").exclusionsByName(Set.of("A", "B")),
                newSanta("B").exclusionsByName(Set.of("A", "B"))
            ))
        );
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("invalidGiftingOrder")
    void testIsValid_invalid(String description, Collection<Santa.SantaBuilder> inputGiftingOrder) {
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> new NoRandomSecretSantaEngine(inputGiftingOrder.stream().map(Santa.SantaBuilder::build).collect(Collectors.toUnmodifiableList()))
        );
        assertEquals("Could not find a valid gifting order!", exception.getMessage());
    }

    private static List<Arguments> validGiftingOrder() {
        return List.of(
            arguments(
                "two not excluding santas", List.of(
                newSanta("A").exclusionsByName(Set.of("A")),
                newSanta("B").exclusionsByName(Set.of("B"))
            )),
            
            arguments(
                "no restrictions", List.of(
                newSanta("A").exclusionsByName(Set.of("A")),
                newSanta("B").exclusionsByName(Set.of("B")),
                newSanta("C").exclusionsByName(Set.of("C")),
                newSanta("D").exclusionsByName(Set.of("D"))
            )),
            arguments(
                "no violations", List.of(
                newSanta("A").exclusionsByName(Set.of("A", "C")),
                newSanta("B").exclusionsByName(Set.of("B", "D")),
                newSanta("C").exclusionsByName(Set.of("C", "A", "E")),
                newSanta("D").exclusionsByName(Set.of("B", "D")),
                newSanta("E").exclusionsByName(Set.of("C", "E"))
            ))
        );
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("validGiftingOrder")
    void testIsValid_valid(String description, Collection<Santa.SantaBuilder> inputGiftingOrder) {
        new NoRandomSecretSantaEngine(inputGiftingOrder.stream().map(Santa.SantaBuilder::build).collect(Collectors.toUnmodifiableList()));
    }

    @Test
    void testIterator() {
        Santa a = newSanta("A").exclusionsByName(Set.of("A")).build();
        Santa b = newSanta("B").exclusionsByName(Set.of("B")).build();
        Santa c = newSanta("C").exclusionsByName(Set.of("C")).build();

        List<Santa> input = List.of(a, b, c);
        List<Gifting> expected = List.of(
            newGifting(a, b),
            newGifting(b, c),
            newGifting(c, a)
        );

        Iterator<Gifting> testee = new NoRandomSecretSantaEngine(input).iterator();
        Iterator<Gifting> verifier = expected.iterator();

        for(int i = 0; i < 3; i++) {
            assertTrue(testee.hasNext());
            assertTrue(verifier.hasNext());
            assertEquals(verifier.next(), testee.next());
        }
        assertFalse(testee.hasNext());
        assertFalse(verifier.hasNext());
    }

    @Test
    void testSecretSantaEngine_validSolutionExists() {
        Santa a = newSanta("A").exclusionsByName(Set.of("A", "B")).build();
        Santa b = newSanta("B").exclusionsByName(Set.of("A", "B")).build();
        Santa c = newSanta("C").exclusionsByName(Set.of("C", "D")).build();
        Santa d = newSanta("D").exclusionsByName(Set.of("C", "D")).build();

        new SecretSantaEngine(List.of(a, b, c, d));
    }

    @Test
    void testSecretSantaEngine_noValidSolutionExists() {
        Santa a = newSanta("A").exclusionsByName(Set.of("A", "B")).build();
        Santa b = newSanta("B").exclusionsByName(Set.of("A", "B", "C")).build();
        Santa c = newSanta("C").exclusionsByName(Set.of("B", "C")).build();

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> new SecretSantaEngine(List.of(a, b, c))
        );
        assertEquals("Could not find a valid gifting order!", exception.getMessage());
    }

    private static Santa.SantaBuilder newSanta(String name) {
        return Santa.builder().name(name).email(name + "@nowhere.void");
    }

    private static Gifting newGifting(Santa from, Santa to) {
        return Gifting.builder().from(from).to(to).build();
    }

    private static class NoRandomSecretSantaEngine extends SecretSantaEngine {

        public NoRandomSecretSantaEngine(Collection<Santa> santas) {
            super(santas);
        }

        @Override
        <T> void shuffle(List<T> list) {
            // don't shuffle
        }
    };
}
