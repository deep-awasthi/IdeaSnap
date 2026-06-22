package com.ideasnap.controller;

import com.ideasnap.dto.CommentRequest;
import com.ideasnap.dto.CommentResponse;
import com.ideasnap.dto.MessageResponse;
import com.ideasnap.security.UserPrincipal;
import com.ideasnap.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@Tag(name = "Comments", description = "Endpoints for commenting on posts")
public class CommentController {

    @Autowired
    private CommentService commentService;

    private UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal().toString())) {
            return null;
        }
        return (UserPrincipal) authentication.getPrincipal();
    }

    @PostMapping("/api/posts/{id}/comments")
    @Operation(summary = "Add a comment to a post")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable UUID id,
            @Valid @RequestBody CommentRequest request) {
        UserPrincipal principal = getCurrentUser();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CommentResponse response = commentService.addComment(principal.getId(), id, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/api/posts/{id}/comments")
    @Operation(summary = "Get comments of a post with pagination")
    public ResponseEntity<Page<CommentResponse>> getComments(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentResponse> response = commentService.getComments(id, pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/comments/{commentId}")
    @Operation(summary = "Delete comment by ID (Only allowed by author or Admin)")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable UUID commentId) {
        UserPrincipal principal = getCurrentUser();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        boolean isAdmin = principal.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        commentService.deleteComment(commentId, principal.getId(), isAdmin);
        return ResponseEntity.ok(new MessageResponse("Comment deleted successfully"));
    }
}
