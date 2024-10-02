package org.example.demoproductservice.interfaces.dto;

import jakarta.validation.constraints.*;

public record RegisterProductRequest(
        @NotNull(message = "categoryId는 필수 값입니다.")
        @Min(value = 1, message = "categoryId는 1 이상 1,000,000,000 이하의 정수입니다.")
        @Max(value = 1_000_000_000, message = "categoryId는 1 이상 1,000,000,000 이하의 정수입니다.")
        Long categoryId,
        @NotNull(message = "brandId는 필수 값입니다.")
        @Min(value = 1, message = "brandId는 1 이상 1,000,000,000 이하의 정수입니다.")
        @Max(value = 1_000_000_000, message = "brandId는 1 이상 1,000,000,000 이하의 정수입니다.")
        Long brandId,
        @NotNull(message = "price(상품 가격)는 필수 값입니다.")
        @Min(value = 0, message = "price(상품 가격)는 0원 이상이어야 합니다.")
        @Max(value = 100_000_000, message = "price(상품 가격)는 100,000,000원 이하여야 합니다.")
        Long price) {
}
