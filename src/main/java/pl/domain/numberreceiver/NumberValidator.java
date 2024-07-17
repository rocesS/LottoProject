package pl.domain.numberreceiver;

import java.util.Set;

class NumberValidator {

    private static final int MAX_NUMBER_FROM_USER = 6;
    public static final int MINIMAL_NUMBER_FROM_USER = 1;
    public static final int MAXIMAL_NUMBER_FROM_USER = 99;


    boolean areAllNumbersInRange(Set<Integer> numbersFromUser) {
        return numbersFromUser.stream()
                .filter(number -> number >= MINIMAL_NUMBER_FROM_USER)
                .filter(number -> number <= MAXIMAL_NUMBER_FROM_USER)
                .count() == MAX_NUMBER_FROM_USER;
    }
}
