package com.ape.service;

import com.ape.dto.CategoryDTO;
import com.ape.dto.request.CategoryRequest;
import com.ape.dto.request.CategoryUpdateRequest;
import com.ape.exception.BadRequestException;
import com.ape.exception.ConflictException;
import com.ape.exception.ResourceNotFoundException;
import com.ape.mapper.CategoryMapper;
import com.ape.model.Category;
import com.ape.model.enums.CategoryStatus;
import com.ape.repository.CategoryRepository;
import com.ape.repository.ProductRepository;
import com.ape.utility.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final ProductRepository productRepository;

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

    public CategoryDTO findCategoryById(Long id) {
        return categoryMapper.categoryToCategoryDTO(getCategoryById(id));
    }


    public List<CategoryDTO> getAllCategoriesAsList() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryMapper.categoryListToCategoryDTOList(categoryList);
    }

    public CategoryDTO removeById(Long id) {
        Category category = getCategoryById(id);
        Boolean existProduct = productRepository.existsByCategoryId(id);

        if (existProduct){
            throw new BadRequestException(ErrorMessage.CATEGORY_CAN_NOT_DELETE_EXCEPTION);
        }
        CategoryDTO categoryDTO = categoryMapper.categoryToCategoryDTO(category);
        categoryRepository.delete(category);
        return categoryDTO;
    }

    public CategoryDTO updateCategory(Long id, CategoryUpdateRequest categoryUpdateRequest) {
        Category category=getCategoryById(id);
        boolean existsTitle= categoryRepository.existsByTitle(categoryUpdateRequest.getTitle());

        if(existsTitle && ! categoryUpdateRequest.getTitle().equalsIgnoreCase(category.getTitle())) {
            throw new ConflictException(String.format(ErrorMessage.CATEGORY_USED_EXCEPTION,categoryUpdateRequest.getTitle()));
        }
        category.setTitle(categoryUpdateRequest.getTitle());
        category.setStatus(categoryUpdateRequest.getStatus());
        category.setUpdateAt(LocalDateTime.now());
        categoryRepository.save(category);
        return categoryMapper.categoryToCategoryDTO(category);
    }
}
