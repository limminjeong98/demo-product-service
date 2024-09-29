package org.example.demoproductservice.domain.service;

import org.example.demoproductservice.domain.repository.BrandRepository;
import org.example.demoproductservice.domain.repository.entity.Brand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BrandService {
    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Transactional(readOnly = true)
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }
}
