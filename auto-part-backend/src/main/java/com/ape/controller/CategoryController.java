package com.ape.controller;

import com.ape.dto.CategoryDTO;
import com.ape.dto.request.CategoryRequest;
import com.ape.service.CategoryService;
import com.ape.utility.APEResponse;
import com.ape.utility.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
}
