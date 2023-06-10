package com.ape.dto;

import com.ape.model.*;
import com.ape.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OrderDTO {
    private Long id;

    private String code;

    private OrderStatus status;

    private Double subTotal;

    private Double discount;

    private Double tax;

    private String contactName;

    private String contactPhone;

    private Double shippingCost;

    private String shippingDetails;

    private Double grandTotal;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private List<OrderItemDTO> orderItemsDTO;

    private String customer;

    private UserAddressDTO addressDTO;
}
