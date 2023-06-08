package com.ape.dto;

import com.ape.model.ImageFile;
import com.ape.model.Product;
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

public class BrandDTO {

    private Long id;

    private String name;

    private BrandStatus status;

    private Boolean builtIn = false;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private List<Product> product;

    private ImageFile image;
}
