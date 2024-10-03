package org.example.demoproductservice.domain.service;

import org.example.demoproductservice.common.exception.BrandNotFoundException;
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

    @Transactional(readOnly = true)
    public Brand findById(Long id) {
        return brandRepository.findById(id).orElseThrow(BrandNotFoundException::new);
    }

    public Brand register(String brandName) {
        return brandRepository.save(new Brand(null, brandName));
    }

    public Brand update(Long id, String brandName) {
        Brand brand = brandRepository.findById(id).orElseThrow(BrandNotFoundException::new);
        return brandRepository.save(new Brand(id, brandName, brand.getProducts()));
    }

    public void delete(Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(BrandNotFoundException::new);
        brandRepository.deleteById(id);
    }
}
