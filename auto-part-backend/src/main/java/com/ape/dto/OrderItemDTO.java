package com.ape.dto;

import com.ape.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private Long productId;

    private String imageId;

    private String title;

    private Integer quantity;

    private Double unitPrice;

    private double tax;

    private double discount;

    private Double subTotal;
}
