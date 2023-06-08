package com.ape.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BasketItemDTO {

    private Long id;

    private Long productId;

    private Long quantity;

    private String title;

    private String imageId;

    private Double unitPrice;

    private Double discountedPrice;

    private Double totalPrice;

    private Integer discount;
    private Double tax;
    private LocalDateTime createAt;

    private LocalDateTime updateAt;
}
