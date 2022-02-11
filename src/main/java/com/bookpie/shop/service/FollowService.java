package com.bookpie.shop.service;

import com.bookpie.shop.domain.Follow;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.follow.FollowerDto;
import com.bookpie.shop.domain.dto.follow.FollowingDto;
import com.bookpie.shop.repository.FollowRepository;
import com.bookpie.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public Boolean following(Long toUserId, Long currentUserId) {
        User fromUser = userRepository.findById(currentUserId)
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        User toUser = userRepository.findById(toUserId)
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        Follow follow = new Follow(fromUser, toUser);
        followRepository.save(follow);

        return true;
    }

    public Boolean deleteFollow(Long userId, Long currentUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        Follow follow = followRepository.findFollow(currentUserId, userId)
                .orElseThrow(() -> new IllegalArgumentException("두 사용자는 팔로잉 관계가 아닙니다."));

        followRepository.delete(follow);
        return true;
    }

    public List<FollowingDto> myFollowing(Long currentUserId) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        List<Follow> follows = followRepository.myFollowing(currentUserId);
        return follows.stream().map(follow -> FollowingDto.createDto(follow)).collect(Collectors.toList());
    }

    public List<FollowerDto> myFollower(Long currentUserId) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        List<Follow> follows = followRepository.myFollower(currentUserId);
        return follows.stream().map(follow -> FollowerDto.createDto(follow)).collect(Collectors.toList());
    }
}
