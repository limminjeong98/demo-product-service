package org.example.demoproductservice.application;

public record CategoryProductSet(String categoryType, CategoryProductItem lowestPriceProduct,
                                 CategoryProductItem highestPriceProduct) {
}
