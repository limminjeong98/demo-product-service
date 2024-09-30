package org.example.demoproductservice.application;

import org.example.demoproductservice.domain.repository.entity.Category;
import org.example.demoproductservice.domain.repository.entity.Product;
import org.example.demoproductservice.domain.service.CategoryService;
import org.example.demoproductservice.domain.service.ProductService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductFacade {

    private final CategoryService categoryService;
    private final ProductService productService;

    public ProductFacade(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    /**
     * 각 카테고리별 가격이 가장 낮은 상품으로 구성된 코디 정보(각 상품의 브랜드와 가격, 코디 상품 총액)를 반환
     */
    public CoordSet getLowestPriceProductSet() {
        List<CoordItem> items = new ArrayList<>();
        Long totalPrice = 0L;

        List<Category> categories = categoryService.findAll();

        for (Category category : categories) {
            Product product = productService.findLowestPriceProductByCategory(category);
            items.add(
                    new CoordItem(
                            category.getCategoryType().name(),
                            product.getBrand().getBrandName(),
                            product.getPrice()));
            totalPrice += product.getPrice();
        }
        return new CoordSet(items, totalPrice);
    }


}
