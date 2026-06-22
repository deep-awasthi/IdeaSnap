package com.ideasnap.controller;

import com.ideasnap.dto.MessageResponse;
import com.ideasnap.dto.ReactionRequest;
import com.ideasnap.security.UserPrincipal;
import com.ideasnap.service.ReactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts/{id}/reactions")
@Tag(name = "Reactions", description = "Endpoints for reacting to posts (LIKE, INSIGHTFUL, FIRE, INTERESTING)")
public class ReactionController {

    @Autowired
    private ReactionService reactionService;

    private UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal().toString())) {
            return null;
        }
        return (UserPrincipal) authentication.getPrincipal();
    }

    @PostMapping
    @Operation(summary = "React to a post (Overwrites existing reaction if any)")
    public ResponseEntity<MessageResponse> reactToPost(
            @PathVariable UUID id,
            @Valid @RequestBody ReactionRequest request) {
        UserPrincipal principal = getCurrentUser();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        reactionService.reactToPost(principal.getId(), id, request.getReactionType());
        return ResponseEntity.ok(new MessageResponse("Post reacted successfully with " + request.getReactionType()));
    }

    @DeleteMapping
    @Operation(summary = "Remove reaction from a post")
    public ResponseEntity<MessageResponse> removeReaction(@PathVariable UUID id) {
        UserPrincipal principal = getCurrentUser();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        reactionService.removeReaction(principal.getId(), id);
        return ResponseEntity.ok(new MessageResponse("Reaction removed successfully"));
    }
}
