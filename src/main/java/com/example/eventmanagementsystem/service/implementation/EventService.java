package com.example.eventmanagementsystem.service.implementation;

import com.example.eventmanagementsystem.dto.eventDto.EventCreateDto;
import com.example.eventmanagementsystem.dto.eventDto.EventGetDto;
import com.example.eventmanagementsystem.dto.eventDto.EventUpdateDto;
import com.example.eventmanagementsystem.entity.Event;
import com.example.eventmanagementsystem.repository.IEventRepository;
import com.example.eventmanagementsystem.response.ApiResponse;
import com.example.eventmanagementsystem.service.abstraction.IEventService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class EventService implements IEventService {
    private final Logger logger = LoggerFactory.getLogger(EventService.class);
    private final ModelMapper modelMapper = new ModelMapper();
    private final IEventRepository eventRepository;

    public EventService(IEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    private String getLoggedInUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @Async
    @Override
    public CompletableFuture<ApiResponse<List<EventGetDto>>> getAllEvents() {
        logger.info("getAllEvents called");
        var future = new CompletableFuture<ApiResponse<List<EventGetDto>>>();
        var response = new ApiResponse<List<EventGetDto>>();

        try {
            String username = getLoggedInUsername();
            var events = eventRepository.findByUsername(username);

            var mappedEvents = events.stream()
                    .map(event -> modelMapper.map(event, EventGetDto.class))
                    .toList();

            response.Success(mappedEvents, 200);
            logger.info("getAllEvents completed successfully");
            future.complete(response);
        }
        catch (Exception e) {
            response.Failure(e.getMessage(), 500);
            logger.error("getAllEvents failed: {}", e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }

    @Async
    @Override
    public CompletableFuture<ApiResponse<EventGetDto>> getEventById(Long id) {
        logger.info("getEventById called");
        var future = new CompletableFuture<ApiResponse<EventGetDto>>();
        var response = new ApiResponse<EventGetDto>();

        try {
            String username = getLoggedInUsername();
            Optional<Event> event = eventRepository.findByIdAndUsername(id, username);

            if (event.isPresent()) {
                var mappedEvent = modelMapper.map(event.get(), EventGetDto.class);
                response.Success(mappedEvent, 200);
                future.complete(response);
                logger.info("getEventById completed successfully");
            } else {
                response.Failure("Event not found", 500);
                future.complete(response);
                logger.info("getEventById failed - Event not found");
            }
        }
        catch (Exception e) {
            response.Failure(e.getMessage(), 500);
            logger.error("getEventById failed: {}", e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }

    @Async
    @Override
    public CompletableFuture<ApiResponse<EventCreateDto>> createEvent(EventCreateDto eventCreateDto) {
        logger.info("createEvent called");
        var future = new CompletableFuture<ApiResponse<EventCreateDto>>();
        var response = new ApiResponse<EventCreateDto>();

        try {
            String username = getLoggedInUsername();
            var event = modelMapper.map(eventCreateDto, Event.class);
            event.setUsername(username);

            var savedEvent = eventRepository.save(event);
            EventCreateDto savedEventDto = modelMapper.map(savedEvent, EventCreateDto.class);

            response.Success(savedEventDto, 201);
            future.complete(response);
            logger.info("createEvent completed successfully");
        }
        catch (Exception e) {
            response.Failure(e.getMessage(), 500);
            logger.error("createEvent failed: {}", e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }

    @Async
    @Override
    public CompletableFuture<ApiResponse<EventUpdateDto>> updateEvent(EventUpdateDto eventUpdateDto) {
        logger.info("updateEvent called");
        var future = new CompletableFuture<ApiResponse<EventUpdateDto>>();
        var response = new ApiResponse<EventUpdateDto>();

        try {
            String username = getLoggedInUsername();
            Optional<Event> optionalEvent = eventRepository.findByIdAndUsername(eventUpdateDto.getId(), username);

            if (optionalEvent.isPresent()) {
                Event event = optionalEvent.get();
                modelMapper.map(eventUpdateDto, event);

                Event updatedEvent = eventRepository.save(event);
                EventUpdateDto updatedEventDto = modelMapper.map(updatedEvent, EventUpdateDto.class);

                response.Success(updatedEventDto, 204);
                future.complete(response);
                logger.info("updateEvent completed successfully");
            } else {
                response.Failure("Event not found", 404);
                future.complete(response);
                logger.info("updateEvent failed - Event not found");
            }
        }
        catch (Exception e) {
            response.Failure(e.getMessage(), 500);
            logger.error("updateEvent failed: {}", e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }

    @Async
    @Override
    public CompletableFuture<ApiResponse<Boolean>> deleteEvent(Long id) {
        logger.info("deleteEvent called");
        var future = new CompletableFuture<ApiResponse<Boolean>>();
        var response = new ApiResponse<Boolean>();

        try {
            String username = getLoggedInUsername();
            Optional<Event> optionalEvent = eventRepository.findByIdAndUsername(id, username);

            if (optionalEvent.isPresent()) {
                eventRepository.delete(optionalEvent.get());

                response.Success(true, 200);
                future.complete(response);
                logger.info("deleteEvent completed successfully");
            } else {
                response.Failure("Event not found", 404);
                future.complete(response);
                logger.info("deleteEvent failed - Event not found");
            }
        }
        catch (Exception e) {
            response.Failure("deleteEvent failed", 500);
            logger.error("deleteEvent failed: {}", e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }
}