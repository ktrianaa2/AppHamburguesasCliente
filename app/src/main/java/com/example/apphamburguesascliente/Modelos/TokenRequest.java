package com.example.apphamburguesascliente.Modelos;

public class TokenRequest {
    private String token;

    public TokenRequest(String token) {
        this.token = token;
    }

    // Getters y setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}