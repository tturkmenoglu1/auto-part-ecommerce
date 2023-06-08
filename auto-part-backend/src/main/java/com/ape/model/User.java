package com.ape.model;

import com.ape.model.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30 , nullable = false)
    private String firstName;

    @Column(length = 30 , nullable = false)
    private String lastName;

    @Column(length = 15 , nullable = false)
    private String phone;

    @Column
    private LocalDate birthDate;

    @Column(length = 80, nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column
    private UserStatus status;

    @Column(length = 120, nullable = false)
    private String password;

    @Column
    private Boolean locked = false;

    @Column
    private LocalDateTime createAt = LocalDateTime.now();

    @Column
    private LocalDateTime updateAt;


    @ManyToMany
    @JoinTable(name = "t_user_roles",
                        joinColumns = @JoinColumn(name = "user_id"),
                        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user",orphanRemoval = true)
    private Set<UserAddress> addresses = new HashSet<>();

    @OneToMany(mappedBy = "user",orphanRemoval = true)
    private Set<ConfirmationToken> confirmationTokens = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Basket basket;
}
