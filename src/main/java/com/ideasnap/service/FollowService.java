package com.ideasnap.service;

import com.ideasnap.dto.FollowDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface FollowService {
    void follow(UUID followerId, String followingUsername);
    void unfollow(UUID followerId, String followingUsername);
    Page<FollowDto> getFollowers(String username, Pageable pageable);
    Page<FollowDto> getFollowing(String username, Pageable pageable);
}
