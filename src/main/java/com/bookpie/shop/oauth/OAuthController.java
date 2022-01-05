package com.bookpie.shop.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bookpie.shop.utils.ApiUtil.success;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/oauth")
public class OAuthController {

    private final OAuthService oAuthService;


    @GetMapping("/login/kakao/{accessToken}")
    public ResponseEntity kakaoLoginTest(@PathVariable("accessToken")String accessToken){
        return new ResponseEntity(success(oAuthService.kakaoLogin(accessToken)),HttpStatus.OK);
    }

    @GetMapping("/login/naver/{accessToken}")
    public ResponseEntity naverLogin(@PathVariable("accessToken")String accessToken){
        return new ResponseEntity(success(oAuthService.naverLogin(accessToken)),HttpStatus.OK);
    }




}

