package com.ape.dto;

import com.ape.model.BasketItem;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasketDTO {

    private Long id;

    private String basketUUID;

    private Double grandTotal = 0.0;

    private LocalDateTime createAt;

    private List<BasketItem> basketItem;
}
