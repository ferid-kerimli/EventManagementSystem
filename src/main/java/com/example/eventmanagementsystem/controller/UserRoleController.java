package com.example.eventmanagementsystem.controller;

import com.example.eventmanagementsystem.dto.userDto.AssignRoleDto;
import com.example.eventmanagementsystem.dto.userDto.GetUserWithRoleDto;
import com.example.eventmanagementsystem.response.ApiResponse;
import com.example.eventmanagementsystem.service.abstraction.IUserRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/usersAndRoles")
public class UserRoleController {
    private final IUserRoleService userRoleService;

    public UserRoleController(IUserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @Async
    @GetMapping("/{roleName}")
    public CompletableFuture<ResponseEntity<ApiResponse<List<GetUserWithRoleDto>>>> getUsersWithRole(@PathVariable String roleName) {
        return userRoleService.getUsersWithRole(roleName)
                .thenApply(apiResponse -> ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse));
    }

    @Async
    @PostMapping("/assignRoleToUser")
    public CompletableFuture<ResponseEntity<ApiResponse<AssignRoleDto>>> assignRoleToUser(@RequestBody AssignRoleDto assignRoleDto) {
        return userRoleService.assignRoleToUser(assignRoleDto)
                .thenApply(apiResponse -> ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse));
    }
}
