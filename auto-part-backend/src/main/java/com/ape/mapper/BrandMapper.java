package com.ape.mapper;

import com.ape.dto.BrandDTO;
import com.ape.dto.request.BrandRequest;
import com.ape.model.Brand;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    List<BrandDTO> brandListToBrandListDTO(List<Brand> brands);

    Brand brandRequestToBrand(BrandRequest brandRequest);

    BrandDTO brandToBrandDTO(Brand brand);
}
