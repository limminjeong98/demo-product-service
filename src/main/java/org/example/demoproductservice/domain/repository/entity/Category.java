package org.example.demoproductservice.domain.repository.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "categories",
        indexes = @Index(name = "idx_categories_type", columnList = "id, category_type")
)
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

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private ZonedDateTime updatedAt;

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

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
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
