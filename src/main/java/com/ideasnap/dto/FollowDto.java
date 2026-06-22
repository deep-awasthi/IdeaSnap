package com.ideasnap.dto;

import java.util.UUID;

public class FollowDto {
    private UUID id;
    private String username;
    private String bio;
    private String profileImage;

    // Constructors
    public FollowDto() {
    }

    public FollowDto(UUID id, String username, String bio, String profileImage) {
        this.id = id;
        this.username = username;
        this.bio = bio;
        this.profileImage = profileImage;
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

    // Builder
    public static FollowDtoBuilder builder() {
        return new FollowDtoBuilder();
    }

    public static class FollowDtoBuilder {
        private UUID id;
        private String username;
        private String bio;
        private String profileImage;

        public FollowDtoBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public FollowDtoBuilder username(String username) {
            this.username = username;
            return this;
        }

        public FollowDtoBuilder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public FollowDtoBuilder profileImage(String profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public FollowDto build() {
            return new FollowDto(id, username, bio, profileImage);
        }
    }
}
