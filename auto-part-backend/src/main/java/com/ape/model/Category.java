package com.ape.model;

import com.ape.model.enums.CategoryStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name="t_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 80, nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column
    private CategoryStatus status;

    @Column
    private LocalDateTime createAt=LocalDateTime.now();

    @Column
    private LocalDateTime updateAt;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Product> product = new ArrayList<>();



}

