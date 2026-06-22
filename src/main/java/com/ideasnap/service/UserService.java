package com.ideasnap.service;

import com.ideasnap.domain.User;
import com.ideasnap.dto.*;
import java.util.UUID;

public interface UserService {
    User registerUser(RegisterRequest request);
    AuthResponse login(AuthRequest request);
    TokenRefreshResponse refreshToken(TokenRefreshRequest request);
    void logout(UUID userId);
    User getUserProfile(String username);
    User updateProfile(UUID userId, UpdateProfileRequest request);
}
