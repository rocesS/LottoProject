package pl.domain.numbergenerator;

import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
class RandomGenerator implements RandomNumberGenerable {

    private static final int LOWER_BAND = 1;
    private static final int UPPER_BAND = 99;

    private final OneRandomNumberFetcher client;

    public Set<Integer> generateSixRandomNumbers() {
        Set<Integer> winningNumbers = new HashSet<>();
        while (isAmountOfNumbersLowerThanSix(winningNumbers)) {
            OneRandomNumberResponseDto randomNumberResponseDto = client.retrieveOneRandomNumber(LOWER_BAND, UPPER_BAND);
            int randomNumber = randomNumberResponseDto.number();
            winningNumbers.add(randomNumber);
        }
        return winningNumbers;
    }

    private boolean isAmountOfNumbersLowerThanSix(Set<Integer> winningNumbers) {
        return winningNumbers.size() < 6;
    }
}


