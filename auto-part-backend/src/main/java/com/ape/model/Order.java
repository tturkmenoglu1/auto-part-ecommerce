package com.ape.model;

import com.ape.model.enums.OrderStatus;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column
    private OrderStatus status;

    @Column
    private Double subTotal;

    @Column
    private Double discount;

    @Column
    private Double tax;

    @Column
    private String contactName;

    @Column
    private String contactPhone;

    @Column
    private Double shippingCost;

    @Column
    private String shippingDetails;

    @Column
    private Double grandTotal;

    @Column
    private LocalDateTime createAt = LocalDateTime.now();

    @Column
    private LocalDateTime updateAt;

    @OneToMany(orphanRemoval = true,fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItem = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<Transaction> transaction = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<Payment> payments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private UserAddress address;

}
