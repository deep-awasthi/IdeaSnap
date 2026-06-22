package com.ideasnap.service;

import com.ideasnap.domain.Post;
import com.ideasnap.dto.PostRequest;
import com.ideasnap.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface PostService {
    Post createPost(UUID authorId, PostRequest request);
    PostResponse getPostById(UUID postId, UUID currentUserId);
    void deletePost(UUID postId, UUID currentUserId, boolean isAdmin);
    Page<PostResponse> getFeed(String sort, UUID currentUserId, Pageable pageable);
    Page<PostResponse> getUserPosts(String username, UUID currentUserId, Pageable pageable);
}
