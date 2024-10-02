package org.example.demoproductservice.interfaces.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.example.demoproductservice.application.ProductFacade;
import org.example.demoproductservice.common.controller.ApiResponse;
import org.example.demoproductservice.common.exception.BrandNotFoundException;
import org.example.demoproductservice.common.exception.CategoryNotFoundException;
import org.example.demoproductservice.common.exception.ProductNotFoundException;
import org.example.demoproductservice.domain.repository.entity.Product;
import org.example.demoproductservice.interfaces.consts.ErrorCode;
import org.example.demoproductservice.interfaces.dto.ProductResponse;
import org.example.demoproductservice.interfaces.dto.RegisterProductRequest;
import org.example.demoproductservice.interfaces.dto.UpdateProductRequest;
import org.example.demoproductservice.interfaces.exception.CommonHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductFacade productFacade;

    public ProductController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> getProduct(
            @PathVariable("productId") @NotNull(message = "productId는 필수 값입니다.")
            @Min(value = 1, message = "productId는 1 이상 1,000,000,000 이하의 정수입니다.")
            @Max(value = 1_000_000_000, message = "productId는 1 이상 1,000,000,000 이하의 정수입니다.") Long productId
    ) {
        Product product;
        try {
            product = productFacade.findProductById(productId);
        } catch (ProductNotFoundException e) {
            throw new CommonHttpException(ErrorCode.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return ApiResponse.of(ProductResponse.from(product));
    }

    @PostMapping
    public ApiResponse<ProductResponse> registerProduct(@Valid @RequestBody RegisterProductRequest request) {
        Product product;
        try {
            product = productFacade.registerProduct(request.categoryId(), request.brandId(), request.price());
        } catch (BrandNotFoundException e) {
            throw new CommonHttpException(ErrorCode.BRAND_NOT_FOUND, HttpStatus.NOT_FOUND);
        } catch (CategoryNotFoundException e) {
            throw new CommonHttpException(ErrorCode.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return ApiResponse.of(ProductResponse.from(product));
    }

    @PatchMapping("/{productId}")
    public ApiResponse<ProductResponse> updateProduct(
            @PathVariable("productId") @NotNull(message = "productId는 필수 값입니다.")
            @Min(value = 1, message = "productId는 1 이상 1,000,000,000 이하의 정수입니다.")
            @Max(value = 1_000_000_000, message = "productId는 1 이상 1,000,000,000 이하의 정수입니다.")
            Long productId,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        Product product;
        try {
            product = productFacade.updateProduct(productId, request.categoryId(), request.brandId(), request.price());
        } catch (BrandNotFoundException e) {
            throw new CommonHttpException(ErrorCode.BRAND_NOT_FOUND, HttpStatus.NOT_FOUND);
        } catch (CategoryNotFoundException e) {
            throw new CommonHttpException(ErrorCode.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
        } catch (ProductNotFoundException e) {
            throw new CommonHttpException(ErrorCode.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return ApiResponse.of(ProductResponse.from(product));
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<Void> deleteProduct(
            @PathVariable("productId") @NotNull(message = "productId는 필수 값입니다.")
            @Min(value = 1, message = "productId는 1 이상 1,000,000,000 이하의 정수입니다.")
            @Max(value = 1_000_000_000, message = "productId는 1 이상 1,000,000,000 이하의 정수입니다.") Long productId
    ) {
        try {
            productFacade.deleteProduct(productId);
        } catch (ProductNotFoundException e) {
            throw new CommonHttpException(ErrorCode.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return ApiResponse.of(null);
    }
}
