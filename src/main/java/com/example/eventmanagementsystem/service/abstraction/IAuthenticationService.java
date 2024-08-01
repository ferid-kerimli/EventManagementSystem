package com.example.eventmanagementsystem.service.abstraction;

import com.example.eventmanagementsystem.dto.accountDto.UserLoginDto;
import com.example.eventmanagementsystem.dto.accountDto.UserRegisterDto;
import com.example.eventmanagementsystem.response.AuthenticationResponse;
import com.example.eventmanagementsystem.response.SignOutResponse;
import com.example.eventmanagementsystem.response.SignUpResponse;

import java.util.concurrent.CompletableFuture;

public interface IAuthenticationService {
    CompletableFuture<SignUpResponse> signUp(UserRegisterDto input);
    CompletableFuture<AuthenticationResponse> login(UserLoginDto input);
    // CompletableFuture<SignOutResponse> signOut();
}
