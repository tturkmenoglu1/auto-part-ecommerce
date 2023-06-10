package com.ape.service;

import com.ape.model.OrderItem;
import com.ape.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public void save(OrderItem orderItem){
        orderItemRepository.save(orderItem);
    }
}
