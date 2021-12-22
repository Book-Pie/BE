package com.bookpie.shop.service;

import com.bookpie.shop.config.JwtTokenProvider;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.LoginDto;
import com.bookpie.shop.domain.dto.UserCreateDto;
import com.bookpie.shop.domain.dto.UserDetailDto;
import com.bookpie.shop.domain.dto.UserUpdateDto;
import com.bookpie.shop.repository.UserRepository;
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

    @Value("${path.image.deploy}")
    private String filePath;

    @Transactional
    public Long signup(UserCreateDto userCreateDto){
        userCreateDto.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        User user = User.createUser(userCreateDto);
        return userRepository.save(user);
    }


    public String login(LoginDto loginDto){
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(()->new IllegalArgumentException("가입되지 않은 ID 입니다."));

        if(!passwordEncoder.matches(loginDto.getPassword(),user.getPassword())){
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }
        return jwtTokenProvider.createToken(user.getUsername(),user.getRoles());
    }

    public boolean usernameValidation(String username){
        return userRepository.findByUsername(username).isEmpty();
    }

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
        if(file.isEmpty()){
            throw new FileUploadException("파일이 없습니다.");
        }
        String fileName = new StringBuilder()
                .append(new Date().getTime())
                .append(file.getOriginalFilename())
                .toString();
        try{
            Path path = Paths.get(filePath+fileName);
            Files.write(path,file.getBytes());
            User user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
            user.changeImage(fileName);
            return fileName;
        }catch (Exception e){
            throw new FileUploadException("파일 업로드 중 에러가 발생하였습니다.");
        }

    }

}
