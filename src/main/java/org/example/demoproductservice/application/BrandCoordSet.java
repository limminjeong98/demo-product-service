package org.example.demoproductservice.application;

import java.util.List;

public record BrandCoordSet(String brandName, List<BrandCoordItem> items, Long totalCost) {
    public Long getTotalPrice() {
        return totalCost;
    }
}
