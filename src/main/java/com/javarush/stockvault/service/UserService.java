package com.javarush.stockvault.service;

import com.javarush.stockvault.dto.LoginResponse;
import com.javarush.stockvault.entity.User;
import com.javarush.stockvault.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(String email, String username, String password) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        return userRepository.save(user);
    }

    public LoginResponse login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return new LoginResponse(user.getId(), user.getEmail());
    }
}
