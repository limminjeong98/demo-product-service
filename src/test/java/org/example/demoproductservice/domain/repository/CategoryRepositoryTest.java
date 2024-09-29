package org.example.demoproductservice.domain.repository;

import org.example.demoproductservice.domain.repository.entity.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository sut;


    @DisplayName("모든 카테고리 목록을 조회한다")
    @Test
    void testFindAll() {
        // given
        // data.sql 실행되어 8개 카테고리가 저장되어 있음

        // when
        List<Category> result = sut.findAll();

        // then
        assertFalse(result.isEmpty());
        assertEquals(8, result.size());
    }
}