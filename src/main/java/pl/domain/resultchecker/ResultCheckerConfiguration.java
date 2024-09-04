package pl.domain.resultchecker;

import pl.domain.numbergenerator.WinningNumbers;
import pl.domain.numbergenerator.WinningNumbersGeneratorFacade;
import pl.domain.numberreceiver.NumberReceiverFacade;

public class ResultCheckerConfiguration {

    ResultCheckerFacade createForTest(WinningNumbersGeneratorFacade generatorFacade, NumberReceiverFacade receiverFacade,
                                      PlayerRepository playerRepository) {
        WinnersRetriever winnersRetriever = new WinnersRetriever();
        return new ResultCheckerFacade(generatorFacade, receiverFacade, playerRepository, winnersRetriever);
    }
}
