package org.example.demoproductservice.interfaces.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.example.demoproductservice.common.controller.ApiResponse;
import org.example.demoproductservice.common.exception.BrandNotFoundException;
import org.example.demoproductservice.domain.repository.entity.Brand;
import org.example.demoproductservice.domain.service.BrandService;
import org.example.demoproductservice.interfaces.consts.ErrorCode;
import org.example.demoproductservice.interfaces.dto.BrandResponse;
import org.example.demoproductservice.interfaces.dto.RegisterBrandRequest;
import org.example.demoproductservice.interfaces.dto.UpdateBrandRequest;
import org.example.demoproductservice.interfaces.exception.CommonHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/brands")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping
    public ApiResponse<BrandResponse> registerBrand(@Valid @RequestBody RegisterBrandRequest request) {
        return ApiResponse.of(BrandResponse.from(brandService.register(request.brandName())));
    }

    @GetMapping("/{brandId}")
    public ApiResponse<BrandResponse> getBrand(
            @PathVariable("brandId") @NotNull(message = "brandId는 필수 값입니다.")
            @Min(value = 1, message = "brandId는 0보다 큰 정수입니다.")
            @Max(value = 1_000_000_000, message = "brandId는 1 이상 1,000,000,000 이하의 정수입니다.") Long brandId
    ) {
        Brand brand;
        try {
            brand = brandService.findById(brandId);
        } catch (BrandNotFoundException e) {
            throw new CommonHttpException(ErrorCode.BRAND_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return ApiResponse.of(BrandResponse.from(brand));
    }

    @PatchMapping("/{brandId}")
    public ApiResponse<BrandResponse> updateBrand(
            @PathVariable("brandId") @NotNull(message = "brandId는 필수 값입니다.")
            @Min(value = 1, message = "brandId는 0보다 큰 정수입니다.")
            @Max(value = 1_000_000_000, message = "brandId는 1 이상 1,000,000,000 이하의 정수입니다.") Long brandId,
            @Valid @RequestBody UpdateBrandRequest request
    ) {
        Brand brand;
        try {
            brand = brandService.update(brandId, request.brandName());
        } catch (BrandNotFoundException e) {
            throw new CommonHttpException(ErrorCode.BRAND_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return ApiResponse.of(BrandResponse.from(brand));
    }

    @DeleteMapping("/{brandId}")
    public ApiResponse<Void> deleteBrand(
            @PathVariable("brandId") @NotNull(message = "brandId는 필수 값입니다.")
            @Min(value = 1, message = "brandId는 0보다 큰 정수입니다.")
            @Max(value = 1_000_000_000, message = "brandId는 1 이상 1,000,000,000 이하의 정수입니다.") Long brandId
    ) {
        try {
            brandService.delete(brandId);
        } catch (BrandNotFoundException e) {
            throw new CommonHttpException(ErrorCode.BRAND_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return ApiResponse.of(null);
    }
}
