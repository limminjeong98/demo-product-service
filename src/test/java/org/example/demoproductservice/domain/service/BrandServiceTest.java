package org.example.demoproductservice.domain.service;

import org.example.demoproductservice.domain.repository.BrandRepositoryStub;
import org.example.demoproductservice.domain.repository.entity.Brand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {

    @Spy
    BrandRepositoryStub brandRepository;

    @InjectMocks
    BrandService sut;

    @DisplayName("모든 브랜드 목록을 조회한다")
    @Nested
    class GetAllBrands {
        @DisplayName("저장된 모든 브랜드를 반환한다")
        @Test
        void test1() {
            // given
            brandRepository.prepareTestData();

            // when
            List<Brand> result = sut.findAll();

            // then
            assertNotNull(result);
            assertEquals(9, result.size());
            verify(brandRepository).findAll();
        }

        @DisplayName("저장된 브랜드가 없으면 빈 리스트를 반환한다")
        @Test
        void test2() {
            // given
            brandRepository.deleteAll();

            // when
            List<Brand> result = sut.findAll();

            // then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(brandRepository).findAll();
        }
    }
}