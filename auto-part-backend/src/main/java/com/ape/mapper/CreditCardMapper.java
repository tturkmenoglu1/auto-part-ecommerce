package com.ape.mapper;

import com.ape.dto.CreditCardDTO;
import com.ape.model.CreditCard;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CreditCardMapper {

    List<CreditCardDTO> creditCardListToCreditCardDTOList(List<CreditCard> creditCards);
}
