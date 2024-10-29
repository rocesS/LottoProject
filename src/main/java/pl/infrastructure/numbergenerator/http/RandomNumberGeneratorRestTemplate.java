package pl.infrastructure.numbergenerator.http;

import lombok.AllArgsConstructor;
import org.springframework.web.client.RestTemplate;
import pl.domain.numbergenerator.RandomNumberGenerable;
import pl.domain.numbergenerator.SixRandomNumbersDto;

@AllArgsConstructor
class RandomNumberGeneratorRestTemplate implements RandomNumberGenerable {

    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;

    @Override
    public SixRandomNumbersDto generateSixRandomNumbers() {
        return null;
    }
}
