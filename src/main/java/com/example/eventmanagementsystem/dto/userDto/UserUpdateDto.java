package com.example.eventmanagementsystem.dto.userDto;

import lombok.Data;

@Data
public class UserUpdateDto {
    private Long id;

    private String username;
    private String password;
}
