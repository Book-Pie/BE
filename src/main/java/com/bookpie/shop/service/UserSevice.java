package com.bookpie.shop.service;

import com.bookpie.shop.config.JwtTokenProvider;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.LoginDto;
import com.bookpie.shop.domain.dto.UserCreateDto;
import com.bookpie.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserSevice {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Long signup(UserCreateDto userCreateDto){
        userCreateDto.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        User user = User.createUser(userCreateDto);
        return userRepository.save(user);
    }


    public String login(LoginDto loginDto){
        User user = userRepository.findByUsername(loginDto.getUserId())
                .orElseThrow(()->new IllegalArgumentException("가입되지 않은 ID 입니다."));

        if(!passwordEncoder.matches(loginDto.getPassword(),user.getPassword())){
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }
        return jwtTokenProvider.createToken(user.getEmail(),user.getRoles());
    }

    public boolean usernameValidation(String username){
        return userRepository.findByUsername(username).isEmpty();
    }

    public boolean nickNameValidation(String nickName){
        return userRepository.findByNickName(nickName).isEmpty();
    }
}
