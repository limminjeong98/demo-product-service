package org.example.demoproductservice.interfaces.dto;

import org.example.demoproductservice.domain.repository.entity.Product;

public record ProductResponse(Long id, Long categoryId, Long brandId, Long price) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getCategory().getId(), product.getBrand().getId(), product.getPrice());
    }
}
