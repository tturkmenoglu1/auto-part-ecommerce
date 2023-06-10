package com.ape.service;

import com.ape.dto.BasketDTO;
import com.ape.dto.BasketItemDTO;
import com.ape.dto.request.BasketItemRequest;
import com.ape.dto.request.BasketUpdateRequest;
import com.ape.exception.BadRequestException;
import com.ape.exception.ResourceNotFoundException;
import com.ape.mapper.BasketItemMapper;
import com.ape.mapper.BasketMapper;
import com.ape.model.Basket;
import com.ape.model.BasketItem;
import com.ape.model.Product;
import com.ape.repository.BasketItemRepository;
import com.ape.repository.BasketRepository;
import com.ape.utility.ErrorMessage;
import com.ape.utility.reuseableMethods.Calculators;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;

    private final BasketMapper basketMapper;

    private final BasketItemMapper basketItemMapper;

    private final ProductService productService;

    private final BasketItemRepository basketItemRepository;

    private final Calculators calculator;



    public BasketDTO getBasket(String basketUUID) {
        Basket basket;
        if (!basketUUID.isEmpty()){
            basket = findBasketByUUID(basketUUID);
        }else{
            basket = new Basket();
            basket.setBasketUUID(UUID.randomUUID().toString());
            basketRepository.save(basket);
        }
        return basketMapper.basketToBasketDTO(basket);
    }

    public Basket findBasketByUUID(String basketUUID) {
        return basketRepository.findByBasketUUID(basketUUID).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,basketUUID)));
    }

    public BasketItemDTO createBasketItem(String basketUUID, BasketItemRequest basketItemRequest) {
        Basket basket = findBasketByUUID(basketUUID);

        Double totalPrice;

        Product product = productService.findProductById(basketItemRequest.getProductId());
        BasketItem foundItem = basketItemRepository.findByProductIdAndBasketBasketUUID(product.getId(),basket.getBasketUUID());
        BasketItem basketItem = null;
        if (basketItemRequest.getQuantity()>product.getStockAmount()){
            throw new BadRequestException(String.format(ErrorMessage.PRODUCT_OUT_OF_STOCK_MESSAGE,product.getId()));
        }
        if (basket.getBasketItem().size()>0 && basket.getBasketItem().contains(foundItem)){
            if (basketItemRequest.getQuantity()>foundItem.getProduct().getStockAmount()){
                throw new BadRequestException(String.format(ErrorMessage.PRODUCT_OUT_OF_STOCK_MESSAGE,product.getId()));
            }
            Integer quantity = foundItem.getQuantity()+basketItemRequest.getQuantity();
            foundItem.setQuantity(quantity);
            totalPrice = quantity* product.getPrice();
            foundItem.setTotalPrice(totalPrice);
            basketItemRepository.save(foundItem);
            basket.setGrandTotal(basket.getGrandTotal()+(basketItemRequest.getQuantity()+foundItem.getProduct().getPrice()));
            basketItem = foundItem;
            basketItem.setUpdateAt(LocalDateTime.now());
        }else {
            basketItem = new BasketItem();
            basketItem.setProduct(product);
            basketItem.setQuantity(basketItemRequest.getQuantity());
            basketItem.setBasket(basket);
            totalPrice = basketItemRequest.getQuantity()*product.getPrice();
            basketItem.setTotalPrice(totalPrice);
            basketItemRepository.save(basketItem);
            basket.getBasketItem().add(basketItem);
            basket.setGrandTotal(basket.getGrandTotal()+totalPrice);

        }

        basketRepository.save(basket);
        return basketItemMapper.basketItemToBasketItemDTO(basketItem);
    }




    public BasketItemDTO changeQuantity(String basketUUID, BasketUpdateRequest basketUpdateRequest, String operator) {
        Basket basket = findBasketByUUID(basketUUID);
        Product product = productService.findProductById(basketUpdateRequest.getProductId());
        BasketItem foundItem = basketItemRepository.findByProductIdAndBasketBasketUUID(product.getId(), basketUUID);
        switch (operator){
            case "increase":
                foundItem.setQuantity(foundItem.getQuantity()+1);
                basket.setGrandTotal(basket.getGrandTotal()+foundItem.getProduct().getPrice());
                break;
            case "decrease":
                foundItem.setQuantity(foundItem.getQuantity()-1);
                basket.setGrandTotal(basket.getGrandTotal()-foundItem.getProduct().getPrice());
                break;
        }
        Double totalPrice = calculator.totalPriceWithDiscountCalculate(foundItem.getQuantity(), product.getPrice(), product.getDiscount());
        foundItem.setTotalPrice(totalPrice);
        foundItem.setUpdateAt(LocalDateTime.now());
        basketItemRepository.save(foundItem);
        basketRepository.save(basket);

        return basketItemMapper.basketItemToBasketItemDTO(foundItem);
    }

    public BasketItemDTO removeItemById(String basketUUID, Long productId) {
        Basket shoppingCart = findBasketByUUID(basketUUID);
        BasketItem foundItem = basketItemRepository.findByProductIdAndBasketBasketUUID(productId,
                basketUUID);
        shoppingCart.setGrandTotal(shoppingCart.getGrandTotal()-foundItem.getTotalPrice());
        basketItemRepository.delete(foundItem);
        basketRepository.save(shoppingCart);
        return basketItemMapper.basketItemToBasketItemDTO(foundItem);
    }

    public void cleanBasket(String basketUUID){
        Basket shoppingCart = findBasketByUUID(basketUUID);
        basketItemRepository.deleteAll(shoppingCart.getBasketItem());
        shoppingCart.setGrandTotal(0.0);
    }
}
