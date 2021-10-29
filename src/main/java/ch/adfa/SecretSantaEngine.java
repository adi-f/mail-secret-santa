package ch.adfa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ch.adfa.dto.Gifting;
import ch.adfa.dto.Santa;

public class SecretSantaEngine implements Iterable<Gifting> {
    private static final int MAX_RETIES = 300;
    private static final String FAILED_FINDING_SOLUTION_MESSAGE = "Could not find a valid gifting order!";

    private final List<Santa> giftingOrder;

    public static SecretSantaEngine fromRules(Collection<Santa> rules) {
        return new SecretSantaEngine(rules);
    }
    
    SecretSantaEngine(Collection<Santa> santas) {
        giftingOrder = createValidRandomOrder(santas);
    }

    private List<Santa> createValidRandomOrder(Collection<Santa> santas) {
        if(santas.size() < 2) {
            throw new RuntimeException(FAILED_FINDING_SOLUTION_MESSAGE);
        }
        int retries = 0;
        List<Santa> giftingOrder = new ArrayList<>(santas);

        do {
            if(retries++ >= MAX_RETIES) {
                throw new RuntimeException(FAILED_FINDING_SOLUTION_MESSAGE);
            }
            shuffle(giftingOrder);
        } while(!isValid(giftingOrder));
        return Collections.unmodifiableList(giftingOrder);
    }

    <T> void shuffle(List<T> list) {
        Collections.shuffle(list);
    }

    private boolean isValid(List<Santa> giftingOrder) {
        for (int i = 0, j = 1; i < giftingOrder.size(); i++, j = (j+1) % giftingOrder.size()) {
            Santa from = giftingOrder.get(i);
            Santa to = giftingOrder.get(j);
            if(from.getExclusionsByName().contains(to.getName())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<Gifting> iterator() {
        return new Iterator<Gifting>() {
            int position = 0;

            @Override
            public boolean hasNext() {
                return position < giftingOrder.size();
            }

            @Override
            public Gifting next() {
                int i = position++;
                int j = (i+1) % giftingOrder.size();
                return Gifting.builder()
                .from(giftingOrder.get(i))
                .to(giftingOrder.get(j))
                .build();
            }
        };
    }
}
