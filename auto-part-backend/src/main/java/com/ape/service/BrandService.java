package com.ape.service;

import com.ape.dto.BrandDTO;
import com.ape.dto.request.BrandRequest;
import com.ape.exception.ConflictException;
import com.ape.mapper.BrandMapper;
import com.ape.model.Brand;
import com.ape.model.ImageFile;
import com.ape.model.enums.BrandStatus;
import com.ape.repository.BrandRepository;
import com.ape.utility.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    private final BrandMapper brandMapper;

    private final ImageFileService imageFileService;

    public List<BrandDTO> getAllBrandList() {
        List<Brand> brands = brandRepository.findAll();
        return brandMapper.brandListToBrandListDTO(brands);
    }


    public void createBrand(String imageId, BrandRequest brandRequest) {
        ImageFile imageFile = imageFileService.getImageById(imageId);
        Integer usedBrandCount = brandRepository.findBrandsByImageId(imageId);
        if (usedBrandCount>0){
            throw new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);
        }
        boolean foundBrand = brandRepository.existsByName(brandRequest.getName());
        if (foundBrand){
            throw new ConflictException(String.format(ErrorMessage.BRAND_CONFLICT_EXCEPTION,brandRequest.getName()));
        }
        Brand brand=brandMapper.brandRequestToBrand(brandRequest);
        brand.setStatus(BrandStatus.NOT_PUBLISHED);
        brand.setName(brandRequest.getName());
        brand.setImage(imageFile);
        brandRepository.save(brand);
    }
}
