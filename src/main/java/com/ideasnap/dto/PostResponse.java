package com.ideasnap.dto;

import com.ideasnap.domain.PostVisibility;
import com.ideasnap.domain.ReactionType;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import java.io.Serializable;

public class PostResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private UUID authorId;
    private String authorUsername;
    private String title;
    private String content;
    private Set<String> tags;
    private PostVisibility visibility;
    private Instant createdAt;
    private Instant expiresAt;
    private long likesCount;
    private long commentsCount;
    private ReactionType reactedType;

    // Constructors
    public PostResponse() {
    }

    public PostResponse(UUID id, UUID authorId, String authorUsername, String title, String content, Set<String> tags, PostVisibility visibility, Instant createdAt, Instant expiresAt, long likesCount, long commentsCount, ReactionType reactedType) {
        this.id = id;
        this.authorId = authorId;
        this.authorUsername = authorUsername;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.visibility = visibility;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.reactedType = reactedType;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
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

    public long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(long likesCount) {
        this.likesCount = likesCount;
    }

    public long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(long commentsCount) {
        this.commentsCount = commentsCount;
    }

    public ReactionType getReactedType() {
        return reactedType;
    }

    public void setReactedType(ReactionType reactedType) {
        this.reactedType = reactedType;
    }

    // Builder
    public static PostResponseBuilder builder() {
        return new PostResponseBuilder();
    }

    public static class PostResponseBuilder {
        private UUID id;
        private UUID authorId;
        private String authorUsername;
        private String title;
        private String content;
        private Set<String> tags;
        private PostVisibility visibility;
        private Instant createdAt;
        private Instant expiresAt;
        private long likesCount;
        private long commentsCount;
        private ReactionType reactedType;

        public PostResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public PostResponseBuilder authorId(UUID authorId) {
            this.authorId = authorId;
            return this;
        }

        public PostResponseBuilder authorUsername(String authorUsername) {
            this.authorUsername = authorUsername;
            return this;
        }

        public PostResponseBuilder title(String title) {
            this.title = title;
            return this;
        }

        public PostResponseBuilder content(String content) {
            this.content = content;
            return this;
        }

        public PostResponseBuilder tags(Set<String> tags) {
            this.tags = tags;
            return this;
        }

        public PostResponseBuilder visibility(PostVisibility visibility) {
            this.visibility = visibility;
            return this;
        }

        public PostResponseBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public PostResponseBuilder expiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public PostResponseBuilder likesCount(long likesCount) {
            this.likesCount = likesCount;
            return this;
        }

        public PostResponseBuilder commentsCount(long commentsCount) {
            this.commentsCount = commentsCount;
            return this;
        }

        public PostResponseBuilder reactedType(ReactionType reactedType) {
            this.reactedType = reactedType;
            return this;
        }

        public PostResponse build() {
            return new PostResponse(id, authorId, authorUsername, title, content, tags, visibility, createdAt, expiresAt, likesCount, commentsCount, reactedType);
        }
    }
}
