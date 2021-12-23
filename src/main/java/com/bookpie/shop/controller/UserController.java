package com.bookpie.shop.controller;

import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.FindUserDto;
import com.bookpie.shop.domain.dto.LoginDto;
import com.bookpie.shop.domain.dto.UserCreateDto;
import com.bookpie.shop.domain.dto.UserUpdateDto;
import com.bookpie.shop.service.UserSevice;
import com.bookpie.shop.utils.ApiUtil;
import com.bookpie.shop.utils.ApiUtil.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static com.bookpie.shop.utils.ApiUtil.error;
import static com.bookpie.shop.utils.ApiUtil.success;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {


    private final UserSevice userSevice;

    // 회원 가입
    @PostMapping("/signup")
    public ApiResult join(@RequestBody UserCreateDto userCreateDto){
        return success(userSevice.signup(userCreateDto));
    }

    //로그인
    @PostMapping("/login")
    public ApiResult login(@RequestBody LoginDto loginDto){
        log.debug("login");
        return success(userSevice.login(loginDto));
    }

    //회원 아이디 중복 검사
    @GetMapping("/username/{username}")
    public ApiResult userIdValidation(@PathVariable("username") String username){
        return success(userSevice.usernameValidation(username));
    }

    //회원 닉네임 중복 검사
    @GetMapping("/nickname/{nickname}")
    public ApiResult nickNameValidation(@PathVariable("nickname") String nickname){
        return success(userSevice.nickNameValidation(nickname));
    }

    //닉네임 변경
    @PutMapping("nickname/{nickname}")
    public ApiResult nickNameUpdate(@PathVariable("nickname") String nickname){
        if (userSevice.nickNameValidation(nickname)) {
            return success(userSevice.updateNickname(getCurrentUserId(),nickname));
        }else{
            return error("이미 사용중인 닉네임 입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    //내 정보 확인
    @GetMapping("/me")
    public ApiResult getMyInfo(){
        return success(userSevice.getUserDetail(getCurrentUserId()));
    }

    //회원 탈퇴
    @DeleteMapping("/me")
    public ApiResult withDraw(@RequestBody String reason){
        return success(userSevice.deleteAccount(getCurrentUserId(),reason));
    }

    //회원 정보 수정
    @PutMapping("/me")
    public ApiResult updateInfo(@RequestBody UserUpdateDto userUpdateDto){
        return success(userSevice.updateUserInfo(getCurrentUserId(),userUpdateDto));
    }

    //회원 정보 확인
    @GetMapping("{id}")
    public ApiResult getUserInfo(@PathVariable("id") Long id){
        return success(userSevice.getUserDetail(id));
    }

    //비밀번호 확인
    @PostMapping("/password")
    public ApiResult checkPassword(@RequestBody Map<String,String> request){
        return success(userSevice.checkPassword(getCurrentUserId(),request.get("password")));
    }

    //비밀번호 변경
    @PutMapping("/password")
    public ApiResult changePassword(@RequestBody Map<String,String> request){
        return success(userSevice.changePassword(getCurrentUserId(),request.get("password")));
    }
    private Long getCurrentUserId(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }

    @PostMapping("/image")
    public ApiResult uploadUserImage(@RequestParam("image")MultipartFile file) throws Exception{
        return success(userSevice.uploadImage(getCurrentUserId(),file));
    }

    //아이디 찾기
    @PostMapping("/find/id")
    public ApiResult findId(@RequestBody FindUserDto findUserDto) throws Exception{
        return success(userSevice.findId(findUserDto));
    }

    //비밀번호 찾기
    @PostMapping("/find/password")
    public ApiResult findPassword(@RequestBody FindUserDto findUserDto) throws Exception{
        return success(userSevice.findPassword(findUserDto));
    }


}
