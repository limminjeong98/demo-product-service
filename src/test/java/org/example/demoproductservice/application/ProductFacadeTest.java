package org.example.demoproductservice.application;

import org.example.demoproductservice.common.exception.AtLeastOneBrandRequiredException;
import org.example.demoproductservice.common.exception.ProductNotFoundException;
import org.example.demoproductservice.domain.repository.BrandRepositoryStub;
import org.example.demoproductservice.domain.repository.CategoryRepositoryStub;
import org.example.demoproductservice.domain.repository.ProductRepositoryStub;
import org.example.demoproductservice.domain.repository.entity.Brand;
import org.example.demoproductservice.domain.repository.entity.Category;
import org.example.demoproductservice.domain.repository.entity.Product;
import org.example.demoproductservice.domain.service.BrandService;
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

import java.util.ArrayList;
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

    @Spy
    BrandRepositoryStub brandRepository;

    @Mock
    BrandService brandService;

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
            assertEquals(34100L, result.totalCost());
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


    @DisplayName("단일 브랜드로 전체 카테고리 상품을 구매할 경우, 상품 가격 총액이 가장 낮은 코디 정보를 조회한다")
    @Nested
    class GetLowestPriceBrandProductSet {
        @DisplayName("각 브랜드의 카테고리별 상품 가격 총액이 가장 낮은 코디 상품을 반환하는지 검증한다")
        @Test
        void test1() {
            // given
            // 테스트 데이터 세팅
            brandRepository.prepareTestData();
            categoryRepository.prepareTestData();
            productRepository.prepareTestData();

            List<Brand> brands = brandRepository.findAll();
            given(brandService.findAll()).willReturn(brands);

            List<Category> categories = categoryRepository.findAll();
            given(categoryService.findAll()).willReturn(categories);

            for (Brand brand : brands) {
                for (Category category : categories) {
                    Product product = productRepository.findTopByBrandAndCategoryOrderByPriceAsc(brand, category);
                    given(productService.findLowestPriceProductByBrandAndCategory(brand, category)).willReturn(product);
                }
            }

            // when
            BrandCoordSet result = sut.getLowestPriceBrandProductSet();

            // then
            assertNotNull(result);
            assertEquals("D", result.brandName());
            assertEquals(36100L, result.totalCost());
            List<BrandCoordItem> items = result.items();
            for (BrandCoordItem item : items) {
                if (item.categoryType().equals(TOP.name())) {
                    assertEquals(10100L, item.price());
                } else if (item.categoryType().equals(OUTER.name())) {
                    assertEquals(5100L, item.price());
                } else if (item.categoryType().equals(BOTTOM.name())) {
                    assertEquals(3000L, item.price());
                } else if (item.categoryType().equals(SNEAKERS.name())) {
                    assertEquals(9500L, item.price());
                } else if (item.categoryType().equals(BAG.name())) {
                    assertEquals(2500L, item.price());
                } else if (item.categoryType().equals(HAT.name())) {
                    assertEquals(1500L, item.price());
                } else if (item.categoryType().equals(SOCKS.name())) {
                    assertEquals(2400L, item.price());
                } else if (item.categoryType().equals(ACCESSORY.name())) {
                    assertEquals(2000L, item.price());
                }
            }
        }


        @DisplayName("브랜드의 일부 카테고리에 상품이 등록되어 있지 않으면 해당 브랜드는 코디 세트에 제외되는지 검증한다")
        @Test
        void test2() {
            // given
            // 테스트 데이터 세팅
            brandRepository.prepareTestData();
            categoryRepository.prepareTestData();
            productRepository.prepareTestData();
            // D 브랜드의 TOP 상품이 없도록 세팅
            productRepository.deleteById(25L);

            List<Brand> brands = brandRepository.findAll();
            given(brandService.findAll()).willReturn(brands);

            List<Category> categories = categoryRepository.findAll();
            given(categoryService.findAll()).willReturn(categories);

            for (Brand brand : brands) {
                for (Category category : categories) {
                    Product product = productRepository.findTopByBrandAndCategoryOrderByPriceAsc(brand, category);
                    if (product != null) {
                        given(productService.findLowestPriceProductByBrandAndCategory(brand, category)).willReturn(product);
                    } else {
                        given(productService.findLowestPriceProductByBrandAndCategory(brand, category)).willThrow(ProductNotFoundException.class);
                        // 해당 브랜드에서 특정 카테고리 상품이 없는 경우 더 이상 진행 않고, 다른 브랜드를 처리하도록 작성함
                        // UnnecessaryStubbingException 예외 방지하기 위해서 break 추가
                        break;
                    }
                }
            }

            // when
            BrandCoordSet result = sut.getLowestPriceBrandProductSet();

            // then
            assertNotNull(result);
            assertNotEquals("D", result.brandName());
            assertNotEquals(36100L, result.totalCost());
            assertEquals("C", result.brandName());
            assertEquals(37100L, result.totalCost());
        }

        @DisplayName("브랜드가 1개도 등록되어있지 않으면 에러를 반환하는지 검증한다")
        @Test
        void test3() {
            // given
            // 테스트 데이터 세팅
            given(brandService.findAll()).willReturn(new ArrayList<>());

            // when & then
            assertThrows(AtLeastOneBrandRequiredException.class, () -> sut.getLowestPriceBrandProductSet());
        }
    }

    @DisplayName("카테고리별 최저, 최고 가격 상품 정보를 조회한다")
    @Nested
    class GetLowestAndHighestPriceProductByCategory {
        @DisplayName("카테고리에서 가격이 가장 낮은 상품과 높은 상품의 브랜드와 가격을 반환하는지 검증한다")
        @Test
        void test1() {
            // given
            // 테스트 데이터 세팅
            brandRepository.prepareTestData();
            categoryRepository.prepareTestData();
            productRepository.prepareTestData();

            String categoryType = "TOP";
            Category category = categoryRepository.findByCategoryType(Category.CategoryType.valueOf(categoryType));
            given(categoryService.findByCategoryType(category.getCategoryType().name())).willReturn(category);
            Product lowestProduct = productRepository.findTopByCategoryOrderByPriceAsc(category);
            given(productService.findLowestPriceProductByCategory(category)).willReturn(lowestProduct);
            Product highestProduct = productRepository.findTopByCategoryOrderByPriceDesc(category);
            given(productService.findHighestPriceProductByCategory(category)).willReturn(highestProduct);

            // when
            CategoryProductSet result = sut.getLowestAndHighestPriceProductByCategory(categoryType);

            // then
            assertNotNull(result);
            assertEquals("TOP", result.categoryType());
            assertEquals("I", result.highestPriceProduct().brandName());
            assertEquals(11400L, result.highestPriceProduct().price());
            assertEquals("C", result.lowestPriceProduct().brandName());
            assertEquals(10000L, result.lowestPriceProduct().price());
        }
    }
}