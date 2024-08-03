package com.example.eventmanagementsystem.repository;

import com.example.eventmanagementsystem.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IEventRepository extends JpaRepository<Event, Long> {
    List<Event> findByUsername(String username);
    Optional<Event> findByIdAndUsername(Long id, String username);
}
