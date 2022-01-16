package com.bookpie.shop.controller;

import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.UserReviewCreateDto;
import com.bookpie.shop.service.UserReviewService;
import com.bookpie.shop.utils.ApiUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.bookpie.shop.utils.ApiUtil.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userreview")
public class UserReviewController {
    private final UserReviewService userReviewService;

    @PostMapping("")
    public ResponseEntity upload(@RequestBody UserReviewCreateDto userReviewCreateDto){
        return new ResponseEntity(success(userReviewService.uploadUserReview(userReviewCreateDto,getCurrentUserId())), HttpStatus.OK);
    }

    private Long getCurrentUserId(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }
}
