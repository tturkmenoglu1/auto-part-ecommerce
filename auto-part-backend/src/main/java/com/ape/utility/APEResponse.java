package com.ape.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class APEResponse {

    private String message;

    private Boolean success;


    public APEResponse(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }
}
