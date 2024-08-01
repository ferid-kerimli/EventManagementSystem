package com.example.eventmanagementsystem.repository;

import com.example.eventmanagementsystem.entity.Role;
import com.example.eventmanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByRolesContaining(Role role);
}
