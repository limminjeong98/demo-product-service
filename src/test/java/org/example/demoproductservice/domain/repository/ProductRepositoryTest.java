package org.example.demoproductservice.domain.repository;

import org.example.demoproductservice.config.JpaConfig;
import org.example.demoproductservice.domain.repository.entity.Brand;
import org.example.demoproductservice.domain.repository.entity.Category;
import org.example.demoproductservice.domain.repository.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.example.demoproductservice.domain.repository.entity.Category.CategoryType.*;
import static org.junit.jupiter.api.Assertions.*;

@Import(JpaConfig.class)
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository sut;

    @DisplayName("특정 카테고리에서 가격이 가장 낮은 상품을 조회한다")
    @Test
    void testFindTopByCategoryOrderByPriceAsc() {
        // given
        // BAG (id: 5) 카테고리
        Category category = new Category(5L, BAG, null);

        // when
        Product result = sut.findTopByCategoryOrderByPriceAsc(category);

        // then
        assertNotNull(result);
        assertEquals(BAG, result.getCategory().getCategoryType());
        assertEquals(5L, result.getId());
        assertEquals(2000, result.getPrice());
    }

    @DisplayName("특정 카테고리에서 가격이 가장 높은 상품을 조회한다")
    @Test
    void testFindTopByCategoryOrderByPriceDesc() {
        // given
        // TOP (id: 1) 카테고리
        Category category = new Category(1L, TOP, null);

        // when
        Product result = sut.findTopByCategoryOrderByPriceDesc(category);

        // then
        assertNotNull(result);
        assertEquals(TOP, result.getCategory().getCategoryType());
        assertEquals(65L, result.getId());
        assertEquals(11400, result.getPrice());
    }

    @DisplayName("특정 브랜드의 카테고리에서 가격이 가장 낮은 상품을 조회한다")
    @Test
    void testFindTopByBrandAndCategoryOrderByPriceAsc() {
        // given
        // B 브랜드 (id: 2)의 BOTTOM (id: 3) 카테고리
        Brand brand = new Brand(2L, null, null);
        Category category = new Category(3L, BOTTOM, null);

        // when
        Product result = sut.findTopByBrandAndCategoryOrderByPriceAsc(brand, category);

        // then
        assertNotNull(result);
        assertEquals(BOTTOM, result.getCategory().getCategoryType());
        assertEquals(11L, result.getId());
        assertEquals(3800L, result.getPrice());
    }

    @DisplayName("상품 조회")
    @Nested
    class FindProduct {

        @DisplayName("상품 id에 해당하는 상품을 조회한다")
        @Test
        void testFindById() {
            // given
            // id: 1, category: TOP, brand: A, price = 11,200
            Long id = 1L;

            // when
            Optional<Product> result = sut.findById(id);

            // then
            assertTrue(result.isPresent());
            assertEquals(id, result.get().getId());
            assertEquals("A", result.get().getBrand().getBrandName());
            assertEquals(TOP, result.get().getCategory().getCategoryType());
        }
    }

    @DisplayName("상품 등록, 수정")
    @Nested
    class SaveProduct {
        @DisplayName("상품을 신규 등록한다")
        @Test
        void testRegister() {
            // given
            Product product = new Product(null, new Category(1L, null, null), new Brand(1L, null, null), 500L);

            // when
            Product result = sut.save(product);

            // then
            assertNotNull(result);
            assertNotNull(result.getId());
            assertEquals(result.getCategory().getId(), 1L);
            assertEquals(result.getBrand().getId(), 1L);
            assertEquals(500L, result.getPrice());
        }

        @DisplayName("상품 정보를 수정한다")
        @Test
        void testUpdate() {
            // given
            Long id = 1L;
            Product product = new Product(id, new Category(1L, null, null), new Brand(1L, null, null), 1000L);

            // when
            Product result = sut.save(product);

            // then
            assertNotNull(result);
            assertEquals(id, result.getId());
            assertEquals(1000L, result.getPrice());
        }
    }

    @DisplayName("상품 삭제")
    @Nested
    class DeleteProduct {
        @DisplayName("상품 id에 해당하는 상품을 삭제한다")
        @Test
        void testDeleteById() {
            // given
            Product product = new Product(1L, null, null, null);

            // when
            sut.deleteById(product.getId());

            // then
            Optional<Product> result = sut.findById(product.getId());
            assertTrue(result.isEmpty());
        }
    }
}
