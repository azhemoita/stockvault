package com.javarush.stockvault.controller;

import com.javarush.stockvault.dto.LoginRequest;
import com.javarush.stockvault.dto.LoginResponse;
import com.javarush.stockvault.entity.User;
import com.javarush.stockvault.service.UserService;
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
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(
                user.getEmail(),
                user.getUsername(),
                user.getPassword()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(
                request.getEmail(),
                request.getPassword()
        );
        return ResponseEntity.ok(response);
    }
}
