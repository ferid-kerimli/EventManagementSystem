package com.example.eventmanagementsystem.service.abstraction;

import com.example.eventmanagementsystem.dto.userDto.AssignRoleDto;
import com.example.eventmanagementsystem.dto.userDto.GetUserWithRoleDto;
import com.example.eventmanagementsystem.response.ApiResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IUserRoleService {
    CompletableFuture<ApiResponse<List<GetUserWithRoleDto>>> getUsersWithRole(String roleName);
    CompletableFuture<ApiResponse<AssignRoleDto>> assignRoleToUser(AssignRoleDto assignRoleDto);
}
