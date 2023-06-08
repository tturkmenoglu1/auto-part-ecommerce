package com.ape.dto;

import com.ape.model.Product;
import com.ape.model.enums.CategoryStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CategoryDTO {

    private Long id;

    private String title;

    private CategoryStatus status;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

}
