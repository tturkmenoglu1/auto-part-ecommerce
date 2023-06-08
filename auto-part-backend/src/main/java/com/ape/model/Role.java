package com.ape.model;

import com.ape.model.enums.RoleType;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Admin, Manager, Customer

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleType roleName;
}
