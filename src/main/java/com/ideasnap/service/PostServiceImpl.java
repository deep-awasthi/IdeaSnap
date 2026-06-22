package com.ideasnap.service;

import com.ideasnap.domain.Post;
import com.ideasnap.domain.PostVisibility;
import com.ideasnap.domain.Reaction;
import com.ideasnap.domain.ReactionType;
import com.ideasnap.domain.User;
import com.ideasnap.dto.PostRequest;
import com.ideasnap.dto.PostResponse;
import com.ideasnap.exception.BadRequestException;
import com.ideasnap.exception.ResourceNotFoundException;
import com.ideasnap.mapper.PostMapper;
import com.ideasnap.repository.CommentRepository;
import com.ideasnap.repository.PostRepository;
import com.ideasnap.repository.ReactionRepository;
import com.ideasnap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostMapper postMapper;

    @Override
    @Transactional
    @CacheEvict(value = "feeds", allEntries = true)
    public Post createPost(UUID authorId, PostRequest request) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        if (request.getContent().length() > 1000) {
            throw new BadRequestException("Content exceeds maximum length of 1000 characters");
        }

        Instant expiresAt = null;
        if (request.getExpiresInMinutes() != null && request.getExpiresInMinutes() > 0) {
            expiresAt = Instant.now().plus(request.getExpiresInMinutes(), ChronoUnit.MINUTES);
        }

        Post post = Post.builder()
                .author(author)
                .title(request.getTitle())
                .content(request.getContent())
                .tags(request.getTags() != null ? request.getTags() : new HashSet<>())
                .visibility(request.getVisibility())
                .expiresAt(expiresAt)
                .build();

        return postRepository.save(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getPostById(UUID postId, UUID currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        if (post.isExpired()) {
            throw new ResourceNotFoundException("Post has expired");
        }

        if (post.getVisibility() == PostVisibility.PRIVATE) {
            if (currentUserId == null || !post.getAuthor().getId().equals(currentUserId)) {
                throw new AccessDeniedException("You do not have permission to view this private post");
            }
        }

        long likesCount = reactionRepository.countByPostId(post.getId());
        long commentsCount = commentRepository.countByPostId(post.getId());
        ReactionType reactedType = null;

        if (currentUserId != null) {
            reactedType = reactionRepository.findByUserIdAndPostId(currentUserId, post.getId())
                    .map(Reaction::getReactionType)
                    .orElse(null);
        }

        return postMapper.toResponse(post, likesCount, commentsCount, reactedType);
    }

    @Override
    @Transactional
    @CacheEvict(value = "feeds", allEntries = true)
    public void deletePost(UUID postId, UUID currentUserId, boolean isAdmin) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        if (!isAdmin && !post.getAuthor().getId().equals(currentUserId)) {
            throw new AccessDeniedException("You are not authorized to delete this post");
        }

        postRepository.delete(post);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "feeds", key = "#sort + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + (#currentUserId != null ? #currentUserId : 'anonymous')")
    public Page<PostResponse> getFeed(String sort, UUID currentUserId, Pageable pageable) {
        Instant now = Instant.now();
        Page<Post> posts;

        if ("trending".equalsIgnoreCase(sort)) {
            posts = postRepository.findTrendingPosts(now, pageable);
        } else {
            // default to latest
            Pageable sortedByNewest = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("created_at").descending() // Using native/column sorting for consistency
            );
            // JPA fallback: or we can use spring pageable sorted
            Pageable jpaPageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("createdAt").descending()
            );
            posts = postRepository.findActivePublicPosts(now, jpaPageable);
        }

        return posts.map(post -> {
            long likesCount = reactionRepository.countByPostId(post.getId());
            long commentsCount = commentRepository.countByPostId(post.getId());
            ReactionType reactedType = null;
            if (currentUserId != null) {
                reactedType = reactionRepository.findByUserIdAndPostId(currentUserId, post.getId())
                        .map(Reaction::getReactionType)
                        .orElse(null);
            }
            return postMapper.toResponse(post, likesCount, commentsCount, reactedType);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getUserPosts(String username, UUID currentUserId, Pageable pageable) {
        Instant now = Instant.now();
        Pageable sortedByNewest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdAt").descending()
        );

        Page<Post> posts = postRepository.findActivePublicPostsByAuthor(username, now, sortedByNewest);

        return posts.map(post -> {
            long likesCount = reactionRepository.countByPostId(post.getId());
            long commentsCount = commentRepository.countByPostId(post.getId());
            ReactionType reactedType = null;
            if (currentUserId != null) {
                reactedType = reactionRepository.findByUserIdAndPostId(currentUserId, post.getId())
                        .map(Reaction::getReactionType)
                        .orElse(null);
            }
            return postMapper.toResponse(post, likesCount, commentsCount, reactedType);
        });
    }
}
