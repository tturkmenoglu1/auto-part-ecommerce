package com.ape.controller;

import com.ape.dto.BrandDTO;
import com.ape.dto.request.BrandRequest;
import com.ape.dto.request.BrandUpdateRequest;
import com.ape.service.BrandService;
import com.ape.utility.APEResponse;
import com.ape.utility.ResponseMessage;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/brand")
public class BrandController {

    private final BrandService brandService;

    @GetMapping("/option")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<BrandDTO>> getAllBrandsForOption(){
        return ResponseEntity.ok(brandService.getAllBrandList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDTO> getBrandById(@PathVariable("id") Long id){
        BrandDTO brandDTO=brandService.getBrandById(id);
        return ResponseEntity.ok(brandDTO);
    }

    @PostMapping("/{imageId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APEResponse> addBrandById(@PathVariable("imageId")String imageId, @Valid @RequestBody BrandRequest brandRequest){
        brandService.createBrand(imageId,brandRequest);
        APEResponse response = new APEResponse(ResponseMessage.BRAND_CREATE_RESPONSE_MESSAGE,true);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<APEResponse> updateBrand(@Valid @PathVariable("id") Long id,@RequestBody BrandUpdateRequest brandUpdateRequest){
        BrandDTO brandDTO=brandService.updateBrand(id,brandUpdateRequest);
        APEResponse gpmResponse=new APEResponse(ResponseMessage.BRAND_UPDATE_RESPONSE_MESSAGE,true,brandDTO);
        return  ResponseEntity.ok(gpmResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<APEResponse> deleteBrandById(@PathVariable("id") Long id){
        BrandDTO brandDTO=brandService.deleteBrandById(id);
        APEResponse gpmResponse=new APEResponse(ResponseMessage.BRAND_DELETE_RESPONSE_MESSAGE,true,brandDTO);
        return ResponseEntity.ok(gpmResponse);
    }

}
