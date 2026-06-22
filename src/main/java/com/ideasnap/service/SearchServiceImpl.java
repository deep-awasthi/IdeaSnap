package com.ideasnap.service;

import com.ideasnap.domain.Post;
import com.ideasnap.domain.Reaction;
import com.ideasnap.domain.ReactionType;
import com.ideasnap.dto.PostResponse;
import com.ideasnap.mapper.PostMapper;
import com.ideasnap.repository.CommentRepository;
import com.ideasnap.repository.PostRepository;
import com.ideasnap.repository.ReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostMapper postMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> searchPosts(String query, String tag, String username, UUID currentUserId, Pageable pageable) {
        Instant now = Instant.now();
        Page<Post> posts;

        if (tag != null && !tag.trim().isEmpty()) {
            posts = postRepository.searchByTag(tag.trim(), now, pageable);
        } else if (username != null && !username.trim().isEmpty()) {
            posts = postRepository.searchByUsername(username.trim(), now, pageable);
        } else if (query != null && !query.trim().isEmpty()) {
            posts = postRepository.searchByTitle(query.trim(), now, pageable);
        } else {
            return Page.empty(pageable);
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
}
