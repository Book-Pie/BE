package com.bookpie.shop.controller;

import com.bookpie.shop.domain.dto.LoginDto;
import com.bookpie.shop.domain.dto.UserCreateDto;
import com.bookpie.shop.service.UserSevice;
import com.bookpie.shop.utils.ApiUtil;
import com.bookpie.shop.utils.ApiUtil.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.bookpie.shop.utils.ApiUtil.success;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {


    private final UserSevice userSevice;

    @PostMapping("signup")
    public ApiResult join(@RequestBody UserCreateDto userCreateDto){
        return success(userSevice.signup(userCreateDto));
    }

    @PostMapping("login")
    public ApiResult login(@RequestBody LoginDto loginDto){
        return success(userSevice.login(loginDto));
    }

    @GetMapping("/userid/{userId}")
    public ApiResult userIdValidation(@PathVariable("userId") String userId){
        return success(userSevice.usernameValidation(userId));
    }

    @GetMapping("/nickname/{nickname}")
    public ApiResult nickNameValidation(@PathVariable("nickname") String nickname){
        return success(userSevice.nickNameValidation(nickname));
    }



}
