package org.example.demoproductservice.domain.repository.entity;

import jakarta.persistence.*;

// FIXME: INDEX 추가
@Table(name = "products")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    @JoinColumn(name = "brand_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Brand brand;

    @Column(nullable = false)
    private Long price;

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
