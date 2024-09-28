package org.example.demoproductservice.domain.repository.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// FIXME: INDEX 추가
@Table(name = "categories")
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, name = "category_type")
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    public Category() {
    }

    public Category(Long id) {
        this.id = id;
    }

    public Category(Long id, CategoryType categoryType, ArrayList<Product> products) {
        this.id = id;
        this.categoryType = categoryType;
        this.products = products;
    }

    public static List<CategoryType> getAllCategoryTypes() {
        return Arrays.stream(CategoryType.values()).toList();
    }

    public Long getId() {
        return id;
    }

    public CategoryType getCategoryType() {
        return this.categoryType;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public enum CategoryType {
        TOP("상의"),
        OUTER("아우터"),
        BOTTOM("하의"),
        SNEAKERS("스니커즈"),
        BAG("가방"),
        HAT("모자"),
        SOCKS("양말"),
        ACCESSORY("액세서리");

        CategoryType(String categoryName) {
        }
    }
}
