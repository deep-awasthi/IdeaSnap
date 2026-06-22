package com.ideasnap.service;

import com.ideasnap.domain.ReactionType;
import java.util.UUID;

public interface ReactionService {
    void reactToPost(UUID userId, UUID postId, ReactionType type);
    void removeReaction(UUID userId, UUID postId);
}
