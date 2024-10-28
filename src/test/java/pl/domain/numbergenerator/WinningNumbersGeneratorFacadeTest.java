package pl.domain.numbergenerator;

import org.junit.jupiter.api.Test;
import pl.domain.numbergenerator.dto.WinningNumbersDto;
import pl.domain.numberreceiver.NumberReceiverFacade;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WinningNumbersGeneratorFacadeTest {

    private final WinningNumbersRepository winningNumbersRepository = new WinningNumbersRepositoryTestImpl();
    NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);
    private final OneRandomNumberFetcher fetcher = new SecureRandomOneNumberFetcher();

    //given
    @Test
    public void should_return_set_of_required_size() {
        RandomNumberGenerable generator = new RandomGenerator(fetcher);
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade numberGenerator = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        WinningNumbersDto generatedNumbers = numberGenerator.generateWinningNumbers();
        //then
        assertThat(generatedNumbers.getWinningNumbers().size()).isEqualTo(6);
    }

    @Test
    public void should_return_set_of_required_size_within_required_range() {
        //given
        RandomNumberGenerable generator = new RandomGenerator(fetcher);
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade numbersGeneratorFacade = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        WinningNumbersDto generatedNumbers = numbersGeneratorFacade.generateWinningNumbers();
        //then
        int upperBound = 99;
        int lowerBound = 1;
        Set<Integer> winningNumbers = generatedNumbers.getWinningNumbers();
        boolean numberInRange = winningNumbers.stream().allMatch(number -> number >= lowerBound && number <= upperBound);
    }

    @Test
    public void should_throw_an_exception_when_number_not_in_range() {
        //given
        Set<Integer> numbersOutOfRange = Set.of(1, 2, 3, 4, 5, 100);
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl(numbersOutOfRange);
        WinningNumbersGeneratorFacade numbersGenerator = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        //then
        assertThrows(IllegalStateException.class, numbersGenerator::generateWinningNumbers, "Number out of Range!");
    }

    @Test
    public void should_return_collection_of_unique_values() {
        //given
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade numbersGenerator = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        WinningNumbersDto generatedNumbers = numbersGenerator.generateWinningNumbers();
        //then
        int generatedNumberSize = new HashSet<>(generatedNumbers.getWinningNumbers()).size();
        assertThat(generatedNumberSize).isEqualTo(6);
    }


    @Test
    public void should_return_winning_numbers_by_given_date() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 8, 10, 12, 0, 0);
        Set<Integer> generatedWinningNumbers = Set.of(1, 2, 3, 4, 5, 6);
        String id = UUID.randomUUID().toString();
        WinningNumbers winningNumbers = WinningNumbers.builder()
                .id(id)
                .date(drawDate)
                .winningNumbers(generatedWinningNumbers)
                .build();
        winningNumbersRepository.save(winningNumbers);
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumbersGeneratorFacade numbersGenerator = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        WinningNumbersDto winningNumbersDto = numbersGenerator.retrieveWinningNumberByDate(drawDate);
        //then
        WinningNumbersDto expectedWinningNumbersDto = WinningNumbersDto.builder()
                .date(drawDate)
                .winningNumbers(generatedWinningNumbers)
                .build();
        assertThat(expectedWinningNumbersDto).isEqualTo(winningNumbersDto);
    }

    @Test
    public void should_trow_an_exception_when_fail_to_retrieve_numbers_by_given_date() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 8, 10, 12, 0, 0);
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        //then
        assertThrows(WinningNumbersNotFoundException.class, () -> winningNumbersGeneratorFacade.retrieveWinningNumberByDate(drawDate), "Not Found");
    }

    @Test
    public void should_return_true_if_numbers_are_generated_by_given_date() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 8, 10, 12, 0, 0);
        Set<Integer> generatedWinningNumbers = Set.of(1, 2, 3, 4, 5, 6);
        String id = UUID.randomUUID().toString();
        WinningNumbers winningNumbers = WinningNumbers.builder()
                .id(id)
                .date(drawDate)
                .winningNumbers(generatedWinningNumbers)
                .build();
        winningNumbersRepository.save(winningNumbers);
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumbersGeneratorFacade numbersGenerator = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        boolean areWinningNumbersGeneratedByDate = numbersGenerator.areWinningNumbersGeneratedByDate();
        //then
        assertTrue(areWinningNumbersGeneratedByDate);
    }
}