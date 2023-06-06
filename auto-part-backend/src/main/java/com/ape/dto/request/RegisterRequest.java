package com.ape.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Size(max=50)
    @NotBlank(message="Please provide yout First Name")
    private String firstName;

    @Size(max=50)
    @NotBlank(message="Please provide yout Last Name")
    private String lastName;

    @Size(min=5, max=80)
    @Email(message="Please provide valid e-mail")
    private String email;

    @Size(min=4, max=20, message="Please provide Correct Size of Password")
    @NotBlank(message="Please provide your password")
    private String password;

    @NotBlank
    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
            message = "Please provide valid phone number")
    private String phone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate birthDate;


}