package com.bookpie.shop.service;

import com.bookpie.shop.config.JwtTokenProvider;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.*;
import com.bookpie.shop.repository.UserRepository;
import com.bookpie.shop.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserSevice {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${path.image.dev}")
    private String filePath;

    @Transactional
    public Long signup(UserCreateDto userCreateDto){
        userCreateDto.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        User user = User.createUser(userCreateDto);
        if (emailValidation(user.getEmail()) && nickNameValidation(user.getNickName())){
            return userRepository.save(user);}
        else {
            throw new IllegalArgumentException("이미 가입된 아이디 입니다.");
        }
    }


    public String login(LoginDto loginDto){
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(()->new UsernameNotFoundException("가입되지 않은 아이디입니다."));
        log.debug(user.toString());
        if(!passwordEncoder.matches(loginDto.getPassword(),user.getPassword())){
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }
        return jwtTokenProvider.createToken(user.getEmail(),user.getRoles());
    }

    /*
    public boolean usernameValidation(String username){
        return userRepository.findByUsername(username).isEmpty();
    }

     */

    public boolean nickNameValidation(String nickName){
        return userRepository.findByNickName(nickName).isEmpty();
    }

    @Transactional
    public String updateNickname(Long id,String nickName){
        User user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        user.changeNickname(nickName);
        return user.getNickName();
    }

    public UserDetailDto getUserDetail(Long id){
        User user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return UserDetailDto.createUserDetailDto(user);

    }

    @Transactional
    public boolean deleteAccount(Long id,String reason){
        User user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        user.deleteAccount(reason);
        return true;
    }

    public boolean checkPassword(Long id, String password){
        User user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return passwordEncoder.matches(password,user.getPassword());

    }

    public boolean changePassword(Long id,String password){
        User user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        user.changePassword(passwordEncoder.encode(password));
        return true;
    }

    @Transactional
    public boolean updateUserInfo(Long id, UserUpdateDto userUpdateDto){
        User user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        user.update(userUpdateDto);
        return true;
    }

    @Transactional
    public String uploadImage(Long id,MultipartFile file) throws Exception {
        User user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        String fileName = FileUtil.save(filePath,file);
        user.changeImage(fileName);
        return fileName;

    }
    public boolean emailValidation(String email){
        return userRepository.findByEmail(email).isEmpty();
    }


    public String findId(FindUserDto findUserDto) throws Exception{
        User user = userRepository.findByEmail(findUserDto.getEmail()).orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        if (user.getName().equals(findUserDto.getName()) && user.getPhone().equals(findUserDto.getPhone())){
            return user.getUsername();
        }else{
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
    }

    @Transactional
    public boolean findPassword(FindUserDto findUserDto) throws Exception{
        User user = userRepository.findByEmail(findUserDto.getEmail()).orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        if (user.getEmail().equals(findUserDto.getEmail()) &&
            user.getName().equals(findUserDto.getName()) &&
            user.getPhone().equals(findUserDto.getPhone())){
            return changePassword(user.getId(),findUserDto.getPassword());
        }else{
            throw new IllegalArgumentException("이름, 이메일, 휴대폰번호를 올바르게 입력해주세요.");
        }
    }

    public Long totalUser(){
        return userRepository.count();
    }
}
