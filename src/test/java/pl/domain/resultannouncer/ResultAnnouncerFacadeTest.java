package pl.domain.resultannouncer;

import org.junit.jupiter.api.Test;
import pl.domain.resultannouncer.dto.ResponseDto;
import pl.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.domain.resultchecker.ResultCheckerFacade;
import pl.domain.resultchecker.dto.ResultDto;

import javax.xml.transform.Result;
import java.time.*;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.domain.resultannouncer.MessageResponse.*;

class ResultAnnouncerFacadeTest {
    ResponseRepository responseRepository = new ResponseRepositoryTestImpl();
    ResultCheckerFacade resultCheckerFacade = mock(ResultCheckerFacade.class);

    @Test
    public void it_should_return_response_with_lose_message_if_ticket_is_not_winning_ticket() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022,12,17,12,0,0);
        String hash = "123";
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().createForTest(resultCheckerFacade, responseRepository, Clock.systemUTC());
        ResultDto resultDto = ResultDto.builder()
                .hash(hash)
                .numbers(Set.of(1,2,3,4,5,6))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();
        when(resultCheckerFacade.findByHash(hash)).thenReturn(resultDto);

        //when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(hash);

        //then
        ResponseDto responseDto = ResponseDto.builder()
                .hash("123")
                .numbers(Set.of(1,2,3,4,5,6))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();

        ResultAnnouncerResponseDto expectResult = new ResultAnnouncerResponseDto(responseDto, LOSE_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectResult);

    }

    @Test
    public void should_return_response_with_wait_message_if_date_is_before_announcement_time() {
        //give
        LocalDateTime drawDate = LocalDateTime.of(2022,12,31,12,0,0);
        String hash = "123";
        Clock clock = Clock.fixed(LocalDateTime.of(2022,12,17,12,0,0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().createForTest(resultCheckerFacade, responseRepository, clock);
        ResultDto resultDto = ResultDto.builder()
                .hash("123")
                .numbers(Set.of(1,2,3,4,5,6))
                .hitNumbers(Set.of(1,2,3,4,9,0))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        when(resultCheckerFacade.findByHash(hash)).thenReturn(resultDto);

        //when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(hash);

        //then
        ResponseDto responseDto = ResponseDto.builder()
                .hash("123")
                .numbers(Set.of(1,2,3,4,5,6))
                .hitNumbers(Set.of(1,2,3,4,9,0))
                .drawDate(drawDate)
                .isWinner(true)
                .build();

        ResultAnnouncerResponseDto expectResult = new ResultAnnouncerResponseDto(responseDto, WAIT_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectResult);

    }

    @Test
    public void should_return_response_with_hash_does_not_exist_message_if_hash_does_not_exist() {
        //given
        String hash = "123";
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().createForTest(resultCheckerFacade, responseRepository, Clock.systemUTC());

        when(resultCheckerFacade.findByHash(hash)).thenReturn(null);

        //when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(hash);

        //then
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(null, HASH_DOES_NOTE_EXIST_MESSAGE.info);
    }

    @Test
    public void should_return_response_with_hash_does_not_exist_message_if_response_is_not_saved_to_db_yet() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022,12,17,12,0,0);
        String hash = "123";
        ResultDto resultDto = ResultDto.builder()
                .hash(hash)
                .numbers(Set.of(1,2,3,4,5,6))
                .hitNumbers(Set.of(1,2,3,4,9,0))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        when(resultCheckerFacade.findByHash(hash)).thenReturn(resultDto);

        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().createForTest(resultCheckerFacade, responseRepository, Clock.systemUTC());
        ResultAnnouncerResponseDto resultAnnouncerResponseDto1 = resultAnnouncerFacade.checkResult(hash);
        String underTest = resultAnnouncerResponseDto1.responseDto().hash();

        //when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(underTest);

        //then
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(resultAnnouncerResponseDto.responseDto(), ALREADY_CHECKED.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResult);
    }














}