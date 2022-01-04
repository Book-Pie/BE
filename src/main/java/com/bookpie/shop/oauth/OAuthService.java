package com.bookpie.shop.oauth;

import com.bookpie.shop.config.JwtTokenProvider;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.enums.LoginType;
import com.bookpie.shop.repository.UserRepository;
import com.bookpie.shop.service.UserSevice;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserSevice userSevice;
    @Transactional
    public String kakaoLogin(JSONObject profile){
        Map<String,Object> kakaoAccount = (Map<String, Object>) profile.get("kakao_account");
        Map<String,Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
        String name = (String) kakaoProfile.get("nickname");
        String email = (String) kakaoAccount.get("email");
        Optional<User> optionalUser= userRepository.findByEmail(email);
        User user;
        if (optionalUser.isEmpty()){
            User nUser = User.oauthCreate(email,name, LoginType.KAKAO);
            
            if (!userSevice.emailValidation(email)) throw new IllegalArgumentException("이미 가입된 이메일 입니다.");

            Long id = userRepository.save(nUser);
            user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        }else{
            user = optionalUser.get();
            if (user.getLoginType() != LoginType.KAKAO){
                throw new UsernameNotFoundException("로그인 형식이 잘못되었습니다.");
            }
        }
        return jwtTokenProvider.createToken(user.getUsername(),user.getRoles());
    }
}
