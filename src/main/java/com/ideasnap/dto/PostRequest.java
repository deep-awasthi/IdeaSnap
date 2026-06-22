package com.ideasnap.dto;

import com.ideasnap.domain.PostVisibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

public class PostRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(max = 1000, message = "Content cannot exceed 1000 characters")
    private String content;

    private Set<String> tags;

    @NotNull(message = "Visibility is required")
    private PostVisibility visibility = PostVisibility.PUBLIC;

    private Integer expiresInMinutes; // Optional expiration

    // Constructors
    public PostRequest() {
    }

    public PostRequest(String title, String content, Set<String> tags, PostVisibility visibility, Integer expiresInMinutes) {
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.visibility = visibility != null ? visibility : PostVisibility.PUBLIC;
        this.expiresInMinutes = expiresInMinutes;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public PostVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(PostVisibility visibility) {
        this.visibility = visibility;
    }

    public Integer getExpiresInMinutes() {
        return expiresInMinutes;
    }

    public void setExpiresInMinutes(Integer expiresInMinutes) {
        this.expiresInMinutes = expiresInMinutes;
    }
}
