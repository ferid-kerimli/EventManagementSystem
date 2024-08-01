package com.example.eventmanagementsystem.controller;

import com.example.eventmanagementsystem.dto.roleDto.RoleCreateDto;
import com.example.eventmanagementsystem.dto.roleDto.RoleGetDto;
import com.example.eventmanagementsystem.response.ApiResponse;
import com.example.eventmanagementsystem.service.abstraction.IRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final IRoleService roleService;

    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }

    @Async
    @GetMapping("getAll")
    public CompletableFuture<ResponseEntity<ApiResponse<List<RoleGetDto>>>> getAllRoles() {
        return roleService.getAllRoles()
                .thenApply(apiResponse -> ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse));
    }

    @Async
    @GetMapping("{id}")
    public CompletableFuture<ResponseEntity<ApiResponse<RoleGetDto>>> getRoleById(@PathVariable("id") Long id) {
        return roleService.getRoleById(id)
                .thenApply(apiResponse -> ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse));
    }

    @Async
    @PostMapping("create")
    public CompletableFuture<ResponseEntity<ApiResponse<RoleCreateDto>>> createRole(@RequestBody RoleCreateDto roleCreateDto) {
        return roleService.createRole(roleCreateDto)
                .thenApply(apiResponse -> ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse));
    }

    @Async
    @DeleteMapping("{id}")
    public CompletableFuture<ResponseEntity<ApiResponse<Boolean>>> deleteRole(@PathVariable("id") Long id) {
        return roleService.deleteRole(id)
                .thenApply(apiResponse -> ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse));
    }
}
