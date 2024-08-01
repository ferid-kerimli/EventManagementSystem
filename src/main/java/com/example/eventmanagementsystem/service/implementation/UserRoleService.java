package com.example.eventmanagementsystem.service.implementation;

import com.example.eventmanagementsystem.dto.userDto.AssignRoleDto;
import com.example.eventmanagementsystem.dto.userDto.GetUserWithRoleDto;
import com.example.eventmanagementsystem.entity.Role;
import com.example.eventmanagementsystem.entity.User;
import com.example.eventmanagementsystem.repository.IRoleRepository;
import com.example.eventmanagementsystem.repository.IUserRepository;
import com.example.eventmanagementsystem.response.ApiResponse;
import com.example.eventmanagementsystem.service.abstraction.IUserRoleService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class UserRoleService implements IUserRoleService {
    private final Logger logger = LoggerFactory.getLogger(UserRoleService.class);
    private final ModelMapper modelMapper = new ModelMapper();

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    public UserRoleService(IUserRepository userRepository, IRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Async
    @Override
    public CompletableFuture<ApiResponse<List<GetUserWithRoleDto>>> getUsersWithRole(String roleName) {
        logger.info("getUsersWithRole called");
        var future = new CompletableFuture<ApiResponse<List<GetUserWithRoleDto>>>();
        var response = new ApiResponse<List<GetUserWithRoleDto>>();

        try {
            // Fetch the role by its name
            Optional<Role> optionalRole = roleRepository.findByName(roleName);

            if (optionalRole.isPresent()) {
                Role role = optionalRole.get();

                // Fetch all users with the given role
                var users = userRepository.findByRolesContaining(role);

                var mappedUsers = users.stream()
                        .map(user -> modelMapper.map(user, GetUserWithRoleDto.class))
                        .collect(Collectors.toList());

                response.Success(mappedUsers, 200);
                future.complete(response);
                logger.info("getUsersWithRole completed successfully");
            } else {
                response.Failure("Role not found", 404);
                future.complete(response);
                logger.info("getUsersWithRole failed - Role not found");
            }
        } catch (Exception e) {
            response.Failure(e.getMessage(), 500);
            logger.error("getUsersWithRole failed: {}", e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }

    @Async
    @Override
    @Transactional
    public CompletableFuture<ApiResponse<AssignRoleDto>> assignRoleToUser(AssignRoleDto assignRoleDto) {
        logger.info("assignRoleToUser called");
        var future = new CompletableFuture<ApiResponse<AssignRoleDto>>();
        var response = new ApiResponse<AssignRoleDto>();

        try {
            Optional<User> optionalUser = userRepository.findByUsername(assignRoleDto.getUsername());
            Optional<Role> optionalRole = roleRepository.findByName(assignRoleDto.getRoleName());

            if (optionalUser.isPresent() && optionalRole.isPresent()) {
                User user = optionalUser.get();
                Role role = optionalRole.get();

                user.getRoles().add(role);
                userRepository.save(user);

                response.Success(assignRoleDto, 200);
                future.complete(response);
                logger.info("assignRoleToUser completed successfully");
            } else {
                response.Failure("User or Role not found", 404);
                future.complete(response);
                logger.info("assignRoleToUser failed - User or Role not found");
            }
        }
        catch (Exception e) {
            response.Failure(e.getMessage(), 500);
            logger.error("assignRoleToUser failed: {}", e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }
}
