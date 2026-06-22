package com.ideasnap.controller;

import com.ideasnap.domain.User;
import com.ideasnap.dto.*;
import com.ideasnap.mapper.UserMapper;
import com.ideasnap.security.UserPrincipal;
import com.ideasnap.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for User Registration, Login, Logout, and Token Refresh")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    @Operation(summary = "Register a new developer account")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody RegisterRequest request) {
        User registeredUser = userService.registerUser(request);
        return new ResponseEntity<>(userMapper.toDto(registeredUser), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and return JWT tokens")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh an expired access token using a refresh token")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        TokenRefreshResponse response = userService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout the current user and invalidate their refresh token")
    public ResponseEntity<MessageResponse> logoutUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal principal) {
            userService.logout(principal.getId());
            return ResponseEntity.ok(new MessageResponse("User logged out successfully"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("User not authenticated"));
    }
}
