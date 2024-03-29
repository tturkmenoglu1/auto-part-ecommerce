package com.ape.model;

import com.ape.model.enums.BrandStatus;
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
@Table(name = "t_brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 70, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column
    private BrandStatus status;

    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    @Column
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "brand", orphanRemoval = true)
    private List<Product> product = new ArrayList<>();

    @OneToOne(orphanRemoval = true)
    private ImageFile image;

}
