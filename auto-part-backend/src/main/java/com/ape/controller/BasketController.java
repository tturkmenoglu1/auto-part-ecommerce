package com.ape.controller;


import com.ape.dto.BasketDTO;
import com.ape.dto.BasketItemDTO;
import com.ape.dto.request.BasketItemRequest;
import com.ape.service.BasketService;
import com.ape.utility.APEResponse;
import com.ape.utility.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/basket")
public class BasketController {

    private final BasketService basketService;

    @GetMapping
    public ResponseEntity<BasketDTO> getBasket(@RequestHeader(value = "basketUUD", required = false) String basketUUID){
        BasketDTO basketDTO = basketService.getBasket(basketUUID);
        return ResponseEntity.ok(basketDTO);
    }

    @PostMapping
    public ResponseEntity<APEResponse> createBasketItem(@RequestHeader("basketUUID") String basketUUID, @RequestBody BasketItemRequest basketItemRequest){
        BasketItemDTO basketItemDTO = basketService.createBasket(basketUUID,basketItemRequest);
        APEResponse response = new APEResponse(ResponseMessage.ITEM_ADDED_RESPONSE_MESSAGE,true);
        return ResponseEntity.ok(response);
    }
}
