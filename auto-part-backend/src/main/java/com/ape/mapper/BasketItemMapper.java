package com.ape.mapper;

import com.ape.dto.BasketItemDTO;
import com.ape.model.BasketItem;
import com.ape.model.ImageFile;
import com.ape.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BasketItemMapper {

    @Mapping(source = "product", target = "productId", qualifiedByName = "getProductId")
    @Mapping(source = "product", target = "title", qualifiedByName = "getProductTitle")
    @Mapping(source = "product", target = "imageId", qualifiedByName = "getProductImageId")
    @Mapping(source = "product", target = "unitPrice", qualifiedByName = "getProductPrice")
    @Mapping(source = "product", target = "discount", qualifiedByName = "getProductDiscount")
    @Mapping(source = "product", target = "discountedPrice", qualifiedByName = "getDiscountedPrice")
    @Mapping(source = "product", target = "tax",qualifiedByName = "getTaxRate")
    BasketItemDTO basketItemToBasketItemDTO(BasketItem basketItem);

    @Named("getProductId")
    public static Long getProductId(Product product){
        return product.getId();
    }

    @Named("getProductTitle")
    public static String getProductTitle(Product product){
        return product.getTitle();
    }

    @Named("getProductImageId")
    public static String getProductImageId(Product product){
        return product.getImage().stream().filter(ImageFile::isShowcase).map(ImageFile::getId).findFirst().orElse(null);
    }

    @Named("getProductPrice")
    public static Double getProductPrice(Product product){
        return product.getPrice();
    }

    @Named("getProductDiscount")
    public static Integer getProductDiscount(Product product){
        return product.getDiscount();
    }

    @Named("getDiscountedPrice")
    public static Double getDiscountedPrice(Product product){
        return product.getDiscountedPrice();
    }

    @Named("getTaxRate")
    public static Double getTaxRate(Product product){
        return product.getTax();
    }

}
