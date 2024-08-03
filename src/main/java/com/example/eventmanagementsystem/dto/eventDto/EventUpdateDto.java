package com.example.eventmanagementsystem.dto.eventDto;

import lombok.Data;

@Data
public class EventUpdateDto {
    public Long id;
    public String name;
    public String description;
}
