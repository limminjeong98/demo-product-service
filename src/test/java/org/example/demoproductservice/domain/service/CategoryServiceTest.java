package org.example.demoproductservice.domain.service;

import org.example.demoproductservice.common.exception.CategoryNotFoundException;
import org.example.demoproductservice.domain.repository.CategoryRepositoryStub;
import org.example.demoproductservice.domain.repository.entity.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.example.demoproductservice.domain.repository.entity.Category.CategoryType.TOP;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Spy
    CategoryRepositoryStub categoryRepository;

    @InjectMocks
    CategoryService sut;

    @DisplayName("모든 카테고리 목록을 조회한다")
    @Nested
    class findAll {
        @DisplayName("저장된 모든 카테고리를 반환한다")
        @Test
        void test1() {
            // given
            categoryRepository.prepareTestData();

            // when
            List<Category> result = sut.findAll();

            // then
            assertFalse(result.isEmpty());
            assertEquals(8, result.size());
            verify(categoryRepository).findAll();
        }

        @DisplayName("저장된 카테고리가 없으면 빈 리스트를 반환한다")
        @Test
        void test2() {
            // given
            categoryRepository.deleteAll();

            // when
            List<Category> result = sut.findAll();

            // then
            assertTrue(result.isEmpty());
            verify(categoryRepository).findAll();
        }
    }

    @DisplayName("카테고리 타입으로 카테고리를 조회한다")
    @Nested
    class findByCategoryType {
        @DisplayName("존재하는 카테고리 타입이면 카테고리를 반환한다")
        @Test
        void testSuccess() {
            // given
            categoryRepository.prepareTestData();
            String categoryType = "TOP";

            // when
            Category result = sut.findByCategoryType(categoryType);

            // then
            assertNotNull(result);
            assertEquals(TOP, result.getCategoryType());
            verify(categoryRepository).findByCategoryType(TOP);
        }

        @DisplayName("존재하지 않는 카테고리 타입이면 null을 반환한다")
        @Test
        void testFail() {
            // given
            categoryRepository.prepareTestData();
            String categoryType = "INVALID_CATEGORY_TYPE";

            // when & then
            assertThrows(CategoryNotFoundException.class, () -> sut.findByCategoryType(categoryType));
        }
    }
}