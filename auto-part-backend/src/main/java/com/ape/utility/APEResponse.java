package com.ape.utility;

public class APEResponse {

    private String message;

    private Boolean success;

    public APEResponse(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }
}
