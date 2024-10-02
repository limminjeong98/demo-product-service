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
        if (product == null) throw new ProductNotFoundException();
        return product;
    }

    @Transactional(readOnly = true)
    public Product findHighestPriceProductByCategory(Category category) {
        Product product = productRepository.findTopByCategoryOrderByPriceDesc(category);
        if (product == null) throw new ProductNotFoundException();
        return product;
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    public Product register(Category category, Brand brand, Long price) {
        // 신규 등록일 경우 id는 null
        return productRepository.save(new Product(null, category, brand, price));
    }

    public Product update(Long id, Category category, Brand brand, Long price) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        return productRepository.save(new Product(id, category, brand, price));
    }

    public void delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        productRepository.deleteById(id);
    }
}
