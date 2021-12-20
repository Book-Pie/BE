package com.bookpie.shop.controller;

import com.bookpie.shop.domain.dto.LoginDto;
import com.bookpie.shop.domain.dto.UserCreateDto;
import com.bookpie.shop.service.UserSevice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {


    private final UserSevice userSevice;

    @PostMapping("signup")
    public Long join(@RequestBody UserCreateDto userCreateDto){
        return userSevice.signup(userCreateDto);
    }

    @PostMapping("login")
    public String login(@RequestBody LoginDto loginDto){
        return userSevice.login(loginDto);
    }


}
