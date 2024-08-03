package com.example.eventmanagementsystem.dto.eventDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventCreateDto {
    public String name;
    public String description;
    public LocalDateTime dateTime;
    public String username;
}
