package com.ideasnap.controller;

import com.ideasnap.dto.PostResponse;
import com.ideasnap.security.UserPrincipal;
import com.ideasnap.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/api/search")
@Tag(name = "Search", description = "Endpoints for searching posts by username, tag, or title query")
public class SearchController {

    @Autowired
    private SearchService searchService;

    private UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal().toString())) {
            return null;
        }
        return (UserPrincipal) authentication.getPrincipal();
    }

    @GetMapping
    @Operation(summary = "Search posts by query (title search), tag, or author username with pagination")
    public ResponseEntity<Page<PostResponse>> searchPosts(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UserPrincipal principal = getCurrentUser();
        UUID currentUserId = principal != null ? principal.getId() : null;
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponse> response = searchService.searchPosts(query, tag, username, currentUserId, pageable);
        return ResponseEntity.ok(response);
    }
}
