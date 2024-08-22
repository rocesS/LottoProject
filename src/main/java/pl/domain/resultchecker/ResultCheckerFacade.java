package pl.domain.resultchecker;

import lombok.AllArgsConstructor;
import pl.domain.numbergenerator.WinningNumbersGeneratorFacade;
import pl.domain.numbergenerator.dto.WinningNumbersDto;
import pl.domain.numberreceiver.NumberReceiverFacade;
import pl.domain.numberreceiver.dto.TicketDto;
import pl.domain.resultchecker.dto.PlayerDto;
import pl.domain.resultchecker.dto.ResultDto;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class ResultCheckerFacade {

    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;
    NumberReceiverFacade numberReceiverFacade;
    PlayerRepository playerRepository;
    WinnersRetriever winnerGenerator;

    public PlayerDto generateWinners() {
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate();
        List<Ticket> tickets = ResultCheckerMapper.mapFromTicketDto(allTicketsByDate);
        WinningNumbersDto winningNumbersDto = winningNumbersGeneratorFacade.generateWinningNumbers();
        Set<Integer> winningNumbers = winningNumbersDto.getWinningNumbers();
        if (winningNumbers == null || winningNumbers.isEmpty()) {
            return PlayerDto.builder()
                    .message("Winners failed to retrieve")
                    .build();
        }

        List<Player> players = winnerGenerator.retreivePlayers(tickets, winningNumbers);
        playerRepository.saveAll(players);
        return PlayerDto.builder()
                .results(ResultCheckerMapper.mapPlayersResults(players))
                .message("Winner succeeded to retrieve")
                .build();
        }

    public ResultDto findByHash(String hash) {
        Player player = playerRepository.findById(hash).orElseThrow(() -> new RuntimeException("Not found"));
        return ResultDto.builder()
                .hash(hash)
                .numbers(player.numbers())
                .hitNumbers(player.hitNumbers())
                .drawDate(player.drawDate())
                .isWinner(player.isWinner())
                .build();
    }
}
