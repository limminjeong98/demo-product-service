package org.example.demoproductservice.domain.repository;

import org.example.demoproductservice.domain.repository.entity.Brand;
import org.example.demoproductservice.domain.repository.entity.Category;
import org.example.demoproductservice.domain.repository.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.example.demoproductservice.domain.repository.entity.Category.CategoryType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}
