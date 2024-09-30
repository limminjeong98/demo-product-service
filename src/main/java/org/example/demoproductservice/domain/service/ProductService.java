package org.example.demoproductservice.domain.service;

import org.example.demoproductservice.common.exception.ProductNotFoundException;
import org.example.demoproductservice.domain.repository.ProductRepository;
import org.example.demoproductservice.domain.repository.entity.Brand;
import org.example.demoproductservice.domain.repository.entity.Category;
import org.example.demoproductservice.domain.repository.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Product findLowestPriceProductByBrandAndCategory(Brand brand, Category category) {
        Product product = productRepository.findTopByBrandAndCategoryOrderByPriceAsc(brand, category);
        if (product == null) throw new ProductNotFoundException();
        return product;
    }

    @Transactional(readOnly = true)
    public Product findLowestPriceProductByCategory(Category category) {
        Product product = productRepository.findTopByCategoryOrderByPriceAsc(category);
        // FIXME: 에러 메시지 정의
        if (product == null) throw new ProductNotFoundException();
        return product;
    }

    @Transactional(readOnly = true)
    public Product findHighestPriceProductByCategory(Category category) {
        Product product = productRepository.findTopByCategoryOrderByPriceDesc(category);
        // FIXME: 에러 메시지 정의
        if (product == null) throw new ProductNotFoundException();
        return product;
    }
}
