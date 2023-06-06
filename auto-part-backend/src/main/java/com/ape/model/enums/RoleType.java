package com.ape.model.enums;

public enum RoleType {
    ROLE_USER("User"),
    ROLE_ADMIN("Administrator");

    private String name;

    //cons'u dışarı açmamak için private yapıyoruz
    private RoleType(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
