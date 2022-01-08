package com.bookpie.shop.oauth;

import com.bookpie.shop.config.JwtTokenProvider;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.enums.LoginType;
import com.bookpie.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${naver.url}")
    private String naverUrl;

    @Value("${naver.token}")
    private String tokenUrl;

    @Value("${naver.client-id}")
    private String naverClientId;

    @Value("${naver.client-secret}")
    private String naverClientSecret;

    @Value("${kakao.profile}")

    private String profileUrl;

    @Transactional
    public String kakaoLogin(String token){
        JSONObject profile = getKakaoProfile(token);
        Map<String,Object> kakaoAccount = (Map<String, Object>) profile.get("kakao_account");
        Map<String,Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
        String name = (String) kakaoProfile.get("nickname");
        String email = (String) kakaoAccount.get("email");
        User user = oauthSaveAndGet(email,name,LoginType.KAKAO);
        return jwtTokenProvider.createToken(user.getUsername(),user.getRoles());
    }

    @Transactional
    public String naverLogin(String code,String state){
        String token = getNaverToken(code,state);
        JSONObject naverProfile = getNaverProfile(token);
        Map<String,Object> response =(Map<String, Object>) naverProfile.get("response");
        String email = (String) response.get("email");
        String name = (String) response.get("nickname");
        User user = oauthSaveAndGet(email,name,LoginType.NAVER);
        return jwtTokenProvider.createToken(user.getEmail(),user.getRoles());
    }


    @Transactional
    public User oauthSaveAndGet(String eamil,String name,LoginType type){
        User user;
        Optional<User> optionalUser = userRepository.findByEmail(eamil);
        if(optionalUser.isPresent()){
            user = optionalUser.get();
            if(user.getLoginType()!= type) throw new IllegalArgumentException("로그인 형식이 잘못되었습니다.");
        }else{
            User nUser = User.oauthCreate(eamil,name,type);
            Long id = userRepository.save(nUser);
            user = userRepository.findById(id).get();
        }
        return user;
    }

    public String getNaverToken(String code,String state){
        String requstUrl = tokenUrl + "?grant_type=authorization_code&client_id="+naverClientId + "&client_secret="
                + naverClientSecret + "&code=" + code + "&state=" + state;
        RestTemplate restTemplate = new RestTemplate();
        log.debug(requstUrl);
        try {
            ResponseEntity<JSONObject> response = restTemplate.getForEntity(requstUrl, JSONObject.class);
            if(response.getStatusCode() != HttpStatus.OK) throw new IllegalArgumentException("네이버 인증 토큰을 받아올 수 없습니다.");
            JSONObject body = response.getBody();
            log.debug(response.toString());
            return (String) body.get("access_token");
        }catch (Exception e){
            throw new IllegalArgumentException("네이버 인증 토큰을 받아올 수 없습니다.");
        }
    }

    public JSONObject getNaverProfile(String token){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+token);
        HttpEntity<MultiValueMap<String,Object>> request = new HttpEntity<>(null,headers);
        try{
            ResponseEntity<JSONObject> response = restTemplate.exchange(naverUrl,HttpMethod.GET,request,JSONObject.class);
            return response.getBody();
        }catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException("네이버 사용자 정보를 불러올 수 없습니다.");
        }
    }

    public JSONObject getKakaoProfile(String token){
        String id = "";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","bearer "+token);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //headers.set("KakaoAk",clientId);

        MultiValueMap<String,Object> parameters = new LinkedMultiValueMap<>();
        //parameters.add("property_keys", "[\"id\"]");
        HttpEntity<MultiValueMap<String ,Object>> request = new HttpEntity(parameters,headers);
        try {
            ResponseEntity<JSONObject> response = restTemplate.postForEntity(profileUrl, request, JSONObject.class);
            log.debug(response.toString());
            return response.getBody();
        }catch (Exception e){
            e.printStackTrace();
            log.debug(e.getMessage());
            throw new IllegalArgumentException("카카오 사용자 정보를 불러올 수 없습니다.");

        }

    }
}
