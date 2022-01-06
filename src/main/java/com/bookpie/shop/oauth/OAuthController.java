package com.bookpie.shop.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @GetMapping("/login/naver")
    public ResponseEntity naverLogin(@RequestParam(value = "code") String code,
                                     @RequestParam(value = "state") String state){
        return new ResponseEntity(success(oAuthService.naverLogin(code,state)),HttpStatus.OK);
    }

    @GetMapping("/login/naver/test/{token}")
    public JSONObject getJson(@PathVariable("token") String token){
        JSONObject naverProfile = oAuthService.getNaverProfile(token);
        return naverProfile;
    }

}

