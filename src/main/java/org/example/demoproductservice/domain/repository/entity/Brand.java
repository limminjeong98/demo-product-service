package org.example.demoproductservice.domain.repository.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "brands",
        indexes = @Index(name = "idx_brands_name", columnList = "id, brand_name")
)
@Entity
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "brand_name", length = 100)
    private String brandName;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private ZonedDateTime updatedAt;

    public Brand() {
    }

    public Brand(Long id) {
        this.id = id;
    }

    public Brand(Long id, String brandName) {
        this.id = id;
        this.brandName = brandName;
    }

    public Brand(Long id, String brandName, List<Product> products) {
        this.id = id;
        this.brandName = brandName;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public String getBrandName() {
        return brandName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }
}
