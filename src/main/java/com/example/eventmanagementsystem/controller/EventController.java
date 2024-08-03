package com.example.eventmanagementsystem.controller;

import com.example.eventmanagementsystem.dto.eventDto.EventCreateDto;
import com.example.eventmanagementsystem.dto.eventDto.EventGetDto;
import com.example.eventmanagementsystem.dto.eventDto.EventUpdateDto;
import com.example.eventmanagementsystem.response.ApiResponse;
import com.example.eventmanagementsystem.service.abstraction.IEventService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final IEventService eventService;

    public EventController(IEventService eventService) {
        this.eventService = eventService;
    }

    @Async
    @GetMapping("/getAll")
    public CompletableFuture<ResponseEntity<ApiResponse<List<EventGetDto>>>> getAllEvents() {
        return eventService.getAllEvents()
                .thenApply(apiResponse -> ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse));
    }

    @Async
    @GetMapping("{id}")
    public CompletableFuture<ResponseEntity<ApiResponse<EventGetDto>>> getEventById(@PathVariable("id") Long id) {
        return eventService.getEventById(id)
                .thenApply(apiResponse -> ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse));
    }

    @Async
    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<ApiResponse<EventCreateDto>>> createEvent(@RequestBody EventCreateDto eventCreateDto) {
        return eventService.createEvent(eventCreateDto)
                .thenApply(apiResponse -> ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse));
    }

    @Async
    @PutMapping("/update")
    public CompletableFuture<ResponseEntity<ApiResponse<EventUpdateDto>>> updateEvent(@RequestBody EventUpdateDto eventUpdateDto) {
        return eventService.updateEvent(eventUpdateDto)
                .thenApply(apiResponse -> ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse));
    }

    @Async
    @DeleteMapping("{id}")
    public CompletableFuture<ResponseEntity<ApiResponse<Boolean>>> deleteEventById(@PathVariable("id") Long id) {
        return eventService.deleteEvent(id)
                .thenApply(apiResponse -> ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse));
    }
}
