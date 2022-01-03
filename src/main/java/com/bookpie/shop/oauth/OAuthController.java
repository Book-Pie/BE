package com.bookpie.shop.oauth;

import com.bookpie.shop.utils.ApiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Map;

import static com.bookpie.shop.utils.ApiUtil.success;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/oauth")
public class OAuthController {

    private final OAuthService oAuthService;
    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-url}")
    private String redirectUrl;

    @Value("${kakao.login}")
    private String loginUrl;

    @Value("${kakao.profile}")
    private String profileUrl;

    @Value("${kakao.token}")
    private String tokenUrl;


    @GetMapping("/login/kakao")
    public String kakaoRedirect() throws Exception{
        String reqUrl = loginUrl+"?client_id="+clientId+"&redirect_uri="+redirectUrl+"&response_type=code";
        return reqUrl;
    }

    @GetMapping("/loginresult/kakao")
    public ResponseEntity kakaoLogin(HttpServletRequest request,HttpServletResponse response) throws Exception{
        String code = request.getParameter("code");
        String error = request.getParameter("error");
        if (error!= null){
            throw new Exception();
        }
        String accessToken = getAccessToken(code);
        JSONObject profile = getKakaoProfile(accessToken);
        return new ResponseEntity(success(oAuthService.kakaoLogin(profile)),HttpStatus.OK);
    }




    public String getAccessToken(String code) throws Exception{
        String accessToken = "";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String,Object> parameters = new LinkedMultiValueMap<>();
        parameters.set("grant_type","authorization_code");
        parameters.set("client_id",clientId);
        parameters.set("redirect_uri",redirectUrl);
        parameters.set("code",code);
        HttpEntity<MultiValueMap<String,Object>> request = new HttpEntity<>(parameters,headers);
        ResponseEntity<JSONObject> response = restTemplate.postForEntity(tokenUrl,request,JSONObject.class);
        accessToken = (String) response.getBody().get("access_token");
        log.debug(response.toString());
        return accessToken;
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
        ResponseEntity<JSONObject> response = restTemplate.postForEntity(profileUrl,request,JSONObject.class);
        return response.getBody();

    }


}

