package org.example.demoproductservice.domain.repository;

import org.example.demoproductservice.domain.repository.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryType(Category.CategoryType categoryType);
}
