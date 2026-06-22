package com.ideasnap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CommentRequest {

    @NotBlank(message = "Comment content is required")
    @Size(max = 500, message = "Comment content cannot exceed 500 characters")
    private String content;

    // Constructors
    public CommentRequest() {
    }

    public CommentRequest(String content) {
        this.content = content;
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
