package com.ideasnap.repository;

import com.ideasnap.domain.Follow;
import com.ideasnap.domain.FollowId;
import com.ideasnap.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowId> {
    Page<Follow> findByFollowingId(UUID followingId, Pageable pageable);
    Page<Follow> findByFollowerId(UUID followerId, Pageable pageable);
    boolean existsByFollowerIdAndFollowingId(UUID followerId, UUID followingId);
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
    long countByFollowerId(UUID followerId);
    long countByFollowingId(UUID followingId);
}
