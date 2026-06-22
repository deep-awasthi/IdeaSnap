package com.ideasnap.controller;

import com.ideasnap.domain.User;
import com.ideasnap.dto.UpdateProfileRequest;
import com.ideasnap.dto.UserDto;
import com.ideasnap.mapper.UserMapper;
import com.ideasnap.security.UserPrincipal;
import com.ideasnap.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints for fetching and updating user profiles")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/{username}")
    @Operation(summary = "Get user profile details by username")
    public ResponseEntity<UserDto> getUserProfile(@PathVariable String username) {
        User user = userService.getUserProfile(username);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PutMapping("/profile")
    @Operation(summary = "Update current user's profile details (bio / profile image)")
    public ResponseEntity<UserDto> updateProfile(@RequestBody UpdateProfileRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal principal) {
            User updatedUser = userService.updateProfile(principal.getId(), request);
            return ResponseEntity.ok(userMapper.toDto(updatedUser));
        }
        return ResponseEntity.status(401).build();
    }
}
