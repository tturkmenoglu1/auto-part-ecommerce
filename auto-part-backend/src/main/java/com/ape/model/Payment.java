package com.ape.model;

import com.ape.model.enums.PaymentStatus;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "t_payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private double amount;

    @Column
    private String provider;

    @Enumerated(EnumType.STRING)
    @Column
    private PaymentStatus status;

    @Column(nullable = false)
    private LocalDateTime createAt=LocalDateTime.now();

    @Column
    private LocalDateTime updateAt;
}
