package com.ideasnap.controller;

import com.ideasnap.domain.Role;
import com.ideasnap.domain.User;
import com.ideasnap.dto.AuthRequest;
import com.ideasnap.dto.AuthResponse;
import com.ideasnap.dto.RegisterRequest;
import com.ideasnap.dto.UserDto;
import com.ideasnap.mapper.UserMapper;
import com.ideasnap.security.CustomUserDetailsService;
import com.ideasnap.security.JwtAuthenticationEntryPoint;
import com.ideasnap.security.JwtAuthenticationFilter;
import com.ideasnap.security.JwtTokenProvider;
import com.ideasnap.security.RateLimitingFilter;
import com.ideasnap.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.Instant;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters to easily test controller logic
public class AuthControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private RateLimitingFilter rateLimitingFilter;

    private RegisterRequest registerRequest;
    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("alice");
        registerRequest.setEmail("alice@ideasnap.com");
        registerRequest.setPassword("password123");
        registerRequest.setBio("Spring Developer");

        authRequest = new AuthRequest();
        authRequest.setUsername("alice");
        authRequest.setPassword("password123");
    }

    @Test
    void register_ShouldReturnUserDto() throws Exception {
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("alice")
                .email("alice@ideasnap.com")
                .role(Role.USER)
                .createdAt(Instant.now())
                .build();

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();

        when(userService.registerUser(any(RegisterRequest.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.email").value("alice@ideasnap.com"));
    }

    @Test
    void login_ShouldReturnAuthResponse() throws Exception {
        UserDto userDto = UserDto.builder()
                .username("alice")
                .email("alice@ideasnap.com")
                .role(Role.USER)
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken("access_token")
                .refreshToken("refresh_token")
                .user(userDto)
                .build();

        when(userService.login(any(AuthRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access_token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh_token"))
                .andExpect(jsonPath("$.user.username").value("alice"));
    }
}
