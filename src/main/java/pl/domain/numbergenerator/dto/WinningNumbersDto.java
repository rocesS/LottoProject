package pl.domain.numbergenerator.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class WinningNumbersDto {

    private Set<Integer> winningNumbers;
    private LocalDateTime date;

}
