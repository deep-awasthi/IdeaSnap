package com.ideasnap.dto;

import com.ideasnap.domain.ReactionType;
import jakarta.validation.constraints.NotNull;

public class ReactionRequest {

    @NotNull(message = "Reaction type is required")
    private ReactionType reactionType;

    // Constructors
    public ReactionRequest() {
    }

    public ReactionRequest(ReactionType reactionType) {
        this.reactionType = reactionType;
    }

    // Getters and Setters
    public ReactionType getReactionType() {
        return reactionType;
    }

    public void setReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
    }
}
