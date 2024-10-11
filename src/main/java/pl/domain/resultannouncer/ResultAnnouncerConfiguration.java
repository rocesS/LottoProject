package pl.domain.resultannouncer;

import pl.domain.resultchecker.ResultCheckerFacade;
import java.time.Clock;

public class ResultAnnouncerConfiguration {

    ResultAnnouncerFacade createForTest(ResultCheckerFacade resultCheckerFacade, ResponseRepository responseRepository, Clock clock) {
        return new ResultAnnouncerFacade(resultCheckerFacade,responseRepository, clock);
    }
}
