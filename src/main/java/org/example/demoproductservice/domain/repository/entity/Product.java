package org.example.demoproductservice.domain.repository.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "products",
        indexes = @Index(name = "idx_products_detail", columnList = "id, category_id, brand_id, price")
)
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "category_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    @JoinColumn(name = "brand_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Brand brand;

    @Column(nullable = false)
    private Long price;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private ZonedDateTime updatedAt;

    public Product(Long id, Category category, Brand brand, Long price) {
        this.id = id;
        this.category = category;
        this.brand = brand;
        this.price = price;
    }

    public Product() {
    }

    public Product(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public Brand getBrand() {
        return brand;
    }

    public Long getPrice() {
        return price;
    }
}
