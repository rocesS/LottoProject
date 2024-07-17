package pl.domain.numberreceiver;

import org.junit.jupiter.api.Test;
import pl.domain.AdjustableClock;
import pl.domain.numberreceiver.dto.InputNumberResultDto;
import pl.domain.numberreceiver.dto.TicketDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class NumberReceiverFacadeTest {

    AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2022,12,17,12,0,0).toInstant(ZoneOffset.ofHours(1)), ZoneId.systemDefault());

    NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(
            new NumberValidator(),
            new InMemoryNumberReceiverRepositoryTestImpl(),
            clock
    );


    @Test
    public void should_return_success_when_user_gave_six_numbers() {
        //given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        //when
        InputNumberResultDto result = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        //then
        assertThat(result.message()).isEqualTo("success");
    }

    @Test
    public void should_return_failed_when_user_gave_at_least_one_number_out_of_range_of_1_to_99() {
        //given
        Set<Integer> numbersFromUser = Set.of(1, 200, 3, 4, 5, 6);
        //when
        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        //then
        assertThat(result.message()).isEqualTo("failed");
    }

    @Test
    public void should_return_failed_when_user_gave_more_than_six_numbers() {
        //given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7);
        //when
        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        //then
        assertThat(result.message()).isEqualTo("failed");
    }

    @Test
    public void should_return_save_to_data_base_when_user_gave_six_numbers() {
        //given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        InputNumberResultDto result = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        LocalDateTime drawDate = LocalDateTime.of(2022,12,17,12,0,0);
        //when
        List<TicketDto> ticketDtos = numberReceiverFacade.userNumbers(drawDate);
        //then
        assertThat(ticketDtos).contains(
                TicketDto.builder()
                    .ticketId(result.ticketId())
                    .drawDate(result.drawDate())
                    .numbersFromUser(result.numbersFromUser())
                    .build());
    }




//    @Test
//    public void should_return_failed_when_user_gave_less_than_six_numbers() {
//        //given
//        Set<Integer> numbersFromUser = Set.of(1,2,3,4,5);
//        //when
//        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
//        //then
//        assertThat(result.message()).isEqualTo("Failed");
//    }
}