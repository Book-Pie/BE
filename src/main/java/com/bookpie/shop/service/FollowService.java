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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final HttpServletRequest request;


    public Boolean following(Long toUserId, Long currentUserId) {
        if (toUserId.equals(currentUserId)) throw new IllegalArgumentException("자신한테는 팔로우를 할 수 없습니다.");

        User fromUser = userRepository.findById(currentUserId)
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        User toUser = userRepository.findById(toUserId)
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (followCheck(toUserId)) throw new IllegalArgumentException("팔로우는 한 번만 할 수 있습니다.");

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

    public Boolean followCheck(Long userId) {
        Long currentUserId = 0L;
        // 로그인 유저가 있으면
        if (jwtTokenProvider.resolveToken(request) != null) {
            currentUserId = getCurrentUserId();
        } else {
            return false;
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        List<FollowingDto> followingList = myFollowing(currentUserId);
        for (FollowingDto dto : followingList) {
            if (dto.getUserId().equals(user.getId())) return true;
        }
        return false;
    }

    private Long getCurrentUserId(){
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user.getId();
        }catch (Exception e){
            throw new ClassCastException("토큰에서 사용자 정보를 불러오는데 실패하였습니다.");
        }
    }

    public JSONObject followNumber(Long userId) {
        JSONObject obj = new JSONObject();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        System.out.println("여기까지1");
        Long following = followRepository.followingNumber(userId);
        Long follower = followRepository.followerNumber(userId);
        System.out.println("여기까지2");
        obj.put("following", following);
        obj.put("follower", follower);


        return obj;
    }
}
