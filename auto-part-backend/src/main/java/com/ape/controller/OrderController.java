package com.ape.controller;

import com.ape.dto.OrderDTO;
import com.ape.dto.request.OrderRequest;
import com.ape.service.OrderService;
import com.ape.utility.APEResponse;
import com.ape.utility.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<APEResponse> createOrder(@RequestHeader("basketUUID") String basketUUID, @Valid @RequestBody OrderRequest orderRequest){
        OrderDTO orderDTO=  orderService.createOrder(basketUUID,orderRequest);
        APEResponse response=new APEResponse(ResponseMessage.ORDER_CREATE_RESPONSE,
                true,orderDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
