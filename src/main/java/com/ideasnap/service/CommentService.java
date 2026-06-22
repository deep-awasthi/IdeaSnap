package com.ideasnap.service;

import com.ideasnap.dto.CommentRequest;
import com.ideasnap.dto.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface CommentService {
    CommentResponse addComment(UUID userId, UUID postId, CommentRequest request);
    void deleteComment(UUID commentId, UUID userId, boolean isAdmin);
    Page<CommentResponse> getComments(UUID postId, Pageable pageable);
}
