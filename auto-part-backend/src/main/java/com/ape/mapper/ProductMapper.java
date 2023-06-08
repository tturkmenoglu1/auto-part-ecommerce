package com.ape.mapper;

import com.ape.dto.ProductDTO;
import com.ape.dto.request.ProductRequest;
import com.ape.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target="brand", ignore=true)
    @Mapping(target="category", ignore=true)
    @Mapping(target = "discountedPrice",ignore = true)
    Product productRequestToProduct(ProductRequest productRequest);

    ProductDTO productToProductDTO(Product product);
}
