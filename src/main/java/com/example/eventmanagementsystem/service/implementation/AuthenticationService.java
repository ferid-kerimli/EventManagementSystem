package com.example.eventmanagementsystem.service.implementation;

import com.example.eventmanagementsystem.dto.accountDto.UserLoginDto;
import com.example.eventmanagementsystem.dto.accountDto.UserRegisterDto;
import com.example.eventmanagementsystem.entity.User;
import com.example.eventmanagementsystem.exception.applicationExceptions.InvalidCredentialsException;
import com.example.eventmanagementsystem.exception.applicationExceptions.UsernameAlreadyExistException;
import com.example.eventmanagementsystem.repository.IUserRepository;
import com.example.eventmanagementsystem.response.AuthenticationResponse;
import com.example.eventmanagementsystem.response.SignUpResponse;
import com.example.eventmanagementsystem.service.abstraction.IAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AuthenticationService implements IAuthenticationService {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final IUserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationService(IUserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Async
    @Override
    public CompletableFuture<SignUpResponse> signUp(UserRegisterDto input) {
        logger.info("signUp Called");
        var future = new CompletableFuture<SignUpResponse>();
        var response = new SignUpResponse();

        try {
            if (userRepository.findByUsername(input.getUsername()).isPresent()) {
                throw new UsernameAlreadyExistException("Username already exist");
            }

            User user = new User();

            user.setUsername(input.getUsername());
            user.setPassword(passwordEncoder.encode(input.getPassword()));

            userRepository.save(user);

            response.setMessage("User created Successfully");
            logger.info("User created Successfully");
            future.complete(response);
        }
        catch (Exception e) {
            response.setMessage("Error while signing up");
            logger.info("signUp Error: {}", e.getMessage());
            future.completeExceptionally(e);
        }

        return future;
    }

    @Async
    @Override
    public CompletableFuture<AuthenticationResponse> login(UserLoginDto input) {
        logger.info("login called");
        var future = new CompletableFuture<AuthenticationResponse>();
        var response = new AuthenticationResponse();

        try {
            if (userRepository.findByUsername(input.getUsername()).isEmpty()) {
                throw new UsernameNotFoundException("Username not found");
            }

            authenticateUser(input, response);
            future.complete(response);
            logger.info("User logged in successfully");
        }
        catch (Exception e) {
            response.setMessage("Error while login");
            logger.error("login Error: {}", e.getMessage());
            future.completeExceptionally(e);
        }

        return future;
    }

    private void authenticateUser(UserLoginDto input, AuthenticationResponse response) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwtToken = jwtService.generateToken(userDetails);

            response.setMessage("User logged in successfully");
            response.setToken(jwtToken);
            response.setExpiresIn(jwtService.getExpirationTime());
        }
        catch (Exception e) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }
}
