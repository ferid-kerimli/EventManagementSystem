package com.example.eventmanagementsystem.dto.userDto;

import lombok.Data;

@Data
public class UserGetDto {
    private Long id;

    private String username;
    private String password;
}
