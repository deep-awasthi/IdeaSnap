package com.ideasnap.service;

import com.ideasnap.domain.Role;
import com.ideasnap.domain.User;
import com.ideasnap.dto.RegisterRequest;
import com.ideasnap.exception.BadRequestException;
import com.ideasnap.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@ideasnap.com");
        registerRequest.setPassword("password123");
        registerRequest.setBio("Hello World");
    }

    @Test
    void registerUser_Success() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        
        User savedUser = User.builder()
                .username("testuser")
                .email("test@ideasnap.com")
                .password("hashed_password")
                .bio("Hello World")
                .role(Role.USER)
                .build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.registerUser(registerRequest);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@ideasnap.com", result.getEmail());
        assertEquals("hashed_password", result.getPassword());
        assertEquals(Role.USER, result.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_UsernameTaken() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.registerUser(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_EmailTaken() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.registerUser(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }
}
