package com.ape.controller;

import com.ape.dto.CategoryDTO;
import com.ape.dto.request.CategoryRequest;
import com.ape.dto.request.CategoryUpdateRequest;
import com.ape.service.CategoryService;
import com.ape.utility.APEResponse;
import com.ape.utility.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APEResponse> saveCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        categoryService.saveCategory(categoryRequest);
        APEResponse response = new APEResponse(ResponseMessage.CATEGORY_CREATED_RESPONSE_MESSAGE, true);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id){
        CategoryDTO categoryDTO= categoryService.findCategoryById(id);
        return ResponseEntity.ok(categoryDTO);
    }

    @GetMapping("/option")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<CategoryDTO>> getAllBrandsForOption(){
        return ResponseEntity.ok(categoryService.getAllCategoriesAsList());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APEResponse> updateCategory(@PathVariable Long id,
                                                      @Valid @RequestBody CategoryUpdateRequest categoryUpdateRequest){
        CategoryDTO categoryDTO=categoryService.updateCategory(id,categoryUpdateRequest);
        APEResponse response = new APEResponse(ResponseMessage.CATEGORY_UPDATED_RESPONSE_MESSAGE, true,categoryDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APEResponse> deleteCategory(@PathVariable Long id){
        CategoryDTO categoryDTO=categoryService.removeById(id);
        APEResponse response = new APEResponse(ResponseMessage.CATEGORY_DELETED_RESPONSE_MESSAGE, true,categoryDTO);
        return ResponseEntity.ok(response);
    }
}
