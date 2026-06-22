package com.ideasnap.mapper;

import com.ideasnap.domain.Post;
import com.ideasnap.domain.ReactionType;
import com.ideasnap.dto.PostResponse;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public PostResponse toResponse(Post post, long likesCount, long commentsCount, ReactionType reactedType) {
        if (post == null) {
            return null;
        }
        return PostResponse.builder()
                .id(post.getId())
                .authorId(post.getAuthor().getId())
                .authorUsername(post.getAuthor().getUsername())
                .title(post.getTitle())
                .content(post.getContent())
                .tags(post.getTags() != null ? new java.util.HashSet<>(post.getTags()) : new java.util.HashSet<>())
                .visibility(post.getVisibility())
                .createdAt(post.getCreatedAt())
                .expiresAt(post.getExpiresAt())
                .likesCount(likesCount)
                .commentsCount(commentsCount)
                .reactedType(reactedType)
                .build();
    }
}
