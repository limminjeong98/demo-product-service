package org.example.demoproductservice.application;

import java.util.List;

public record BrandCoordSet(String brandName, List<BrandCoordItem> items, Long totalPrice) {
    public Long getTotalPrice() {
        return totalPrice;
    }
}
