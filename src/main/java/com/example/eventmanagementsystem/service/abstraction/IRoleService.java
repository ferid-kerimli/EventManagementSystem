package com.example.eventmanagementsystem.service.abstraction;

import com.example.eventmanagementsystem.dto.roleDto.RoleCreateDto;
import com.example.eventmanagementsystem.dto.roleDto.RoleGetDto;
import com.example.eventmanagementsystem.entity.Role;
import com.example.eventmanagementsystem.response.ApiResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IRoleService {
    CompletableFuture<ApiResponse<List<RoleGetDto>>> getAllRoles();
    CompletableFuture<ApiResponse<RoleGetDto>> getRoleById(Long id);
    CompletableFuture<ApiResponse<RoleCreateDto>> createRole(RoleCreateDto roleCreateDto);
    CompletableFuture<ApiResponse<Boolean>> deleteRole(Long id);
}
