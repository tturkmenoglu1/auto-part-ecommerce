package com.ape.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class APEResponse {

    private String message;

    private Boolean success;

    private Object data;


    public APEResponse(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }
}
