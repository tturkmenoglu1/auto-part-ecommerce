package com.ape.mapper;

import com.ape.dto.BasketDTO;
import com.ape.model.Basket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BasketMapper {

    BasketDTO basketToBasketDTO(Basket basket);

}
