package com.ape.dto;

import com.ape.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardDTO {

    private Long id;

    private String nameOnCard;

    private String cardNumber;

    private LocalDate expireDate;

    private String securityCode;

    private User user;
}
