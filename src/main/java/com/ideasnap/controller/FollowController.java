package com.ideasnap.controller;

import com.ideasnap.dto.FollowDto;
import com.ideasnap.dto.MessageResponse;
import com.ideasnap.security.UserPrincipal;
import com.ideasnap.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/users/{username}")
@Tag(name = "Follows", description = "Endpoints for following/unfollowing users and listing followers/following")
public class FollowController {

    @Autowired
    private FollowService followService;

    private UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal().toString())) {
            return null;
        }
        return (UserPrincipal) authentication.getPrincipal();
    }

    @PostMapping("/follow")
    @Operation(summary = "Follow a developer")
    public ResponseEntity<MessageResponse> followUser(@PathVariable String username) {
        UserPrincipal principal = getCurrentUser();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        followService.follow(principal.getId(), username);
        return ResponseEntity.ok(new MessageResponse("Successfully followed user " + username));
    }

    @PostMapping("/unfollow")
    @Operation(summary = "Unfollow a developer")
    public ResponseEntity<MessageResponse> unfollowUser(@PathVariable String username) {
        UserPrincipal principal = getCurrentUser();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        followService.unfollow(principal.getId(), username);
        return ResponseEntity.ok(new MessageResponse("Successfully unfollowed user " + username));
    }

    @GetMapping("/followers")
    @Operation(summary = "Get followers of a user, with pagination")
    public ResponseEntity<Page<FollowDto>> getFollowers(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FollowDto> followers = followService.getFollowers(username, pageable);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/following")
    @Operation(summary = "Get users followed by a user, with pagination")
    public ResponseEntity<Page<FollowDto>> getFollowing(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FollowDto> following = followService.getFollowing(username, pageable);
        return ResponseEntity.ok(following);
    }
}
