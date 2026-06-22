package com.ideasnap.dto;

import java.time.Instant;
import java.util.UUID;

public class CommentResponse {
    private UUID id;
    private UUID userId;
    private String username;
    private String content;
    private Instant createdAt;

    // Constructors
    public CommentResponse() {
    }

    public CommentResponse(UUID id, UUID userId, String username, String content, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    // Builder
    public static CommentResponseBuilder builder() {
        return new CommentResponseBuilder();
    }

    public static class CommentResponseBuilder {
        private UUID id;
        private UUID userId;
        private String username;
        private String content;
        private Instant createdAt;

        public CommentResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public CommentResponseBuilder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public CommentResponseBuilder username(String username) {
            this.username = username;
            return this;
        }

        public CommentResponseBuilder content(String content) {
            this.content = content;
            return this;
        }

        public CommentResponseBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public CommentResponse build() {
            return new CommentResponse(id, userId, username, content, createdAt);
        }
    }
}
