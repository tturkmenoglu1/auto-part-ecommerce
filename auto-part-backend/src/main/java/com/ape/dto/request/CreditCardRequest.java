package com.ape.dto.request;

import com.ape.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CreditCardRequest {

    @NotBlank
    private String nameOnCard;

    @Size(min = 12, max = 12, message = "Please provide the number on the card properly")
    private String cardNumber;

    @NotNull
    private LocalDate expireDate;

    @Size(min = 3,max = 3,message = "Security code cannot be null or more than 3 char")
    private String securityCode;

}
