package org.example.demoproductservice.domain.service;

import org.example.demoproductservice.common.exception.ProductNotFoundException;
import org.example.demoproductservice.domain.repository.ProductRepository;
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
    public Product findLowestPriceProductByCategory(Category category) {
        Product product = productRepository.findTopByCategoryOrderByPriceAsc(category);
        if (product == null) throw new ProductNotFoundException();
        return product;
    }
}
