package com.ape.dto;

import com.ape.model.*;
import com.ape.model.enums.ProductStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class ProductDTO {

    private Long id;


    private String title;


    private String shortDesc;


    private String longDesc;

    @Column
    private Double price;

    @Column
    private Double discountedPrice;

    @Column
    private Double tax;

    @Column
    private Integer discount;

    @Column
    private Integer stockAmount;

    private ProductStatus status;

    @Column
    private Double width;

    @Column
    private Double length;

    @Column
    private Double height;

    @Column
    private LocalDateTime createAt;

    @Column
    private LocalDateTime updateAt;

    private Brand brand;

    private Category category;

    private Set<BasketItem> basketItem;

    private Set<OrderItem> orderItems;

    private Set<ImageFile> image;
}
