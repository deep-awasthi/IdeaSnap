package com.ideasnap.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "reactions",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "post_id"})
    }
)
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type", nullable = false, length = 20)
    private ReactionType reactionType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    // Constructors
    public Reaction() {
    }

    public Reaction(Long id, User user, Post post, ReactionType reactionType, Instant createdAt) {
        this.id = id;
        this.user = user;
        this.post = post;
        this.reactionType = reactionType;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public ReactionType getReactionType() {
        return reactionType;
    }

    public void setReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    // Builder
    public static ReactionBuilder builder() {
        return new ReactionBuilder();
    }

    public static class ReactionBuilder {
        private Long id;
        private User user;
        private Post post;
        private ReactionType reactionType;
        private Instant createdAt = Instant.now();

        public ReactionBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ReactionBuilder user(User user) {
            this.user = user;
            return this;
        }

        public ReactionBuilder post(Post post) {
            this.post = post;
            return this;
        }

        public ReactionBuilder reactionType(ReactionType reactionType) {
            this.reactionType = reactionType;
            return this;
        }

        public ReactionBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Reaction build() {
            return new Reaction(id, user, post, reactionType, createdAt);
        }
    }
}
