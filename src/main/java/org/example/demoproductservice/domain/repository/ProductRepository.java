package org.example.demoproductservice.domain.repository;

import org.example.demoproductservice.domain.repository.entity.Brand;
import org.example.demoproductservice.domain.repository.entity.Category;
import org.example.demoproductservice.domain.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findTopByCategoryOrderByPriceAsc(Category category);

    Product findTopByBrandAndCategoryOrderByPriceAsc(Brand brand, Category category);
}
