package com.ideasnap.dto;

public class UpdateProfileRequest {
    private String bio;
    private String profileImage;

    // Constructors
    public UpdateProfileRequest() {
    }

    public UpdateProfileRequest(String bio, String profileImage) {
        this.bio = bio;
        this.profileImage = profileImage;
    }

    // Getters and Setters
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
}
