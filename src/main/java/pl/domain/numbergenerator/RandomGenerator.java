package pl.domain.numbergenerator;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

class RandomGenerator implements RandomNumberGenerable {

    private static final int LOWER_BAND = 1;
    private static final int UPPER_BAND = 99;
    private static final int RANDOM_NUMBER_BOUND = (UPPER_BAND - LOWER_BAND) + 1;

    private final SecureRandom secureRandom = new SecureRandom();

    public Set<Integer> generateSixRandomNumbers() {
        Set<Integer> winningNumbers = new HashSet<>();
        while (winningNumbers.size() < 6) {
            int preparedRandomNumber = secureRandom.nextInt(RANDOM_NUMBER_BOUND) + LOWER_BAND;
            winningNumbers.add(preparedRandomNumber);
        }
        return winningNumbers;
    }
}


