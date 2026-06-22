package com.ideasnap.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "follows")
@IdClass(FollowId.class)
public class Follow {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User follower;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private User following;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    // Constructors
    public Follow() {
    }

    public Follow(User follower, User following, Instant createdAt) {
        this.follower = follower;
        this.following = following;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
    }

    // Getters and Setters
    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowing() {
        return following;
    }

    public void setFollowing(User following) {
        this.following = following;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    // Builder
    public static FollowBuilder builder() {
        return new FollowBuilder();
    }

    public static class FollowBuilder {
        private User follower;
        private User following;
        private Instant createdAt = Instant.now();

        public FollowBuilder follower(User follower) {
            this.follower = follower;
            return this;
        }

        public FollowBuilder following(User following) {
            this.following = following;
            return this;
        }

        public FollowBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Follow build() {
            return new Follow(follower, following, createdAt);
        }
    }
}
