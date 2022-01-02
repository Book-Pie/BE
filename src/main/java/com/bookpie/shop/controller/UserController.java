package com.bookpie.shop.controller;

import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.FindUserDto;
import com.bookpie.shop.domain.dto.LoginDto;
import com.bookpie.shop.domain.dto.UserCreateDto;
import com.bookpie.shop.domain.dto.UserUpdateDto;
import com.bookpie.shop.service.UserSevice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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
    public ResponseEntity join(@Valid @RequestBody UserCreateDto userCreateDto){
        return new ResponseEntity(success(userSevice.signup(userCreateDto)),HttpStatus.OK);
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto){
        log.debug("login");
        return new ResponseEntity<>(success(userSevice.login(loginDto)),HttpStatus.OK);
    }

    /*
    //회원 아이디 중복 검사
    @GetMapping("/username/{username}")
    public ResponseEntity userIdValidation(@PathVariable("username") String username){
        return new ResponseEntity(success(userSevice.usernameValidation(username)),HttpStatus.OK);
    }

     */

    //회원 닉네임 중복 검사
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity nickNameValidation(@PathVariable("nickname") String nickname){
        return new ResponseEntity(success(userSevice.nickNameValidation(nickname)),HttpStatus.OK);
    }

    //회원 이메일 중복검사
    @GetMapping("/email/{email}")
    public ResponseEntity emailValidation(@PathVariable("email") String email){
        return new ResponseEntity(success(userSevice.emailValidation(email)),HttpStatus.OK);
    }

    //닉네임 변경
    @PutMapping("nickname/{nickname}")
    public ResponseEntity nickNameUpdate(@PathVariable("nickname") String nickname){
        if (userSevice.nickNameValidation(nickname)) {
            return new ResponseEntity(success(userSevice.updateNickname(getCurrentUserId(),nickname)),HttpStatus.OK);
        }else{
            return new ResponseEntity(error("이미 사용중인 닉네임 입니다.", HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
        }
    }

    //내 정보 확인
    @GetMapping("/me")
    public ResponseEntity getMyInfo(){
        return new ResponseEntity(success(userSevice.getUserDetail(getCurrentUserId())),HttpStatus.OK);
    }

    //회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity withDraw(@RequestBody String reason){
        return new ResponseEntity(success(userSevice.deleteAccount(getCurrentUserId(),reason)),HttpStatus.OK);
    }

    //회원 정보 수정
    @PutMapping("/me")
    public ResponseEntity updateInfo(@RequestBody UserUpdateDto userUpdateDto){
        return new ResponseEntity(success(userSevice.updateUserInfo(getCurrentUserId(),userUpdateDto)),HttpStatus.OK);
    }

    //회원 정보 확인
    @GetMapping("{id}")
    public ResponseEntity getUserInfo(@PathVariable("id") Long id){
        return new ResponseEntity(success(userSevice.getUserDetail(id)),HttpStatus.OK);
    }

    //비밀번호 확인
    @PostMapping("/password")
    public ResponseEntity checkPassword(@RequestBody Map<String,String> request){
        return new ResponseEntity(success(userSevice.checkPassword(getCurrentUserId(),request.get("password"))),HttpStatus.OK);
    }

    //비밀번호 변경
    @PutMapping("/password")
    public ResponseEntity changePassword(@RequestBody Map<String,String> request){
        return new ResponseEntity(success(userSevice.changePassword(getCurrentUserId(),request.get("password"))),HttpStatus.OK);
    }
    private Long getCurrentUserId(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }

    @PostMapping("/image")
    public ResponseEntity uploadUserImage(@RequestParam("image")MultipartFile file) throws Exception{
        return new ResponseEntity(success(userSevice.uploadImage(getCurrentUserId(),file)),HttpStatus.OK);
    }

    //아이디 찾기
    @PostMapping("/find/id")
    public ResponseEntity findId(@RequestBody FindUserDto findUserDto) throws Exception{
        return new ResponseEntity(success(userSevice.findId(findUserDto)),HttpStatus.OK);
    }

    //비밀번호 찾기
    @PostMapping("/find/password")
    public ResponseEntity findPassword(@RequestBody FindUserDto findUserDto) throws Exception{
        return new ResponseEntity(success(userSevice.findPassword(findUserDto)),HttpStatus.OK);
    }

    //전체 회원 수
    @GetMapping("/total")
    public ResponseEntity getTotalUser(){
        return new ResponseEntity(success(userSevice.totalUser()),HttpStatus.OK);
    }

}
