package com.ape.mapper;

import com.ape.dto.OrderDTO;
import com.ape.dto.request.OrderRequest;
import com.ape.model.Order;
import com.ape.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, UserAddressMapper.class})
public interface OrderMapper {

    @Mapping(source = "orderItem",target = "orderItemsDTO")
    @Mapping(source = "user",target = "customer",qualifiedByName = "getUserFullName")
    @Mapping(source = "address",target = "addressDTO")
    OrderDTO orderToOrderDTO (Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "status", ignore = true)
    Order orderRequestToOrder (OrderRequest orderRequest);

    List<OrderDTO> map (List<Order> orderList);

    @Named("getUserFullName")
    public static String getUserId(User user){
        return user.getFirstName() + " " +user.getLastName();
    }

}