package org.example.demoproductservice.application;

import org.example.demoproductservice.common.exception.ProductNotFoundException;
import org.example.demoproductservice.domain.repository.CategoryRepositoryStub;
import org.example.demoproductservice.domain.repository.ProductRepositoryStub;
import org.example.demoproductservice.domain.repository.entity.Category;
import org.example.demoproductservice.domain.repository.entity.Product;
import org.example.demoproductservice.domain.service.CategoryService;
import org.example.demoproductservice.domain.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.example.demoproductservice.domain.repository.entity.Category.CategoryType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductFacadeTest {

    @Spy
    ProductRepositoryStub productRepository;

    @Mock
    ProductService productService;

    @Spy
    CategoryRepositoryStub categoryRepository;

    @Mock
    CategoryService categoryService;

    @InjectMocks
    ProductFacade sut;

    @DisplayName("각 카테고리별 가격이 가장 낮은 상품으로 구성된 코디 정보를 조회한다")
    @Nested
    class GetLowestPriceProductSet {
        @DisplayName("각 카테고리에서 가격이 가장 낮은 상품의 브랜드와 가격, 코디 상품 총액을 반환하는지 검증한다")
        @Test
        void test1() {
            // given
            // 테스트 데이터 세팅
            categoryRepository.prepareTestData();
            productRepository.prepareTestData();

            // 카테고리: 모든 카테고리 타입 (TOP ~ ACCESSORY) 8개
            List<Category> categories = categoryRepository.findAll();
            given(categoryService.findAll()).willReturn(categories);

            // productService 는 각 카테고리에 해당하는 상품 중 가격이 가장 낮은 상품을 반환
            for (Category category : categories) {
                Product product = productRepository.findTopByCategoryOrderByPriceAsc(category);
                given(productService.findLowestPriceProductByCategory(category)).willReturn(product);
            }

            // when
            CoordSet result = sut.getLowestPriceProductSet();

            // then
            assertNotNull(result);
            assertEquals(34100L, result.totalPrice());
            List<CoordItem> items = result.items();
            assertEquals(8, items.size());
            for (CoordItem item : items) {
                if (item.categoryType().equals(TOP.name())) {
                    // 상의 10,000 C
                    assertEquals("C", item.brandName());
                    assertEquals(10000L, item.price());
                } else if (item.categoryType().equals(OUTER.name())) {
                    // 아우터 5,000 E
                    assertEquals("E", item.brandName());
                    assertEquals(5000L, item.price());
                } else if (item.categoryType().equals(BOTTOM.name())) {
                    // 바지 3,000 D
                    assertEquals("D", item.brandName());
                    assertEquals(3000L, item.price());
                } else if (item.categoryType().equals(SNEAKERS.name())) {
                    // 스니커즈 9,000 A
                    assertEquals("A", item.brandName());
                    assertEquals(9000L, item.price());
                } else if (item.categoryType().equals(BAG.name())) {
                    // 가방 2,000 A
                    assertEquals("A", item.brandName());
                    assertEquals(2000L, item.price());
                } else if (item.categoryType().equals(HAT.name())) {
                    // 모자 1,500 D
                    assertEquals("D", item.brandName());
                    assertEquals(1500L, item.price());
                } else if (item.categoryType().equals(SOCKS.name())) {
                    // 양말 1,700 I
                    assertEquals("I", item.brandName());
                    assertEquals(1700L, item.price());
                } else if (item.categoryType().equals(ACCESSORY.name())) {
                    // 액세서리 1,900 F
                    assertEquals("F", item.brandName());
                    assertEquals(1900L, item.price());
                }
            }
        }

        @DisplayName("상품 서비스 레이어에서 카테고리에 등록된 상품이 없어 발생한 에러가 전파되는지 검증한다")
        @Test
        void test2() {
            // given
            // 테스트 데이터 세팅
            categoryRepository.prepareTestData();

            // 카테고리: 모든 카테고리 타입 (TOP ~ ACCESSORY) 8개
            List<Category> categories = categoryRepository.findAll();
            given(categoryService.findAll()).willReturn(categories);

            // 카테고리에 등록된 상품이 없어서 예외가 발생
            given(productService.findLowestPriceProductByCategory(any())).willThrow(ProductNotFoundException.class);

            // when & then
            assertThrows(ProductNotFoundException.class, () -> sut.getLowestPriceProductSet());
        }
    }


}