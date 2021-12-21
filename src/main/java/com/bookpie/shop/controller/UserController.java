package com.bookpie.shop.controller;

import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.LoginDto;
import com.bookpie.shop.domain.dto.UserCreateDto;
import com.bookpie.shop.service.UserSevice;
import com.bookpie.shop.utils.ApiUtil.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.bookpie.shop.utils.ApiUtil.error;
import static com.bookpie.shop.utils.ApiUtil.success;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {


    private final UserSevice userSevice;

    @PostMapping("/signup")
    public ApiResult join(@RequestBody UserCreateDto userCreateDto){
        return success(userSevice.signup(userCreateDto));
    }

    @PostMapping("/login")
    public ApiResult login(@RequestBody LoginDto loginDto){
        return success(userSevice.login(loginDto));
    }

    @GetMapping("/username/{username}")
    public ApiResult userIdValidation(@PathVariable("username") String username){
        return success(userSevice.usernameValidation(username));
    }

    @GetMapping("/nickname/{nickname}")
    public ApiResult nickNameValidation(@PathVariable("nickname") String nickname){
        return success(userSevice.nickNameValidation(nickname));
    }

    @PutMapping("nickname/{nickname}")
    public ApiResult nickNameUpdate(@PathVariable("nickname") String nickname){
        if (userSevice.nickNameValidation(nickname)) {
            return success(userSevice.updateNickname(getCurrentUserId(),nickname));
        }else{
            return error("이미 사용중인 닉네임 입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/me")
    public ApiResult getMyInfo(){
        return success(userSevice.getUserDetail(getCurrentUserId()));
    }

    @DeleteMapping("/me")
    public ApiResult withDraw(){
        return success(userSevice.deleteAccount(getCurrentUserId()));
    }

    private Long getCurrentUserId(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }

}
