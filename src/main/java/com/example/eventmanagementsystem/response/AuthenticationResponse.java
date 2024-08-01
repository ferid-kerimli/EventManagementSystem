package com.example.eventmanagementsystem.response;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private String message;
    private String token;
    private long expiresIn;
}
