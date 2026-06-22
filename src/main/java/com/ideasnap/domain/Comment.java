package com.ideasnap.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    // Constructors
    public Comment() {
    }

    public Comment(UUID id, User user, Post post, String content, Instant createdAt) {
        this.id = id;
        this.user = user;
        this.post = post;
        this.content = content;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
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
    public static CommentBuilder builder() {
        return new CommentBuilder();
    }

    public static class CommentBuilder {
        private UUID id;
        private User user;
        private Post post;
        private String content;
        private Instant createdAt = Instant.now();

        public CommentBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public CommentBuilder user(User user) {
            this.user = user;
            return this;
        }

        public CommentBuilder post(Post post) {
            this.post = post;
            return this;
        }

        public CommentBuilder content(String content) {
            this.content = content;
            return this;
        }

        public CommentBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Comment build() {
            return new Comment(id, user, post, content, createdAt);
        }
    }
}
