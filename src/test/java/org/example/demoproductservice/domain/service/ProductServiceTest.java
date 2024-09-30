package org.example.demoproductservice.domain.service;

import org.example.demoproductservice.common.exception.ProductNotFoundException;
import org.example.demoproductservice.domain.repository.ProductRepositoryStub;
import org.example.demoproductservice.domain.repository.entity.Brand;
import org.example.demoproductservice.domain.repository.entity.Category;
import org.example.demoproductservice.domain.repository.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.example.demoproductservice.domain.repository.entity.Category.CategoryType.BOTTOM;
import static org.example.demoproductservice.domain.repository.entity.Category.CategoryType.TOP;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Spy
    ProductRepositoryStub productRepository;

    @InjectMocks
    ProductService sut;

    @DisplayName("특정 브랜드의 카테고리에서 가격이 가장 낮은 상품을 조회한다")
    @Nested
    class GetLowestPriceProductOfCertainBrandAndCategory {

        final Brand brand = new Brand(null, "E");
        final Category.CategoryType categoryType = BOTTOM;
        final Category category = new Category(null, categoryType, null);

        @DisplayName("E 브랜드의 바지 카테고리 상품 중 가격이 가장 낮은 상품을 조회한다")
        @Test
        void test1() {
            // given
            // E브랜드의 바지(BOTTOM) 카테고리 상품 중 가격이 가장 낮은 상품의 가격은 3,800원
            productRepository.prepareTestData();
            // E브랜드의 바지(BOTTOM) 카테고리에 가격이 10,000원 상품 추가
            productRepository.addTestData(new Product(null, new Category(null, BOTTOM, null), new Brand(null, "E", null), 10000L));

            // when
            Product result = sut.findLowestPriceProductByBrandAndCategory(brand, category);

            // then
            assertNotNull(result);
            assertEquals(3800L, result.getPrice());
            assertEquals("E", result.getBrand().getBrandName());
            verify(productRepository).findTopByBrandAndCategoryOrderByPriceAsc(brand, category);
        }

        @DisplayName("해당 브랜드의 카테고리에 상품이 1개도 존재하지 않으면 에러를 반환한다")
        @Test
        void test2() {
            // given
            // E 브랜드의 바지(BOTTOM) 카테고리를 포함한 모든 카테고리에 상품이 존재하지 않도록 초기화
            productRepository.deleteAll();

            // when & then
            assertThrows(ProductNotFoundException.class, () -> sut.findLowestPriceProductByBrandAndCategory(brand, category));
            verify(productRepository).findTopByBrandAndCategoryOrderByPriceAsc(brand, category);
        }
    }

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
            assertEquals(10000L, result.getPrice());
            assertEquals("C", result.getBrand().getBrandName());
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


    @DisplayName("카테고리에서 가격이 가장 높은 상품의 브랜드와 상품가격을 조회한다")
    @Nested
    class GetHighestPriceProductOfCertainCategory {

        final Category.CategoryType categoryType = TOP;
        final Category category = new Category(null, categoryType, null);

        @DisplayName("상의 카테고리 상품 중 가격이 가장 높은 상품의 브랜드와 상품가격을 조회한다")
        @Test
        void test1() {
            // given
            // 상의(TOP) 카테고리 상품 중 가격이 높은 낮은 상품의 가격은 11,400원이고 브랜드는 I
            productRepository.prepareTestData();

            // when
            Product result = sut.findHighestPriceProductByCategory(category);

            // then
            assertNotNull(result);
            assertEquals(11400L, result.getPrice());
            assertEquals("I", result.getBrand().getBrandName());
            verify(productRepository).findTopByCategoryOrderByPriceDesc(category);
        }

        @DisplayName("카테고리에 상품이 1개도 존재하지 않으면 에러를 반환한다")
        @Test
        void test2() {
            // given
            // 상의(TOP) 카테고리를 포함한 모든 카테고리에 상품이 존재하지 않도록 초기화
            productRepository.deleteAll();

            // when & then
            assertThrows(ProductNotFoundException.class, () -> sut.findHighestPriceProductByCategory(category));
            verify(productRepository).findTopByCategoryOrderByPriceDesc(category);
        }
    }

    @DisplayName("상품 조회")
    @Nested
    class FindById {

        @DisplayName("상품 id로 상품을 조회한다")
        @Test
        void testSuccess() {
            // given
            productRepository.prepareTestData();
            Long id = 1L;

            // when
            Product result = sut.findById(id);

            // then
            assertNotNull(result);
            assertEquals(id, result.getId());
            verify(productRepository).findById(id);
        }

        @DisplayName("상품 id에 해당하는 상품이 없다면 예외가 발생한다")
        @Test
        void testFail() {
            // given
            Long id = 1L;

            // when & then
            assertThrows(ProductNotFoundException.class, () -> sut.findById(id));
        }
    }

    @DisplayName("상품 등록")
    @Nested
    class RegisterProduct {
        @DisplayName("상품을 신규 등록한다")
        @Test
        void testRegisterProduct() {
            // given
            Category category = new Category(1L, null, null);
            Brand brand = new Brand(1L, null);
            Long price = 1000L;

            // when
            Product result = sut.register(category, brand, price);

            // then
            assertNotNull(result);
            assertEquals(category.getId(), result.getCategory().getId());
            assertEquals(price, result.getPrice());
        }
    }

    @DisplayName("상품 수정")
    @Nested
    class UpdateProduct {
        @DisplayName("기존에 저장되어 있던 상품을 업데이트한다")
        @Test
        void testUpdateProductSuccess() {
            // given
            productRepository.prepareTestData();
            Long id = 1L;
            Product product = productRepository.findById(id).get();
            Brand previousBrand = product.getBrand();
            Category previousCategory = product.getCategory();
            Long previousPrice = product.getPrice();


            Category newCategory = new Category(11L, null, null);
            Brand newBrand = new Brand(11L, null);
            Long newPrice = 100L;

            // when
            Product result = sut.update(id, newCategory, newBrand, newPrice);

            // then
            assertNotNull(result);
            assertNotEquals(previousCategory.getId(), result.getCategory().getId());
            assertEquals(newCategory.getId(), result.getCategory().getId());
            assertNotEquals(previousBrand.getId(), result.getBrand().getId());
            assertEquals(newBrand.getId(), result.getBrand().getId());
            assertNotEquals(previousPrice, result.getPrice());
            assertEquals(newPrice, result.getPrice());
        }

        @DisplayName("업데이트하려는 상품이 없을 경우 예외가 발생한다")
        @Test
        void testUpdateProductFail() {
            // given
            Long id = 1L;
            Category category = new Category(1L, null, null);
            Brand brand = new Brand(1L, null);
            Long price = 1000L;

            // when & then
            assertThrows(ProductNotFoundException.class, () -> sut.update(id, category, brand, price));
        }
    }

    @DisplayName("상품 삭제")
    @Nested
    class DeleteProduct {
        @DisplayName("저장된 상품을 삭제한다")
        @Test
        void testDeleteProductByIdSuccess() {
            // given
            productRepository.prepareTestData();
            Long id = 1L;

            // when
            sut.delete(id);

            // then
            Optional<Product> result = productRepository.findById(id);
            assertTrue(result.isEmpty());
            verify(productRepository, atLeastOnce()).findById(id);
            verify(productRepository).deleteById(id);
        }

        @DisplayName("삭제하려는 상품 id에 해당하는 상품이 없다면 예외가 발생한다")
        @Test
        void testDeleteProductByIdFail() {
            // given
            Long id = 1L;

            // when & then
            assertThrows(ProductNotFoundException.class, () -> sut.delete(id));
            verify(productRepository).findById(id);
            verify(productRepository, never()).deleteById(id);
        }
    }
}
