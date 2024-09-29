package org.example.demoproductservice.domain.repository;

import org.example.demoproductservice.domain.repository.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
}
