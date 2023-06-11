package com.ape.controller;

import com.ape.dto.ProductDTO;
import com.ape.dto.request.ProductRequest;
import com.ape.dto.request.ProductUpdateRequest;
import com.ape.service.ProductService;
import com.ape.utility.APEResponse;
import com.ape.utility.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}/admin")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<APEResponse> updateProduct(@PathVariable Long id,
                                                     @Valid @RequestBody ProductUpdateRequest productUpdateRequest){
        ProductDTO productDTO = productService.updateProduct(id, productUpdateRequest);
        APEResponse response = new APEResponse(ResponseMessage.PRODUCT_UPDATED_RESPONSE_MESSAGE,true,productDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductWithId(@PathVariable Long id){
        ProductDTO productDTO = productService.getProductDTOById(id);
        return ResponseEntity.ok(productDTO);
    }

    @DeleteMapping("/{id}/admin")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<APEResponse> deleteProduct(@PathVariable Long id){
        productService.removeById(id);
        APEResponse response = new APEResponse(ResponseMessage.PRODUCT_DELETE_RESPONSE_MESSAGE,true);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/image/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APEResponse>deleteProductImage(@PathVariable String id){
        productService.removeImageById(id);
        APEResponse response =new APEResponse(ResponseMessage.IMAGE_DELETE_RESPONSE_MESSAGE,true);
        return ResponseEntity.ok(response);
    }

}
