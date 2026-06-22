package com.ideasnap.service;

import com.ideasnap.domain.Follow;
import com.ideasnap.domain.User;
import com.ideasnap.dto.FollowDto;
import com.ideasnap.exception.BadRequestException;
import com.ideasnap.exception.ResourceNotFoundException;
import com.ideasnap.mapper.UserMapper;
import com.ideasnap.repository.FollowRepository;
import com.ideasnap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public void follow(UUID followerId, String followingUsername) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("Follower not found"));

        User following = userRepository.findByUsername(followingUsername.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User to follow not found"));

        if (follower.getId().equals(following.getId())) {
            throw new BadRequestException("You cannot follow yourself");
        }

        boolean exists = followRepository.existsByFollowerIdAndFollowingId(follower.getId(), following.getId());
        if (exists) {
            throw new BadRequestException("You are already following " + followingUsername);
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        followRepository.save(follow);
    }

    @Override
    @Transactional
    public void unfollow(UUID followerId, String followingUsername) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("Follower not found"));

        User following = userRepository.findByUsername(followingUsername.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User to unfollow not found"));

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new BadRequestException("You are not following " + followingUsername));

        followRepository.delete(follow);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FollowDto> getFollowers(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Page<Follow> follows = followRepository.findByFollowingId(user.getId(), pageable);
        return follows.map(follow -> userMapper.toFollowDto(follow.getFollower()));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FollowDto> getFollowing(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Page<Follow> follows = followRepository.findByFollowerId(user.getId(), pageable);
        return follows.map(follow -> userMapper.toFollowDto(follow.getFollowing()));
    }
}
