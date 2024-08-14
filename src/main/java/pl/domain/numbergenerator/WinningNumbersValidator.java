package pl.domain.numbergenerator;

import java.util.Set;

class WinningNumbersValidator {

    private final static int LOWER_BAND = 1;
    private final static int UPPER_BAND = 99;

    public Set<Integer> validate(Set<Integer> winningNumbers) {
        if (outOfRange(winningNumbers)) {
            throw new IllegalStateException("Number out of range!");
        }
        return winningNumbers;
    }

    private boolean outOfRange(Set<Integer> winningNumbers) {
        return winningNumbers.stream()
                .anyMatch(number -> number < LOWER_BAND || number > UPPER_BAND);
    }
}
