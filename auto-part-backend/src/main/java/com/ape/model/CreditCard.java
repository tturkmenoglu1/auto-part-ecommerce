package com.ape.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "t_credit_card")
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nameOnCard;

    @Column
    private String cardNumber;

    @Column
    private LocalDate expireDate;

    @Column
    private String securityCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
