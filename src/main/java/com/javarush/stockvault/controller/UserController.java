package com.javarush.stockvault.controller;

import com.javarush.stockvault.dto.LoginRequest;
import com.javarush.stockvault.dto.LoginResponse;
import com.javarush.stockvault.dto.UserRegistrationRequest;
import com.javarush.stockvault.entity.User;
import com.javarush.stockvault.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserRegistrationRequest request) {
        User savedUser = userService.createUser(
                request.getEmail(),
                request.getUsername(),
                request.getPassword()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(
                request.getEmail(),
                request.getPassword()
        );
        return ResponseEntity.ok(response);
    }
}
