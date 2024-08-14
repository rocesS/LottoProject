package pl.domain.numbergenerator;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder

public record WinningNumbers(String id,
                             Set<Integer> winningNumbers,
                             LocalDateTime date) {
}
