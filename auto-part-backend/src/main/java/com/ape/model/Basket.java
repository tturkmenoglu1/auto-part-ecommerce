package com.ape.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_basket")
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String basketUUID;
    @Column
    private Double grandTotal = 0.0;
    @Column
    private LocalDateTime createAt = LocalDateTime.now();

    @OneToMany(mappedBy = "basket",orphanRemoval = true)
    private List<BasketItem> basketItem = new ArrayList<>();

}
