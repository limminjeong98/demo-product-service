package org.example.demoproductservice.domain.repository;

import org.example.demoproductservice.config.JpaConfig;
import org.example.demoproductservice.domain.repository.entity.Brand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import(JpaConfig.class)
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class BrandRepositoryTest {

    @Autowired
    BrandRepository sut;

    @DisplayName("브랜드 조회")
    @Nested
    class FindBrand {
        @DisplayName("모든 브랜드 목록을 조회한다")
        @Test
        void testFindAll() {
            // given
            // data.sql 실행되어 브랜드 9개가 저장되어 있음

            // when
            List<Brand> brands = sut.findAll();

            // then
            assertFalse(brands.isEmpty());
            assertEquals(9, brands.size());
        }

        @DisplayName("브랜드 id에 해당하는 브랜드를 조회한다")
        @Test
        void testFindById() {
            // given
            // id: 1, brandName: 'A'
            Long id = 1L;

            // when
            Optional<Brand> result = sut.findById(id);

            // then
            assertTrue(result.isPresent());
            assertEquals(id, result.get().getId());
            assertEquals("A", result.get().getBrandName());
        }
    }


    @DisplayName("브랜드 수정, 업데이트")
    @Nested
    class SaveBrand {

        @DisplayName("브랜드를 신규 등록한다")
        @Test
        void testRegister() {
            // given
            Brand brand = new Brand(null, "신규 브랜드");

            // when
            Brand save = sut.save(brand);

            // then
            assertNotNull(save);
            assertNotNull(save.getId());
            assertEquals("신규 브랜드", save.getBrandName());
        }

        @DisplayName("브랜드 정보를 수정한다")
        @Test
        void testUpdate() {
            // given
            Brand brand = new Brand(1L, "수정한 브랜드 A 이름");

            // when
            Brand save = sut.save(brand);

            // then
            assertNotNull(save);
            assertEquals(brand.getId(), save.getId());
            assertEquals("수정한 브랜드 A 이름", save.getBrandName());
        }

    }

    @DisplayName("브랜드 삭제")
    @Nested
    class DeleteBrand {

        @DisplayName("브랜드 id에 해당하는 브랜드를 삭제한다")
        @Test
        void testDeleteById() {
            // given
            Brand brand = new Brand(1L, null);

            // when
            sut.delete(brand);

            // then
            Optional<Brand> result = sut.findById(brand.getId());
            assertTrue(result.isEmpty());
        }
    }
}