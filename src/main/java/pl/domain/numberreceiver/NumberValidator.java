package pl.domain.numberreceiver;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class NumberValidator {

    private static final int QUANTITY_NUMBERS_FROM_USER = 6;
    private static final int MIN_VALUE_NUMBER_FROM_USER = 1;
    private static final int MAX_VALUE_NUMBER_FROM_USER = 99;



    List<ValidationResult> errors = new LinkedList<>();

    List<ValidationResult> validate(Set<Integer> numbersFromUser) {
        if (!isNumberSizeEqualSix(numbersFromUser)) {
            errors.add(ValidationResult.NOT_SIX_NUMBERS_GIVEN);
        }
        if (!isNumberInRange(numbersFromUser)) {
            errors.add(ValidationResult.NOT_IN_RANGE);
        }
        return errors;
    }

    private boolean isNumberSizeEqualSix (Set<Integer> numbersFromUser) {
        return numbersFromUser.size() == QUANTITY_NUMBERS_FROM_USER;
    }

    private boolean isNumberInRange(Set<Integer> numbersFromUser) {
        return numbersFromUser.stream()
                .allMatch(number -> number >= MIN_VALUE_NUMBER_FROM_USER && number <= MAX_VALUE_NUMBER_FROM_USER);
    }

    String createResultMessage() {
        return this.errors
                .stream()
                .map(validationResult -> validationResult.info)
                .collect(Collectors.joining(","));
    }
}
