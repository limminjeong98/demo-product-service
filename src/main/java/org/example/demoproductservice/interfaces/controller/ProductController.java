package org.example.demoproductservice.interfaces.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.demoproductservice.application.BrandCoordSet;
import org.example.demoproductservice.application.CategoryProductSet;
import org.example.demoproductservice.application.CoordSet;
import org.example.demoproductservice.application.ProductFacade;
import org.example.demoproductservice.common.controller.ApiResponse;
import org.example.demoproductservice.common.exception.*;
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

    @GetMapping("/coordi-set/minimum-total-cost")
    public ApiResponse<CoordSet> getMinimumTotalCostProductSet() {
        CoordSet coordSet;
        try {
            coordSet = productFacade.getLowestPriceProductSet();
        } catch (AtLeastOneCategoryRequiredException e) {
            throw new CommonHttpException(ErrorCode.AT_LEAST_ONE_CATEGORY_REQUIRED, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AtLeastOneProductRegisteredToCategory e) {
            throw new CommonHttpException(ErrorCode.AT_LEAST_ONE_PRODUCT_REGISTERED_TO_EACH_CATEGORY, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ApiResponse.of(coordSet);
    }

    @GetMapping("/coordi-set/minimum-total-cost/one-brand")
    public ApiResponse<BrandCoordSet> getMinimumTotalCostOneBrandProductSet() {
        BrandCoordSet brandCoordSet;
        try {
            brandCoordSet = productFacade.getLowestPriceBrandProductSet();
        } catch (AtLeastOneBrandRequiredException e) {
            throw new CommonHttpException(ErrorCode.AT_LEAST_ONE_BRAND_REQUIRED, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AtLeastOneCategoryRequiredException e) {
            throw new CommonHttpException(ErrorCode.AT_LEAST_ONE_CATEGORY_REQUIRED, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AtLeastOneBrandRegisterAllCategoriesException e) {
            throw new CommonHttpException(ErrorCode.AT_LEAST_ONE_BRAND_REGISTER_ALL_CATEGORIES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ApiResponse.of(brandCoordSet);
    }

    @GetMapping("/lowest-and-highest-price")
    public ApiResponse<CategoryProductSet> getLowestAndHighestPriceProducts(@RequestParam(required = true, name = "categoryType") @NotBlank(message = "categoryType(카테고리명)은 필수 값입니다.") String categoryType) {
        CategoryProductSet productSet;
        try {
            productSet = productFacade.getLowestAndHighestPriceProductByCategory(categoryType);
        } catch (AtLeastOneProductRegisteredToCategory e) {
            throw new CommonHttpException(ErrorCode.AT_LEAST_ONE_PRODUCT_REGISTERED_TO_CATEGORY, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (CategoryNotFoundException e) {
            throw new CommonHttpException(ErrorCode.CATEGORY_NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ApiResponse.of(productSet);
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
