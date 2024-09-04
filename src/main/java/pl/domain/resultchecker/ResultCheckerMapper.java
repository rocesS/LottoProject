package pl.domain.resultchecker;

import pl.domain.numberreceiver.dto.TicketDto;
import pl.domain.resultchecker.dto.ResultDto;

import java.util.List;
import java.util.stream.Collectors;

class ResultCheckerMapper {

    static List<ResultDto> mapPlayersResults(List<Player> players) {
        return players.stream()
                .map(player -> ResultDto.builder()
                        .hash(player.hash())
                        .numbers(player.numbers())
                        .hitNumbers(player.hitNumbers())
                        .drawDate(player.drawDate())
                        .isWinner(player.isWinner())
                        .build())
                .collect(Collectors.toList());
    }

    static List<Ticket> mapFromTicketDto(List<TicketDto> allTicketsByDate) {
        return allTicketsByDate.stream()
                .map(ticketDto -> Ticket.builder()
                        .drawDate(ticketDto.drawDate())
                        .hash(ticketDto.hash())
                        .numbers(ticketDto.numbers())
                        .build())
                .toList();
    }


}
