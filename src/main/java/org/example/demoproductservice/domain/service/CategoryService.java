package org.example.demoproductservice.domain.service;

import org.example.demoproductservice.common.exception.CategoryNotFoundException;
import org.example.demoproductservice.domain.repository.CategoryRepository;
import org.example.demoproductservice.domain.repository.entity.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Category findByCategoryType(String categoryType) {
        Category.CategoryType type;
        try {
            type = Category.CategoryType.valueOf(categoryType);
        } catch (IllegalArgumentException e) {
            // CategoryType enum에 정의되지 않은 카테고리를 조회한 경우
            throw new CategoryNotFoundException();
        }
        return categoryRepository.findByCategoryType(type);
    }

    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
    }
}
