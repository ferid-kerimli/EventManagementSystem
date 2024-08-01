package com.example.eventmanagementsystem.service.implementation;

import com.example.eventmanagementsystem.dto.roleDto.RoleCreateDto;
import com.example.eventmanagementsystem.dto.roleDto.RoleGetDto;
import com.example.eventmanagementsystem.entity.Role;
import com.example.eventmanagementsystem.repository.IRoleRepository;
import com.example.eventmanagementsystem.response.ApiResponse;
import com.example.eventmanagementsystem.service.abstraction.IRoleService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class RoleService implements IRoleService {
    private final Logger logger = LoggerFactory.getLogger(RoleService.class);
    private final ModelMapper modelMapper = new ModelMapper();

    private final IRoleRepository roleRepository;

    public RoleService(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Async
    @Override
    public CompletableFuture<ApiResponse<List<RoleGetDto>>> getAllRoles() {
        logger.info("getAllRoles called");
        var future = new CompletableFuture<ApiResponse<List<RoleGetDto>>>();
        var response = new ApiResponse<List<RoleGetDto>>();

        try {
            var roles = roleRepository.findAll();
            var mappedRoles = roles.stream()
                    .map(role -> modelMapper.map(role, RoleGetDto.class))
                    .toList();

            response.Success(mappedRoles, 200);
            future.complete(response);
            logger.info("getAllRoles completed successfully");
        }
        catch (Exception e) {
            response.Failure(e.getMessage(), 500);
            logger.error("getAllRoles failed: {}", e.getMessage(), e);
            future.completeExceptionally(e);
        }

        logger.info("getRoles completed");
        return future;
    }

    @Async
    @Override
    public CompletableFuture<ApiResponse<RoleGetDto>> getRoleById(Long id) {
        logger.info("getRoleById called");
        var future = new CompletableFuture<ApiResponse<RoleGetDto>>();
        var response = new ApiResponse<RoleGetDto>();

        try {
            var role = roleRepository.findById(id);
            var mappedRole = modelMapper.map(role, RoleGetDto.class);

            response.Success(mappedRole, 200);
            logger.info("getRoleById completed successfully");
            future.complete(response);
        }
        catch (Exception e) {
            response.Failure(e.getMessage(), 500);
            logger.error("getRoleById failed: {}", e.getMessage(), e);
            future.completeExceptionally(e);
        }

        logger.info("getRoleById completed");
        return future;
    }

    @Async
    @Override
    public CompletableFuture<ApiResponse<RoleCreateDto>> createRole(RoleCreateDto roleCreateDto) {
        logger.info("addRole called");
        var future = new CompletableFuture<ApiResponse<RoleCreateDto>>();
        var response = new ApiResponse<RoleCreateDto>();

        try {
            var role = modelMapper.map(roleCreateDto, Role.class);
            roleRepository.save(role);

            response.Success(roleCreateDto, 204);
            future.complete(response);
            logger.info("createRole completed successfully");
        }
        catch (Exception e) {
            response.Failure(e.getMessage(), 500);
            logger.error("createRole failed: {}", e.getMessage(), e);
            future.completeExceptionally(e);
        }

        logger.info("addRole completed");
        return future;
    }

    @Async
    @Override
    public CompletableFuture<ApiResponse<Boolean>> deleteRole(Long id) {
        logger.info("deleteRole called");
        var future = new CompletableFuture<ApiResponse<Boolean>>();
        var response = new ApiResponse<Boolean>();

        try {
            Optional<Role> optionalRole = roleRepository.findById(id);

            if (optionalRole.isPresent()) {
                roleRepository.deleteById(id);

                response.Success(true, 204);
                future.complete(response);
                logger.info("deleteRole completed successfully");
            }
            else {
                response.Failure("Role not found", 500);
                future.complete(response);
                logger.info("Delete Role failed - Role not found");
            }
        }
        catch (Exception e) {
            response.Failure(e.getMessage(), 500);
            logger.error("deleteRole failed: {}", e.getMessage(), e);
            future.completeExceptionally(e);
        }

        logger.info("deleteRole completed");
        return future;
    }
}
