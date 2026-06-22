package com.ideasnap.service;

import com.ideasnap.domain.RefreshToken;
import com.ideasnap.domain.Role;
import com.ideasnap.domain.User;
import com.ideasnap.dto.*;
import com.ideasnap.exception.BadRequestException;
import com.ideasnap.exception.ResourceNotFoundException;
import com.ideasnap.mapper.UserMapper;
import com.ideasnap.repository.UserRepository;
import com.ideasnap.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email Address already in use");
        }

        String profileImage = request.getProfileImage();
        if (profileImage == null || profileImage.trim().isEmpty()) {
            profileImage = "https://api.dicebear.com/7.x/adventurer/svg?seed=" + request.getUsername();
        }

        User user = User.builder()
                .username(request.getUsername().trim().toLowerCase())
                .email(request.getEmail().trim().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .bio(request.getBio())
                .profileImage(profileImage)
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String usernameOrEmail = request.getUsername();
        User user = userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String accessToken = tokenProvider.generateAccessToken(authentication);
        RefreshToken refreshToken = tokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .user(userMapper.toDto(user))
                .build();
    }

    @Override
    @Transactional
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return tokenService.findByToken(requestRefreshToken)
                .map(tokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = tokenProvider.generateAccessTokenFromIdAndUsernameAndRole(
                            user.getId(), user.getUsername(), "ROLE_" + user.getRole().name());
                    // Rotate refresh token
                    RefreshToken newRefreshToken = tokenService.createRefreshToken(user);
                    return TokenRefreshResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(newRefreshToken.getToken())
                            .build();
                })
                .orElseThrow(() -> new BadRequestException("Refresh token is not in database!"));
    }

    @Override
    @Transactional
    public void logout(UUID userId) {
        tokenService.deleteByUserId(userId);
        SecurityContextHolder.clearContext();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserProfile(String username) {
        return userRepository.findByUsername(username.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    @Override
    @Transactional
    public User updateProfile(UUID userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getProfileImage() != null) {
            user.setProfileImage(request.getProfileImage());
        }

        return userRepository.save(user);
    }
}
