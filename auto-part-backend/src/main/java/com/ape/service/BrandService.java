package com.ape.service;

import com.ape.dto.BrandDTO;
import com.ape.dto.request.BrandRequest;
import com.ape.dto.request.BrandUpdateRequest;
import com.ape.exception.BadRequestException;
import com.ape.exception.ConflictException;
import com.ape.exception.ResourceNotFoundException;
import com.ape.mapper.BrandMapper;
import com.ape.model.Brand;
import com.ape.model.ImageFile;
import com.ape.model.enums.BrandStatus;
import com.ape.repository.BrandRepository;
import com.ape.repository.ImageFileRepository;
import com.ape.repository.ProductRepository;
import com.ape.utility.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    private final BrandMapper brandMapper;

    private final ImageFileService imageFileService;

    private final ProductRepository productRepository;

    private final ImageFileRepository imageFileRepository;

    public List<BrandDTO> getAllBrandList() {
        List<Brand> brands = brandRepository.findAll();
        return brandMapper.brandListToBrandListDTO(brands);
    }

    public Brand findBrandById(Long id){
        return brandRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.BRAND_NOT_FOUND_MESSAGE,id)));
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

    public BrandDTO deleteBrandById(Long id) {
        Brand brand=findBrandById(id);
        Boolean existsProduct=productRepository.existsByBrandId(id);

        if(existsProduct){
            throw  new BadRequestException(ErrorMessage.BRAND_CAN_NOT_DELETE_EXCEPTION);
        }

        brandRepository.delete(brand);
        return brandMapper.brandToBrandDTO(brand);
    }

    public BrandDTO getBrandById(Long id) {
        return brandMapper.brandToBrandDTO(findBrandById(id));
    }

    public BrandDTO updateBrand(Long id, BrandUpdateRequest brandUpdateRequest) {
        Brand brand=findBrandById(id);
        if (!brand.getImage().getId().equals(brandUpdateRequest.getImage())){
            ImageFile tempImageFile = brand.getImage();
            ImageFile imageFile = imageFileService.getImageById(brandUpdateRequest.getImage());
            brand.setImage(imageFile);
            imageFileRepository.delete(tempImageFile);
        }

        boolean brandNameExist  = brandRepository.existsByName(brandUpdateRequest.getName());

        if(brandNameExist && ! brandUpdateRequest.getName().equalsIgnoreCase(brand.getName())) {
            throw new ConflictException(String.format(ErrorMessage.BRAND_CONFLICT_EXCEPTION,brandUpdateRequest.getName()));}

        boolean imageExists=brandRepository.existsByImageId(brandUpdateRequest.getImage());


        if(imageExists && ! brandUpdateRequest.getImage().equalsIgnoreCase(brand.getImage().getId())) {
            throw new ConflictException(String.format(ErrorMessage.IMAGE_ALREADY_EXIST_MESSAGE,brandUpdateRequest.getImage()));}


        brand.setName(brandUpdateRequest.getName());
        brand.setStatus(brandUpdateRequest.getStatus());
        brand.setUpdateAt(LocalDateTime.now());

        BrandDTO brandDTO=brandMapper.brandToBrandDTO(brand);
        brandRepository.save(brand);

        return brandDTO;
    }
}
