package pl.domain.numberreceiver;

import org.junit.jupiter.api.Test;

import pl.domain.AdjustableClock;
import pl.domain.numberreceiver.dto.NumberReceiverResponseDto;
import pl.domain.numberreceiver.dto.TicketDto;

import java.time.*;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class NumberReceiverFacadeTest {

    private final TicketRepository ticketRepository = new TicketRepositoryTestImpl();
    Clock clock = Clock.systemUTC();

    @Test
    public void should_return_correct_response_when_user_input_six_number_in_range() {
        //given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfigurator().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numberFromUser = Set.of(1, 2, 3, 4, 5, 6);
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();

        TicketDto generatedTicket = TicketDto.builder()
                .hash(hashGenerator.getHash())
                .numbers(numberFromUser)
                .drawDate(nextDrawDate)
                .build();

        //when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numberFromUser);

        //then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(generatedTicket, ValidationResult.INPUT_SUCCESS.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_failed_message_when_user_input_numbers_and_one_is_out_of_range() {
        //given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfigurator().createForTest(null, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 100);

        //when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        //then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_IN_RANGE.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_failed_message_when_user_input_numbers_and_one_is_out_of_range_and_negative() {
        //given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfigurator().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numberFromUser = Set.of(1, 2, 3, 4, 5, -6);

        //when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numberFromUser);

        //then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_IN_RANGE.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_failed_message_when_user_input_less_than_six_numbers() {
        //given
        HashGenerator hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfigurator().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5);

        //when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        //then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_SIX_NUMBERS_GIVEN.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_failed_message_when_user_input_more_than_six_numbers() {
        //given
        HashGenerator hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfigurator().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7);

        //when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        //then
        NumberReceiverResponseDto exceptionResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_SIX_NUMBERS_GIVEN.info);
    }

    @Test
    public void should_return_correct_hash() {
        //given
        HashGenerator hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfigurator().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        //when
        String response = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().hash();

        //then
        assertThat(response).hasSize(36);
        assertThat(response).isNotNull();
    }

    @Test
    public void hould_return_correct_draw_date() {
        //given
        Clock clock = Clock.fixed(LocalDateTime.of(2024, 8, 3, 10, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        HashGenerator hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfigurator().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        //when
        LocalDateTime testDrawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();

        //then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2024, 8, 3, 12, 0, 0);
        assertThat(expectedDrawDate).isEqualTo(testDrawDate);
    }

    @Test
    public void should_return_next_Saturday_draw_date_when_date_is_Saturday_noon() {
        //given
        Clock clock = Clock.fixed(LocalDateTime.of(2024, 8, 3, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        HashGenerator hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfigurator().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        //when
        LocalDateTime testedDrawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();

        //then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2024, 8, 10, 12, 0, 0);
        assertThat(testedDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_next_Saturday_draw_date_when_date_is_Saturday_afternoon() {
        //given
        Clock clock = Clock.fixed(LocalDateTime.of(2024, 8, 3, 12, 1, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        HashGenerator hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfigurator().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        //when
        LocalDateTime testedDrawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();

        //then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2024, 8, 10, 12, 0, 0);
        assertThat(testedDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_ticket_with_correct_draw_date() {
        //given
        HashGenerable hashGenerator = new HashGenerator();

        Instant fixedInstant = LocalDateTime.of(2024, 8, 8, 12, 0, 0).toInstant(ZoneOffset.UTC);
        ZoneId of = ZoneId.of("Europe/London");
        AdjustableClock clock = new AdjustableClock(fixedInstant, of);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfigurator().createForTest(hashGenerator, clock, ticketRepository);
        NumberReceiverResponseDto numberReceiverResponseDto = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto1 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto2 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto3 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        TicketDto ticketDto = numberReceiverResponseDto.ticketDto();
        TicketDto ticketDto1 = numberReceiverResponseDto1.ticketDto();
        LocalDateTime drawDate = numberReceiverResponseDto.ticketDto().drawDate();

        // when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate);
        // then
        assertThat(allTicketsByDate).containsOnly(ticketDto, ticketDto1);

    }

    @Test
    public void should_return_empty_collection_when_tickets_arent_out_there() {
        //given
        HashGenerable hashGenerator = new HashGenerator();
        Clock clock = Clock.fixed(LocalDateTime.of(2024, 8, 8, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfigurator().createForTest(hashGenerator, clock, ticketRepository);
        NumberReceiverResponseDto numberReceiverResponseDto = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));

        LocalDateTime drawDate = numberReceiverResponseDto.ticketDto().drawDate();

        //when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate.plusWeeks(1L));

        //then
        assertThat(allTicketsByDate).isEmpty();
    }
}