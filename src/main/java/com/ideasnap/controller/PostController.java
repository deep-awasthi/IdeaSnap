package com.ideasnap.controller;

import com.ideasnap.domain.Post;
import com.ideasnap.dto.MessageResponse;
import com.ideasnap.dto.PostRequest;
import com.ideasnap.dto.PostResponse;
import com.ideasnap.security.UserPrincipal;
import com.ideasnap.service.PostService;
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
@RequestMapping("/api/posts")
@Tag(name = "Posts", description = "Endpoints for creating, fetching, and deleting technical posts")
public class PostController {

    @Autowired
    private PostService postService;

    private UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal().toString())) {
            return null;
        }
        return (UserPrincipal) authentication.getPrincipal();
    }

    @PostMapping
    @Operation(summary = "Create a short technical post (supports markdown, tags, and optional expiration)")
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest request) {
        UserPrincipal principal = getCurrentUser();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Post post = postService.createPost(principal.getId(), request);
        // Expose post response
        PostResponse response = postService.getPostById(post.getId(), principal.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch a single technical post by its ID")
    public ResponseEntity<PostResponse> getPostById(@PathVariable UUID id) {
        UserPrincipal principal = getCurrentUser();
        UUID currentUserId = principal != null ? principal.getId() : null;
        PostResponse response = postService.getPostById(id, currentUserId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a post by ID (Only allowed by author or Admin)")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable UUID id) {
        UserPrincipal principal = getCurrentUser();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        boolean isAdmin = principal.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        postService.deletePost(id, principal.getId(), isAdmin);
        return ResponseEntity.ok(new MessageResponse("Post deleted successfully"));
    }

    @GetMapping
    @Operation(summary = "Retrieve public feed (supports sorting by 'latest' or 'trending', with pagination)")
    public ResponseEntity<Page<PostResponse>> getFeed(
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UserPrincipal principal = getCurrentUser();
        UUID currentUserId = principal != null ? principal.getId() : null;
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponse> response = postService.getFeed(sort, currentUserId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{username}")
    @Operation(summary = "Retrieve public posts of a specific user, with pagination")
    public ResponseEntity<Page<PostResponse>> getUserPosts(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UserPrincipal principal = getCurrentUser();
        UUID currentUserId = principal != null ? principal.getId() : null;
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponse> response = postService.getUserPosts(username, currentUserId, pageable);
        return ResponseEntity.ok(response);
    }
}
