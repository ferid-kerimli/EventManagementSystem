package com.example.eventmanagementsystem.controller;

import com.example.eventmanagementsystem.dto.accountDto.UserLoginDto;
import com.example.eventmanagementsystem.dto.accountDto.UserRegisterDto;
import com.example.eventmanagementsystem.response.AuthenticationResponse;
import com.example.eventmanagementsystem.response.SignUpResponse;
import com.example.eventmanagementsystem.service.abstraction.IAuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final IAuthenticationService authenticationService;

    public AuthenticationController(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Async
    @PostMapping("/signup")
    public CompletableFuture<ResponseEntity<SignUpResponse>> signUp(@RequestBody UserRegisterDto userRegisterDto) {
        return authenticationService.signUp(userRegisterDto)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> {
                    SignUpResponse errorResponse = new SignUpResponse();
                    errorResponse.setMessage("Sign up failed: " + ex.getMessage());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                });
    }

    @Async
    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<AuthenticationResponse>> authenticate(@RequestBody UserLoginDto userLoginDto) {
        return authenticationService.login(userLoginDto)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> {
                    AuthenticationResponse errorResponse = new AuthenticationResponse();
                    errorResponse.setMessage("Login failed: " + ex.getMessage());
                   return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                });

    }
}