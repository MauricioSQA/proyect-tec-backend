package com.foodappbackend.foodapp.enums;

public enum RolEnum {
    USUARIO("USUARIO"),
    EMPRESA("EMPRESA");

    private final String rol;

    RolEnum(String rol) {
        this.rol = rol;
    }

    public String getRol() {
        return rol;
    }
}
