package com.ideasnap.service;

import com.ideasnap.domain.Post;
import com.ideasnap.domain.Reaction;
import com.ideasnap.domain.ReactionType;
import com.ideasnap.domain.User;
import com.ideasnap.exception.ResourceNotFoundException;
import com.ideasnap.repository.PostRepository;
import com.ideasnap.repository.ReactionRepository;
import com.ideasnap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReactionServiceImpl implements ReactionService {

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    @Transactional
    @CacheEvict(value = "feeds", allEntries = true)
    public void reactToPost(UUID userId, UUID postId, ReactionType type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (post.isExpired()) {
            throw new ResourceNotFoundException("Post has expired");
        }

        Optional<Reaction> existingReaction = reactionRepository.findByUserAndPost(user, post);

        if (existingReaction.isPresent()) {
            Reaction reaction = existingReaction.get();
            reaction.setReactionType(type);
            reactionRepository.save(reaction);
        } else {
            Reaction reaction = Reaction.builder()
                    .user(user)
                    .post(post)
                    .reactionType(type)
                    .build();
            reactionRepository.save(reaction);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "feeds", allEntries = true)
    public void removeReaction(UUID userId, UUID postId) {
        Reaction reaction = reactionRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new ResourceNotFoundException("Reaction not found"));
        reactionRepository.delete(reaction);
    }
}
