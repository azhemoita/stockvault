package com.javarush.stockvault.service;

import com.javarush.stockvault.entity.User;
import com.javarush.stockvault.repository.UserRepository;
import com.javarush.stockvault.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    JwtService jwtService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserService userService;

    @Test
    @DisplayName("Should create new user with encoded password")
    void registerUser_success() {
        when(passwordEncoder.encode("123456")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = userService.createUser(
                "test@mail.com",
                "testuser",
                "123456"
        );

        assertThat(user.getPassword()).isEqualTo("hashed");
        assertThat(user.getRole()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("Should return JWT token when credentials are valid")
    void login_success() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("hashed");
        user.setRole("ROLE_USER");

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "hashed"))
                .thenReturn(true);
        when(jwtService.generateToken("test@mail.com", "ROLE_USER"))
                .thenReturn("jwt");

        var response = userService.login("test@mail.com", "123456");

        assertThat(response.getToken()).isEqualTo("jwt");
    }

    @Test
    @DisplayName("Should throw exception when password is incorrect")
    void login_wrongPassword_throwsException() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("hashed");

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any()))
                .thenReturn(false);

        assertThatThrownBy(() -> userService.login("test@mail.com", "wrong"))
                .isInstanceOf(BadCredentialsException.class);
    }
}
