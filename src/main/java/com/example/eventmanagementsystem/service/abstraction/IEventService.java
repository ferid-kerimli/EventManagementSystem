package com.example.eventmanagementsystem.service.abstraction;

import com.example.eventmanagementsystem.dto.eventDto.EventCreateDto;
import com.example.eventmanagementsystem.dto.eventDto.EventGetDto;
import com.example.eventmanagementsystem.dto.eventDto.EventUpdateDto;
import com.example.eventmanagementsystem.response.ApiResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IEventService {
    CompletableFuture<ApiResponse<List<EventGetDto>>> getAllEvents();
    CompletableFuture<ApiResponse<EventGetDto>> getEventById(Long id);
    CompletableFuture<ApiResponse<EventCreateDto>> createEvent(EventCreateDto eventCreateDto);
    CompletableFuture<ApiResponse<EventUpdateDto>> updateEvent(EventUpdateDto eventUpdateDto);
    CompletableFuture<ApiResponse<Boolean>> deleteEvent(Long id);
}
