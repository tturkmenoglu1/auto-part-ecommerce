package com.ape.dto;

import com.ape.model.*;
import com.ape.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String phone;

    private LocalDate birthDate;

    private String email;

    private UserStatus status;

    private String password;

    private Boolean builtIn;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private Set<Role> roles;

    private List<Transaction> transactions;

    private List<Order> orders;

    private Set<UserAddress> addresses;

    private Set<ConfirmationToken> confirmationTokens;

    private Basket basket;
}
