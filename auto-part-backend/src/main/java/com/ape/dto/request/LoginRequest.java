package com.ape.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class LoginRequest {

    @Email(message = "Please provide a valid email")
    private String email;

    @NotBlank(message = "Please provide a password")
    private String password;
}
