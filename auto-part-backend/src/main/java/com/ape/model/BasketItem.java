package com.ape.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="t_basket_item")
public class BasketItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @Column
    private Double totalPrice;

    @Column
    private LocalDateTime createAt=LocalDateTime.now();

    @Column
    private LocalDateTime updateAt;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "basket_id")
    private Basket basket;

}
