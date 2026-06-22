package com.ideasnap.service;

import com.ideasnap.domain.Comment;
import com.ideasnap.domain.Post;
import com.ideasnap.domain.User;
import com.ideasnap.dto.CommentRequest;
import com.ideasnap.dto.CommentResponse;
import com.ideasnap.exception.ResourceNotFoundException;
import com.ideasnap.mapper.CommentMapper;
import com.ideasnap.repository.CommentRepository;
import com.ideasnap.repository.PostRepository;
import com.ideasnap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    @Transactional
    @CacheEvict(value = "feeds", allEntries = true)
    public CommentResponse addComment(UUID userId, UUID postId, CommentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (post.isExpired()) {
            throw new ResourceNotFoundException("Post has expired");
        }

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(request.getContent())
                .build();

        comment = commentRepository.save(comment);
        return commentMapper.toResponse(comment);
    }

    @Override
    @Transactional
    @CacheEvict(value = "feeds", allEntries = true)
    public void deleteComment(UUID commentId, UUID userId, boolean isAdmin) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!isAdmin && !comment.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> getComments(UUID postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (post.isExpired()) {
            throw new ResourceNotFoundException("Post has expired");
        }

        Page<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId, pageable);
        return comments.map(commentMapper::toResponse);
    }
}
