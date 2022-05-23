package com.bookpie.shop.controller;

import com.bookpie.shop.domain.User;
import com.bookpie.shop.dto.UserReviewCreateDto;
import com.bookpie.shop.dto.UserReviewUpdateDto;
import com.bookpie.shop.service.UserReviewService;
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

    @PutMapping("")
    public ResponseEntity update(@RequestBody UserReviewUpdateDto userReviewUpdateDto){
        return new ResponseEntity(success(userReviewService.updateUserReview(userReviewUpdateDto,getCurrentUserId())),HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id){
        return new ResponseEntity(success(userReviewService.deleteUserReview(id,getCurrentUserId())),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity findByUserReviews(@PathVariable("id")Long userId,
                                            @RequestParam(value = "page",required = false,defaultValue = "1")int page,
                                            @RequestParam(value = "limit",required = false,defaultValue = "5")int limit,
                                            @RequestParam(value = "pageCount",required = false,defaultValue = "0")Long pageCount){
        return new ResponseEntity(success(userReviewService.getUserReviewsByReader(userId,page,limit)),HttpStatus.OK);
    }
    @GetMapping("/me")
    public ResponseEntity findMyUserReview(@RequestParam(value = "page",required = false,defaultValue = "1")int page,
                                           @RequestParam(value = "limit",required = false,defaultValue = "5")int limit,
                                           @RequestParam(value = "pageCount",required = false,defaultValue = "0")Long pageCount){
        return new ResponseEntity(success(userReviewService.getUserReviewsByWriter(getCurrentUserId(),page,limit)),HttpStatus.OK);
    }

    @GetMapping("/to-me")
    public ResponseEntity findUserReviewToMe(@RequestParam(value = "page",required = false,defaultValue = "1")int page,
                                             @RequestParam(value = "limit",required = false,defaultValue = "5")int limit,
                                             @RequestParam(value = "pageCount",required = false,defaultValue = "0")Long pageCount){
        return new ResponseEntity(success(userReviewService.getUserReviewsByReader(getCurrentUserId(),page,limit)),HttpStatus.OK);
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
