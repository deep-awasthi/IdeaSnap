package com.ideasnap.dto;

import com.ideasnap.domain.Role;
import java.time.Instant;
import java.util.UUID;

public class UserDto {
    private UUID id;
    private String username;
    private String email;
    private String bio;
    private String profileImage;
    private Role role;
    private Instant createdAt;

    // Constructors
    public UserDto() {
    }

    public UserDto(UUID id, String username, String email, String bio, String profileImage, Role role, Instant createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.profileImage = profileImage;
        this.role = role;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    // Builder
    public static UserDtoBuilder builder() {
        return new UserDtoBuilder();
    }

    public static class UserDtoBuilder {
        private UUID id;
        private String username;
        private String email;
        private String bio;
        private String profileImage;
        private Role role;
        private Instant createdAt;

        public UserDtoBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public UserDtoBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserDtoBuilder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public UserDtoBuilder profileImage(String profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public UserDtoBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UserDtoBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public UserDto build() {
            return new UserDto(id, username, email, bio, profileImage, role, createdAt);
        }
    }
}
