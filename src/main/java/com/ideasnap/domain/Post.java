package com.ideasnap.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "post_tags",
        joinColumns = @JoinColumn(name = "post_id")
    )
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PostVisibility visibility = PostVisibility.PUBLIC;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "expires_at")
    private Instant expiresAt;

    // Constructors
    public Post() {
    }

    public Post(UUID id, User author, String title, String content, Set<String> tags, PostVisibility visibility, Instant createdAt, Instant expiresAt) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.tags = tags != null ? tags : new HashSet<>();
        this.visibility = visibility != null ? visibility : PostVisibility.PUBLIC;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.expiresAt = expiresAt;
    }

    // Business checks
    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(Instant.now());
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    // Builder
    public static PostBuilder builder() {
        return new PostBuilder();
    }

    public static class PostBuilder {
        private UUID id;
        private User author;
        private String title;
        private String content;
        private Set<String> tags = new HashSet<>();
        private PostVisibility visibility = PostVisibility.PUBLIC;
        private Instant createdAt = Instant.now();
        private Instant expiresAt;

        public PostBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public PostBuilder author(User author) {
            this.author = author;
            return this;
        }

        public PostBuilder title(String title) {
            this.title = title;
            return this;
        }

        public PostBuilder content(String content) {
            this.content = content;
            return this;
        }

        public PostBuilder tags(Set<String> tags) {
            this.tags = tags;
            return this;
        }

        public PostBuilder visibility(PostVisibility visibility) {
            this.visibility = visibility;
            return this;
        }

        public PostBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public PostBuilder expiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Post build() {
            return new Post(id, author, title, content, tags, visibility, createdAt, expiresAt);
        }
    }
}
