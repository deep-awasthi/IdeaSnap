package com.ideasnap.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class FollowId implements Serializable {
    private UUID follower;
    private UUID following;

    // Constructors
    public FollowId() {
    }

    public FollowId(UUID follower, UUID following) {
        this.follower = follower;
        this.following = following;
    }

    // Getters and Setters
    public UUID getFollower() {
        return follower;
    }

    public void setFollower(UUID follower) {
        this.follower = follower;
    }

    public UUID getFollowing() {
        return following;
    }

    public void setFollowing(UUID following) {
        this.following = following;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowId followId = (FollowId) o;
        return Objects.equals(follower, followId.follower) &&
                Objects.equals(following, followId.following);
    }

    @Override
    public int hashCode() {
        return Objects.hash(follower, following);
    }
}
