package org.example.demoproductservice.domain.service;

import org.example.demoproductservice.common.exception.ProductNotFoundException;
import org.example.demoproductservice.domain.repository.ProductRepositoryStub;
import org.example.demoproductservice.domain.repository.entity.Category;
import org.example.demoproductservice.domain.repository.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.example.demoproductservice.domain.repository.entity.Category.CategoryType.TOP;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Spy
    ProductRepositoryStub productRepository;

    @InjectMocks
    ProductService sut;

    @DisplayName("카테고리에서 가격이 가장 낮은 상품의 브랜드와 상품가격을 조회한다")
    @Nested
    class GetLowestPriceProductOfCertainCategory {

        final Category.CategoryType categoryType = TOP;
        final Category category = new Category(null, categoryType, null);

        @DisplayName("상의 카테고리 상품 중 가격이 가장 낮은 상품의 브랜드와 상품가격을 조회한다")
        @Test
        void test1() {
            // given
            // 상의(TOP) 카테고리 상품 중 가격이 가장 낮은 상품의 가격은 10,000원이고 브랜드는 C
            productRepository.prepareTestData();

            // when
            Product result = sut.findLowestPriceProductByCategory(category);

            // then
            assertNotNull(result);
            assertEquals(result.getPrice(), 10000L);
            assertEquals(result.getBrand().getBrandName(), "C");
            verify(productRepository).findTopByCategoryOrderByPriceAsc(category);
        }

        @DisplayName("카테고리에 상품이 1개도 존재하지 않으면 에러를 반환한다")
        @Test
        void test2() {
            // given
            // 상의(TOP) 카테고리를 포함한 모든 카테고리에 상품이 존재하지 않도록 초기화
            productRepository.deleteAll();

            // when & then
            assertThrows(ProductNotFoundException.class, () -> sut.findLowestPriceProductByCategory(category));
            verify(productRepository).findTopByCategoryOrderByPriceAsc(category);
        }
    }
}
