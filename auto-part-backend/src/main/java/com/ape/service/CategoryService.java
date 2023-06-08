package com.ape.service;

import com.ape.dto.CategoryDTO;
import com.ape.dto.request.CategoryRequest;
import com.ape.exception.ConflictException;
import com.ape.exception.ResourceNotFoundException;
import com.ape.mapper.CategoryMapper;
import com.ape.model.Category;
import com.ape.model.enums.CategoryStatus;
import com.ape.repository.CategoryRepository;
import com.ape.utility.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public void saveCategory(CategoryRequest categoryRequest) {
        Category category = new Category();

        boolean foundTitle = categoryRepository.existsByTitle(categoryRequest.getTitle());
        if (foundTitle){
            throw new ConflictException(String.format(ErrorMessage.CATEGORY_USED_EXCEPTION,categoryRequest.getTitle()));
        }
        category.setTitle(categoryRequest.getTitle());
        category.setStatus(CategoryStatus.NOT_PUBLISHED);
        categoryRepository.save(category);
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.CATEGORY_NOT_FOUND_MESSAGE));
    }
}
