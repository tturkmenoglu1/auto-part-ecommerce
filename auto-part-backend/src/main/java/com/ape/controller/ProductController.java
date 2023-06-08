package com.ape.controller;

import com.ape.dto.request.ProductRequest;
import com.ape.service.ProductService;
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
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APEResponse> saveProduct(@Valid @RequestBody ProductRequest productRequest){
        productService.saveProduct(productRequest);
        APEResponse response = new APEResponse(ResponseMessage.PRODUCT_SAVED_RESPONSE_MESSAGE,true);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
