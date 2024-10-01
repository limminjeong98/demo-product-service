package org.example.demoproductservice.interfaces.dto;

import org.example.demoproductservice.domain.repository.entity.Brand;

public record BrandResponse(Long id, String brandName) {
    public static BrandResponse from(Brand brand) {
        return new BrandResponse(brand.getId(), brand.getBrandName());
    }
}
