package com.example.eventmanagementsystem.controller;

import com.example.eventmanagementsystem.dto.userDto.UserGetDto;
import com.example.eventmanagementsystem.dto.userDto.UserUpdateDto;
import com.example.eventmanagementsystem.response.ApiResponse;
import com.example.eventmanagementsystem.service.abstraction.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @Async
    @GetMapping("/getAll")
    public CompletableFuture<ResponseEntity<ApiResponse<List<UserGetDto>>>> getAllUsers() {
        return userService.getAllUsers()
                .thenApply(apiResponse -> ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse));
    }

    @Async
    @GetMapping("{id}")
    public CompletableFuture<ResponseEntity<ApiResponse<UserGetDto>>> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .thenApply(apiResponse -> ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse));
    }

    @Async
    @PutMapping("/update")
    public CompletableFuture<ResponseEntity<ApiResponse<UserUpdateDto>>> updateUser(@RequestBody UserUpdateDto userUpdateDto) {
        return userService.updateUser(userUpdateDto)
                .thenApply(apiResponse -> ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse));
    }

    @Async
    @DeleteMapping("{id}")
    public CompletableFuture<ResponseEntity<ApiResponse<Boolean>>> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id)
                .thenApply(apiResponse -> ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse));
    }
}
