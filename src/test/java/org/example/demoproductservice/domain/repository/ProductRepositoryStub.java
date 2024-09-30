package org.example.demoproductservice.domain.repository;

import org.example.demoproductservice.domain.repository.entity.Brand;
import org.example.demoproductservice.domain.repository.entity.Category;
import org.example.demoproductservice.domain.repository.entity.Product;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import static org.example.demoproductservice.domain.repository.entity.Category.CategoryType.*;

public class ProductRepositoryStub implements ProductRepository {

    private final List<Product> products = new ArrayList<>();
    private final AtomicLong productIdGenerator = new AtomicLong(1);

    private final Map<Category.CategoryType, Category> categories = new HashMap<>();
    private final AtomicLong categoryIdGenerator = new AtomicLong(1);

    private final Map<String, Brand> brands = new HashMap<>();
    private final AtomicLong brandIdGenerator = new AtomicLong(1);

    public void prepareTestData() {

        // 브랜드 A~I 등록
        int asciiCodeOfA = 'A';
        for (int i = 0; i < 9; i++) {
            String brandName = String.valueOf((char) (asciiCodeOfA + i));
            Brand brand = new Brand(brandIdGenerator.getAndIncrement(), brandName, null);
            brands.put(brandName, brand);
        }

        // 카테고리 TOP~ACCESSORY 등록
        for (Category.CategoryType categoryType : Category.getAllCategoryTypes()) {
            Category category = new Category(categoryIdGenerator.getAndIncrement(), categoryType, null);
            categories.put(categoryType, category);
        }

        // A 브랜드 상품 등록
        // 상품 총액 37,700
        Brand brandA = brands.get("A");
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(TOP), brandA, 11200L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(OUTER), brandA, 5500L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(BOTTOM), brandA, 4200L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(SNEAKERS), brandA, 9000L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(BAG), brandA, 2000L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(HAT), brandA, 1700L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(SOCKS), brandA, 1800L));
        products.add(
                new Product(productIdGenerator.getAndIncrement(), categories.get(ACCESSORY), brandA, 2300L));

        // B 브랜드 상품 등록
        // 상품 총액 37,600
        Brand brandB = brands.get("B");
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(TOP), brandB, 10500L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(OUTER), brandB, 5900L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(BOTTOM), brandB, 3800L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(SNEAKERS), brandB, 9100L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(BAG), brandB, 2100L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(HAT), brandB, 2000L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(SOCKS), brandB, 2000L));
        products.add(
                new Product(productIdGenerator.getAndIncrement(), categories.get(ACCESSORY), brandB, 2200L));

        // C 브랜드 상품 등록
        // 상품 총액 37,100
        Brand brandC = brands.get("C");
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(TOP), brandC, 10000L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(OUTER), brandC, 6200L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(BOTTOM), brandC, 3300L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(SNEAKERS), brandC, 9200L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(BAG), brandC, 2200L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(HAT), brandC, 1900L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(SOCKS), brandC, 2200L));
        products.add(
                new Product(productIdGenerator.getAndIncrement(), categories.get(ACCESSORY), brandC, 2100L));

        // D 브랜드 상품 등록
        // 상품 총액 36,100
        Brand brandD = brands.get("D");
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(TOP), brandD, 10100L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(OUTER), brandD, 5100L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(BOTTOM), brandD, 3000L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(SNEAKERS), brandD, 9500L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(BAG), brandD, 2500L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(HAT), brandD, 1500L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(SOCKS), brandD, 2400L));
        products.add(
                new Product(productIdGenerator.getAndIncrement(), categories.get(ACCESSORY), brandD, 2000L));

        // E 브랜드 상품 등록
        // 상품 총액 37,700
        Brand brandE = brands.get("E");
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(TOP), brandE, 10700L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(OUTER), brandE, 5000L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(BOTTOM), brandE, 3800L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(SNEAKERS), brandE, 9900L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(BAG), brandE, 2300L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(HAT), brandE, 1800L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(SOCKS), brandE, 2100L));
        products.add(
                new Product(productIdGenerator.getAndIncrement(), categories.get(ACCESSORY), brandE, 2100L));

        // F 브랜드 상품 등록
        // 상품 총액 37,300
        Brand brandF = brands.get("F");
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(TOP), brandF, 11200L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(OUTER), brandF, 7200L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(BOTTOM), brandF, 4000L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(SNEAKERS), brandF, 9300L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(BAG), brandF, 2100L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(HAT), brandF, 1600L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(SOCKS), brandF, 2300L));
        products.add(
                new Product(productIdGenerator.getAndIncrement(), categories.get(ACCESSORY), brandF, 1900L));

        // G 브랜드 상품 등록
        // 상품 총액 37,200
        Brand brandG = brands.get("G");
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(TOP), brandG, 10500L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(OUTER), brandG, 5800L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(BOTTOM), brandG, 3900L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(SNEAKERS), brandG, 9000L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(BAG), brandG, 2200L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(HAT), brandG, 1700L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(SOCKS), brandG, 2100L));
        products.add(
                new Product(productIdGenerator.getAndIncrement(), categories.get(ACCESSORY), brandG, 2000L));

        // H 브랜드 상품 등록
        // 상품 총액 37,600
        Brand brandH = brands.get("H");
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(TOP), brandH, 10800L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(OUTER), brandH, 6300L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(BOTTOM), brandH, 3100L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(SNEAKERS), brandH, 9700L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(BAG), brandH, 2100L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(HAT), brandH, 1600L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(SOCKS), brandH, 2000L));
        products.add(
                new Product(productIdGenerator.getAndIncrement(), categories.get(ACCESSORY), brandH, 2000L));

        // I 브랜드 상품 등록
        // 상품 총액 39,000
        Brand brandI = brands.get("I");
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(TOP), brandI, 11400L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(OUTER), brandI, 6700L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(BOTTOM), brandI, 3200L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(SNEAKERS), brandI, 9500L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(BAG), brandI, 2400L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(HAT), brandI, 1700L));
        products.add(new Product(productIdGenerator.getAndIncrement(), categories.get(SOCKS), brandI, 1700L));
        products.add(
                new Product(productIdGenerator.getAndIncrement(), categories.get(ACCESSORY), brandI, 2400L));
    }

    public void addTestData(Product product) {
        products.add(
                new Product(
                        productIdGenerator.getAndIncrement(),
                        categories.get(product.getCategory().getCategoryType()),
                        brands.get(product.getBrand().getBrandName()),
                        product.getPrice()));
    }


    @Override
    public Product findTopByBrandAndCategoryOrderByPriceAsc(Brand brand, Category category) {
        Category.CategoryType categoryType = category.getCategoryType();
        String brandName = brand.getBrandName();
        return products.stream()
                .filter(
                        product ->
                                categoryType.equals(product.getCategory().getCategoryType())
                                        && brandName.equals(product.getBrand().getBrandName()))
                .min(Comparator.comparingLong(Product::getPrice))
                .orElse(null);
    }

    @Override
    public Product findTopByCategoryOrderByPriceAsc(Category category) {
        Category.CategoryType categoryType = category.getCategoryType();
        return products.stream()
                .filter(product -> categoryType.equals(product.getCategory().getCategoryType()))
                .min(Comparator.comparingLong(Product::getPrice))
                .orElse(null);
    }

    @Override
    public Product findTopByCategoryOrderByPriceDesc(Category category) {
        Category.CategoryType categoryType = category.getCategoryType();
        return products.stream()
                .filter(product -> categoryType.equals(product.getCategory().getCategoryType()))
                .max(Comparator.comparingLong(Product::getPrice))
                .orElse(null);
    }

    @Override
    public void deleteAll() {
        products.clear();
    }

    @Override
    public Optional<Product> findById(Long aLong) {
        return products.stream().filter(p -> p.getId().equals(aLong)).findFirst();
    }

    @Override
    public void deleteById(Long aLong) {
        products.removeIf(product -> product.getId().equals(aLong));
    }


    /**
     * 이하 메서드는 미구현
     */
    @Override
    public void flush() {
    }

    @Override
    public <S extends Product> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Product> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Product> entities) {
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {
    }

    @Override
    public Product getOne(Long aLong) {
        return null;
    }

    @Override
    public Product getById(Long aLong) {
        return null;
    }

    @Override
    public Product getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Product> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Product> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Product> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Product> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Product> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Product> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Product, R> R findBy(
            Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Product> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Product> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }


    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<Product> findAll() {
        return List.of();
    }

    @Override
    public List<Product> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }


    @Override
    public void delete(Product entity) {
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
    }

    @Override
    public void deleteAll(Iterable<? extends Product> entities) {
    }

    @Override
    public void deleteAllInBatch() {
    }

    @Override
    public List<Product> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return null;
    }
}
