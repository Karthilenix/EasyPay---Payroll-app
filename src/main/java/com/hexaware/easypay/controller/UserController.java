package com.hexaware.easypay.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.hexaware.easypay.dto.UserDTO;
import com.hexaware.easypay.entity.User;
import com.hexaware.easypay.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public User addUser(
            @Valid @RequestBody UserDTO dto) {

        return service.addUser(dto);
    }

    @GetMapping("/{id}")
    public User getUser(
            @PathVariable Long id) {

        return service.getUserById(id);
    }

    @GetMapping
    public List<User> getAllUsers() {

        return service.getAllUsers();
    }

    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO dto) {

        return service.updateUser(id, dto);
    }

    @PutMapping("/{id}/disable")
    public String disableUser(
            @PathVariable Long id) {

        service.disableUser(id);

        return "User disabled successfully";
    }
}