package pl.domain.numbergenerator;

import pl.domain.numberreceiver.NumberReceiverFacade;

public class NumberGeneratorConfiguration {

    WinningNumbersGeneratorFacade createForTest (RandomNumberGenerable generator, WinningNumbersRepository winningNumbersRepository, NumberReceiverFacade numberReceiverFacade) {
        WinningNumbersValidator winningNumbersValidator = new WinningNumbersValidator();
        return new WinningNumbersGeneratorFacade(generator, winningNumbersValidator, winningNumbersRepository, numberReceiverFacade);
    }
}
