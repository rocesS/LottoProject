package pl.domain.numbergenerator.dto;

import lombok.Builder;

@Builder
public record OneRandomNumberResponseDto(int number) {
}