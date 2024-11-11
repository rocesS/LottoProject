package pl.domain.numbergenerator;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WinningNumbersRepository extends MongoRepository<WinningNumbers, String> {

    Optional<WinningNumbers> findNumbersByDate(LocalDateTime date);

    boolean existsByDate(LocalDateTime nextDrawDate);

}
