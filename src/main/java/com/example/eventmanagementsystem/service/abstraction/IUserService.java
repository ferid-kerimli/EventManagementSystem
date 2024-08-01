package com.example.eventmanagementsystem.service.abstraction;

import com.example.eventmanagementsystem.dto.userDto.AssignRoleDto;
import com.example.eventmanagementsystem.dto.userDto.GetUserWithRoleDto;
import com.example.eventmanagementsystem.dto.userDto.UserGetDto;
import com.example.eventmanagementsystem.dto.userDto.UserUpdateDto;
import com.example.eventmanagementsystem.response.ApiResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IUserService {
    CompletableFuture<ApiResponse<List<UserGetDto>>> getAllUsers();
    CompletableFuture<ApiResponse<UserGetDto>> getUserById(Long id);
    CompletableFuture<ApiResponse<UserUpdateDto>> updateUser(UserUpdateDto userUpdateDto);
    CompletableFuture<ApiResponse<Boolean>> deleteUser(Long id);
}
