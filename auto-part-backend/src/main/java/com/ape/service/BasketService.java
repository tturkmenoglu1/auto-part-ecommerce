package com.ape.service;

import com.ape.dto.BasketDTO;
import com.ape.dto.BasketItemDTO;
import com.ape.dto.request.BasketItemRequest;
import com.ape.exception.ResourceNotFoundException;
import com.ape.mapper.BasketMapper;
import com.ape.model.Basket;
import com.ape.model.Product;
import com.ape.repository.BasketRepository;
import com.ape.utility.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;

    private final BasketMapper basketMapper;

    public BasketDTO getBasket(String basketUUID) {
        Basket basket;
        if (!basketUUID.isEmpty()){
            basket = findShoppingCartByUUID(basketUUID);
        }else{
            basket = new Basket();
            basket.setBasketUUID(UUID.randomUUID().toString());
            basketRepository.save(basket);
        }
        return basketMapper.basketToBasketDTO(basket);
    }

    private Basket findShoppingCartByUUID(String basketUUID) {
        return basketRepository.findByBasketUUID(basketUUID).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,basketUUID)));
    }

    public BasketItemDTO createBasket(String basketUUID, BasketItemRequest basketItemRequest) {
        Basket basket = findShoppingCartByUUID(basketUUID);

        Double totalPrice;
        return null; //TODO
    }
}
