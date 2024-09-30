package org.example.demoproductservice.domain.service;

import org.example.demoproductservice.common.exception.BrandNotFoundException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @DisplayName("브랜드를 조회한다")
    @Nested
    class FindBrandById {
        @DisplayName("저장된 브랜드를 조회한다")
        @Test
        void testFindBrandByIdSuccess() {
            // given
            brandRepository.prepareTestData();
            Long id = 1L;

            // when
            Brand result = sut.findById(id);

            // then
            assertNotNull(result);
            assertEquals(id, result.getId());
            verify(brandRepository).findById(id);
        }

        @DisplayName("조회한 id에 해당하는 브랜드를 없다면 예외가 발생한다")
        @Test
        void testFindBrandByIdFail() {
            // given
            Long id = 1L;

            // when & then
            assertThrows(BrandNotFoundException.class, () -> sut.findById(id));
        }
    }

    @DisplayName("브랜드를 저장한다")
    @Nested
    class RegisterBrand {

        @DisplayName("브랜드를 새로 등록한다")
        @Test
        void testRegisterBrand() {
            // given
            String brandName = "신규 브랜드";

            // when
            Brand result = sut.register(brandName);

            // then
            assertNotNull(result);
            assertNotNull(result.getId());
            assertEquals(brandName, result.getBrandName());
            verify(brandRepository).save(any());
        }
    }

    @DisplayName("브랜드를 업데이트한다")
    @Nested
    class UpdateBrand {

        @DisplayName("기존에 저장되어 있던 브랜드를 업데이트한다")
        @Test
        void testUpdateBrandSuccess() {
            // given
            brandRepository.prepareTestData();
            Long id = 1L;
            Brand brand = brandRepository.findById(id).get();
            String brandName = "업데이트한 브랜드 이름";

            // when
            Brand result = sut.update(id, brandName);

            // then
            assertNotNull(result);
            assertEquals(id, result.getId());
            assertNotEquals(brand.getBrandName(), result.getBrandName());
            assertEquals(brandName, result.getBrandName());
            verify(brandRepository).save(any());
        }

        @DisplayName("업데이트하려는 브랜드가 없을 경우 예외가 발생한다")
        @Test
        void testUpdateBrandFail() {
            // given
            brandRepository.deleteAll();
            Long id = 1L;

            // when & then
            assertThrows(BrandNotFoundException.class, () -> sut.update(id, "브랜드 이름"));
            verify(brandRepository, never()).save(any());
        }
    }

    @DisplayName("브랜드를 삭제한다")
    @Nested
    class DeleteBrandById {
        @DisplayName("저장된 브랜드를 삭제한다")
        @Test
        void testDeleteBrandByIdSuccess() {
            // given
            brandRepository.prepareTestData();
            Long id = 1L;

            // when
            sut.delete(id);

            // then
            Optional<Brand> result = brandRepository.findById(id);
            assertTrue(result.isEmpty());
            verify(brandRepository, atLeastOnce()).findById(id);
            verify(brandRepository).deleteById(id);
        }

        @DisplayName("삭제하려는 브랜드 id에 해당하는 브랜드가 없다면 예외가 발생한다")
        @Test
        void testDeleteBrandByIdFail() {
            // given
            Long id = 1L;

            // when & then
            assertThrows(BrandNotFoundException.class, () -> sut.delete(id));
            verify(brandRepository).findById(id);
            verify(brandRepository, never()).deleteById(id);
        }
    }
}