package com.ideasnap.mapper;

import com.ideasnap.domain.User;
import com.ideasnap.dto.FollowDto;
import com.ideasnap.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .profileImage(user.getProfileImage())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public FollowDto toFollowDto(User user) {
        if (user == null) {
            return null;
        }
        return FollowDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .bio(user.getBio())
                .profileImage(user.getProfileImage())
                .build();
    }
}
