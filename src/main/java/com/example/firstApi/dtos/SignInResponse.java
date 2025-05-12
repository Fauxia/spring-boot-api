package com.example.firstApi.dtos;

import com.example.firstApi.entities.ERole;

import java.util.List;

public class SignInResponse {
    String jwtToken;
    String email;
    String role;

    public SignInResponse(String jwtToken, String email, String role) {
        this.jwtToken = jwtToken;
        this.email = email;
        this.role = role;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public String getEmail() {
        return email;
    }

    public String getRoles() {
        return role;
    }
}
