package com.bookpie.shop.service;

import com.bookpie.shop.config.JwtTokenProvider;
import com.bookpie.shop.domain.Follow;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.follow.FollowerDto;
import com.bookpie.shop.domain.dto.follow.FollowingDto;
import com.bookpie.shop.repository.FollowRepository;
import com.bookpie.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
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
        if (toUserId.equals(currentUserId)) throw new IllegalArgumentException("자신한테는 팔로우를 할 수 없습니다.");

        User fromUser = userRepository.findById(currentUserId)
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        User toUser = userRepository.findById(toUserId)
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (followCheck(toUserId, currentUserId)) throw new IllegalArgumentException("팔로우는 한 번만 할 수 있습니다.");

        Follow follow = new Follow(fromUser, toUser);
        fromUser.getFollowings().add(follow);
        toUser.getFollowers().add(follow);

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

    // 내가 팔로우 한 유저 리스트
    public List<FollowingDto> myFollowing(Long userId, Long currentUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        List<Follow> follows = followRepository.myFollowing(userId);
        return follows.stream().map(follow -> FollowingDto.createDto(follow)).collect(Collectors.toList());
    }

    // 나를 팔로우 한 유저 리스트
    public List<FollowerDto> myFollower(Long userId, Long currentUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        List<Follow> followers = followRepository.myFollower(userId);

        return followers.stream().map(follow-> FollowerDto.createDto(follow)).collect(Collectors.toList());
    }

    public Boolean followCheck(Long userId, Long currentUserId) {
        if (currentUserId.equals(0L)) return false;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        List<Follow> follows = followRepository.myFollowing(currentUserId);
        List<FollowingDto> followingList = follows.stream().map(follow -> FollowingDto.createDto(follow))
                .collect(Collectors.toList());
        for (FollowingDto dto : followingList) {
            if (dto.getUserId().equals(user.getId())) return true;
        }
        return false;
    }

    public JSONObject followNumber(Long userId) {
        JSONObject obj = new JSONObject();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        Long following = followRepository.followingNumber(userId);
        Long follower = followRepository.followerNumber(userId);
        obj.put("following", following);
        obj.put("follower", follower);


        return obj;
    }
}
