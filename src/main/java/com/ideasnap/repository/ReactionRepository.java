package com.ideasnap.repository;

import com.ideasnap.domain.Post;
import com.ideasnap.domain.Reaction;
import com.ideasnap.domain.ReactionType;
import com.ideasnap.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findByUserAndPost(User user, Post post);
    Optional<Reaction> findByUserIdAndPostId(UUID userId, UUID postId);
    long countByPostIdAndReactionType(UUID postId, ReactionType reactionType);
    long countByPostId(UUID postId);
}
