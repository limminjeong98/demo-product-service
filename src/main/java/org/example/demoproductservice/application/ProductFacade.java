package org.example.demoproductservice.application;

import org.example.demoproductservice.common.exception.BrandNotFoundException;
import org.example.demoproductservice.common.exception.CategoryNotFoundException;
import org.example.demoproductservice.common.exception.ProductNotFoundException;
import org.example.demoproductservice.domain.repository.entity.Brand;
import org.example.demoproductservice.domain.repository.entity.Category;
import org.example.demoproductservice.domain.repository.entity.Product;
import org.example.demoproductservice.domain.service.BrandService;
import org.example.demoproductservice.domain.service.CategoryService;
import org.example.demoproductservice.domain.service.ProductService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProductFacade {

    private final BrandService brandService;
    private final CategoryService categoryService;
    private final ProductService productService;

    public ProductFacade(BrandService brandService, CategoryService categoryService, ProductService productService) {
        this.brandService = brandService;
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
        if (categories.isEmpty()) {
            throw new CategoryNotFoundException();
        }

        for (Category category : categories) {
            Product product = productService.findLowestPriceProductByCategory(category);
            items.add(new CoordItem(category.getCategoryType().name(), product.getBrand().getBrandName(), product.getPrice()));
            totalPrice += product.getPrice();
        }
        return new CoordSet(items, totalPrice);
    }

    /**
     * 단일 브랜드로 전체 카테고리 상품을 구매할 경우, 상품 가격 총액이 가장 낮은 브랜드의 코디 정보(브랜드, 각 상품의 가격, 코디 상품 총액)를 반환
     */
    public BrandCoordSet getLowestPriceBrandProductSet() {

        List<Brand> brands = brandService.findAll();
        if (brands.isEmpty()) throw new BrandNotFoundException();

        List<Category> categories = categoryService.findAll();
        if (categories.isEmpty()) throw new CategoryNotFoundException();

        // 각 브랜드별 상품 조합 구하기
        Map<Brand, BrandCoordSet> brandCoordSetMap = new HashMap<>();

        for (Brand brand : brands) {
            Long brandTotalPrice = 0L;
            List<BrandCoordItem> items = new ArrayList<>();

            // 해당 브랜드에 모든 카테고리 상품이 등록되어 있어야 코디 상품을 만들 수 있다.
            boolean possible = true;
            for (Category category : categories) {
                Product product;
                try {
                    product = productService.findLowestPriceProductByBrandAndCategory(brand, category);
                } catch (ProductNotFoundException e) {
                    possible = false;
                    break;
                }
                brandTotalPrice += product.getPrice();
                items.add(new BrandCoordItem(category.getCategoryType().name(), product.getPrice()));
            }
            if (!possible) continue;
            brandCoordSetMap.put(brand, new BrandCoordSet(brand.getBrandName(), items, brandTotalPrice));
        }

        return brandCoordSetMap.values().stream().sorted(Comparator.comparing(BrandCoordSet::getTotalPrice)).findFirst().orElseThrow(RuntimeException::new);
    }

    /**
     * 카테고리별 최저, 최고 가격 상품 정보(카테고리, 브랜드, 상품 가격)를 반환
     */
    public CategoryProductSet getLowestAndHighestPriceProductByCategory(String categoryType) {
        Category category = categoryService.findByCategoryType(categoryType);
        if (category == null) return null;

        Product lowestPriceProduct;
        Product highestPriceProduct;
        try {
            lowestPriceProduct = productService.findLowestPriceProductByCategory(category);
            highestPriceProduct = productService.findHighestPriceProductByCategory(category);
        } catch (ProductNotFoundException e) {
            return null;
        }

        return new CategoryProductSet(category.getCategoryType().name(),
                new CategoryProductItem(lowestPriceProduct.getBrand().getBrandName(), lowestPriceProduct.getPrice()),
                new CategoryProductItem(highestPriceProduct.getBrand().getBrandName(), highestPriceProduct.getPrice())
        );
    }


    public Product findProductById(Long id) {
        return productService.findById(id);
    }

    public Product registerProduct(Long categoryId, Long brandId, Long price) {
        Category category = categoryService.findById(categoryId);
        Brand brand = brandService.findById(brandId);
        return productService.register(category, brand, price);
    }

    public Product updateProduct(Long id, Long categoryId, Long brandId, Long price) {
        Category category = categoryService.findById(categoryId);
        Brand brand = brandService.findById(brandId);
        return productService.update(id, category, brand, price);
    }

    public void deleteProduct(Long id) {
        productService.delete(id);
    }
}
