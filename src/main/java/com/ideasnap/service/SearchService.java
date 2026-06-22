package com.ideasnap.service;

import com.ideasnap.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface SearchService {
    Page<PostResponse> searchPosts(String query, String tag, String username, UUID currentUserId, Pageable pageable);
}
