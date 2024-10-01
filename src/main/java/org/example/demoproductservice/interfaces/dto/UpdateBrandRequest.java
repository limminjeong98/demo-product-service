package org.example.demoproductservice.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateBrandRequest(@NotBlank(message = "브랜드 이름은 null 혹은 빈 문자열일 수 없습니다.") @Size(min = 1, max = 100, message = "브랜드 이름은 100자 이하여야 합니다.") String brandName) {
}
