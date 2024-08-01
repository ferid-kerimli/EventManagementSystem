package com.example.eventmanagementsystem.service.implementation;

import com.example.eventmanagementsystem.dto.userDto.UserGetDto;
import com.example.eventmanagementsystem.dto.userDto.UserUpdateDto;
import com.example.eventmanagementsystem.entity.User;
import com.example.eventmanagementsystem.repository.IUserRepository;
import com.example.eventmanagementsystem.response.ApiResponse;
import com.example.eventmanagementsystem.service.abstraction.IUserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final ModelMapper modelMapper = new ModelMapper();

    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Async
    @Override
    public CompletableFuture<ApiResponse<List<UserGetDto>>> getAllUsers() {
        logger.info("getAllUsers called");
        var future = new CompletableFuture<ApiResponse<List<UserGetDto>>>();
        var response = new ApiResponse<List<UserGetDto>>();

        try {
            var users = userRepository.findAll();

            var mappedUsers = users.stream()
                    .map(user -> modelMapper.map(user, UserGetDto.class))
                    .collect(Collectors.toList());

            response.Success(mappedUsers, 200);
            future.complete(response);
            logger.info("getAllUsers completed successfully");
        }
        catch (Exception e) {
            response.Failure(e.getMessage(), 500);
            logger.error("getAllUsers failed: {}", e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }

    @Async
    @Override
    public CompletableFuture<ApiResponse<UserGetDto>> getUserById(Long id) {
        logger.info("getUserById called");
        var future = new CompletableFuture<ApiResponse<UserGetDto>>();
        var response = new ApiResponse<UserGetDto>();

        try {
            var user = userRepository.findById(id);
            var mappedUser = modelMapper.map(user, UserGetDto.class);

            response.Success(mappedUser, 200);
            future.complete(response);
            logger.info("getUserById completed successfully");
        }
        catch (Exception e) {
            response.Failure(e.getMessage(), 500);
            logger.error("getUserById failed: {}", e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }

    @Async
    @Override
    public CompletableFuture<ApiResponse<UserUpdateDto>> updateUser(UserUpdateDto userUpdateDto) {
        logger.info("updateUser called");
        var future = new CompletableFuture<ApiResponse<UserUpdateDto>>();
        var response = new ApiResponse<UserUpdateDto>();

        try {
            Optional<User> optionalUser = userRepository.findById(userUpdateDto.getId());

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                user.setUsername(userUpdateDto.getUsername());
                user.setPassword(userUpdateDto.getPassword());

                var updatedUser = userRepository.save(user);

                var mappedUpdatedUser = modelMapper.map(updatedUser, UserUpdateDto.class);

                response.Success(mappedUpdatedUser, 204);
                future.complete(response);
                logger.info("updateUser completed successfully");
            }
            else {
                response.Failure("User not found", 404);
                future.complete(response);
                logger.info("Update User failed - User not found");
            }
        }
        catch (Exception e) {
            response.Failure(e.getMessage(), 500);
            logger.error("UpdateUser failed: {}", e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }

    @Async
    @Override
    public CompletableFuture<ApiResponse<Boolean>> deleteUser(Long id) {
        logger.info("deleteUser called");
        var future = new CompletableFuture<ApiResponse<Boolean>>();
        var response = new ApiResponse<Boolean>();

        try {
            Optional<User> optionalUser = userRepository.findById(id);

            if (optionalUser.isPresent()) {
                userRepository.deleteById(id);

                response.Success(true, 200);
                future.complete(response);
                logger.info("deleteUser completed successfully");
            } else {
                response.Failure("User not found", 404);
                future.complete(response);
                logger.info("Delete User failed - user not found");
            }
        }
        catch (Exception e) {
            response.Failure(e.getMessage(), 500);
            logger.error("deleteUser failed: {}", e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }
}
