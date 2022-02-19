package com.bookpie.shop.controller;

import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.follow.FollowingDto;
import com.bookpie.shop.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.bookpie.shop.utils.ApiUtil.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
public class FollowController {
    private final FollowService followService;

    @PostMapping("")
    public ResponseEntity following(@RequestBody FollowingDto followingDto) {
        return new ResponseEntity(success(followService.following(followingDto.getUserId(), getCurrentUserId())), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteFollow(@PathVariable Long userId) {
        return new ResponseEntity(success(followService.deleteFollow(userId, getCurrentUserId())), HttpStatus.OK);
    }

    // 내가 팔로잉 한 유저 리스트
    @GetMapping("/following/{userId}")
    public ResponseEntity myFollowing(@PathVariable Long userId) {
        Long currentUserId;
        try {
            currentUserId = getCurrentUserId();
        } catch (Exception e) {
            currentUserId = 0L;
        }
        return new ResponseEntity(success(followService.myFollowing(userId, currentUserId)), HttpStatus.OK);
    }

    // 나를 팔로우 한 유저 리스트
    @GetMapping("/follower/{userId}")
    public ResponseEntity myFollower(@PathVariable Long userId) {
        Long currentUserId;
        try {
            currentUserId = getCurrentUserId();
        } catch (Exception e) {
            currentUserId = 0L;
        }
        return new ResponseEntity(success(followService.myFollower(userId, currentUserId)), HttpStatus.OK);
    }

    // 현재 접속자가 해당 유저를 팔로우 했는지
    @GetMapping("/followCheck/{userId}")
    public ResponseEntity followCheck(@PathVariable Long userId) {
        Long currendUserId;
        try {
            currendUserId = getCurrentUserId();
        } catch (Exception e) {
            currendUserId = 0L;
        }
        return new ResponseEntity(success(followService.followCheck(userId, currendUserId)), HttpStatus.OK);
    }

    @GetMapping("/followNumber/{userId}")
    public ResponseEntity followNumber(@PathVariable Long userId) {
        return new ResponseEntity(success(followService.followNumber(userId)), HttpStatus.OK);
    }

    private Long getCurrentUserId(){
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user.getId();
        }catch (Exception e){
            throw new ClassCastException("토큰에서 사용자 정보를 불러오는데 실패하였습니다.");
        }
    }

}
