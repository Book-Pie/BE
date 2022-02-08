package com.bookpie.shop.service;

import com.bookpie.shop.config.ApiConfig;
import com.bookpie.shop.config.JwtTokenProvider;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.*;
import com.bookpie.shop.domain.enums.Grade;
import com.bookpie.shop.domain.enums.LoginType;
import com.bookpie.shop.repository.UserRepository;
import com.bookpie.shop.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserSevice {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private final JavaMailSender javaMailSender;
    private final ApiConfig apiConfig;

    @Value("${path.image.dev}")
    private String filePath;

    @Transactional
    public Long signup(UserCreateDto userCreateDto){
        userCreateDto.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        User user = User.createUser(userCreateDto);
        if (emailValidation(user.getEmail()) && nickNameValidation(user.getNickName())){
            return userRepository.save(user);}
        else {
            throw new IllegalArgumentException("이미 가입된 이메일 입니다.");
        }
    }


    public String login(LoginDto loginDto){
        User user = userRepository.findByEmailAllgrade(loginDto.getEmail())
                .orElseThrow(()->new UsernameNotFoundException("가입되지 않은 이메일입니다."));
        if(!passwordEncoder.matches(loginDto.getPassword(),user.getPassword())){
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }
        if(user.getGrade() == Grade.WITH_DRAW){
            throw new IllegalArgumentException("탈퇴한 회원입니다.");
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
        if(user.getLoginType() == LoginType.LOCAL) {
            user.deleteAccount(reason);
            return true;
        }
        return userRepository.remove(user);
    }

    public boolean checkPassword(Long id, String password){
        User user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return passwordEncoder.matches(password,user.getPassword());

    }

    @Transactional
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
        User user = userRepository.findByNameAndPhone(findUserDto.getName(), findUserDto.getPhone()).orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        //if (user.getName().equals(findUserDto.getName()) && user.getPhone().equals(findUserDto.getPhone())){
        return user.getUsername();
        //}else{
        //    throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
       // }
    }

    @Transactional
    public boolean findPassword(FindUserDto findUserDto) throws Exception{
        User user = userRepository.findByEmail(findUserDto.getEmail()).orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        if (user.getName().equals(findUserDto.getName()) &&
            user.getPhone().equals(findUserDto.getPhone())){
        return changePassword(user.getId(),findUserDto.getPassword());
        }else{
            throw new IllegalArgumentException("이름, 이메일, 휴대폰번호를 올바르게 입력해주세요.");
        }
    }

    public Long totalUser(){
        return userRepository.count();
    }

    // 이메일 인증코드로 확인
    public boolean emailCheck(String email, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, String> emailCode = new HashMap<>();
        final String FROM_ADDRESS = apiConfig.getAdminMail();
        final String TITLE = "북파이 회원가입 전 이메일 확인 메일입니다.";

        // 인증코드
        String code = createKey();
        emailCode.put(email, code);
        session.setAttribute("emailCode", emailCode);
        session.setMaxInactiveInterval(5*60);  // 세션 유지시간 5분

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.addRecipients(Message.RecipientType.TO, email);
            message.setSubject(TITLE);
            StringBuilder sb = new StringBuilder();
            sb.append("<div style='margin:100px;'>");
            sb.append("<h1> 안녕하세요 북파이입니다. </h1>");
            sb.append("<br>");
            sb.append("<p>아래 코드를 회원가입 창으로 돌아가 입력해 주세요.</p>");
            sb.append("<br>");
            sb.append("<div align='center' style='border:1px solid black; font-family:verdana';>");
            sb.append("<h3 style='color:blue;'>이메일 인증 코드입니다.</h3>");
            sb.append("<div style='font-size:130%'>");
            sb.append("CODE : <strong>");
            sb.append(code+"</strong></div><br>");
            sb.append("</div>");
            message.setText(sb.toString(), "utf-8", "html");
            message.setFrom(FROM_ADDRESS);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("인증코드 보내기 실패");
        }

        javaMailSender.send(message);
        return true;
    }
    // 이메일 코드 생성 메서드
    public static String createKey() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);

            switch (index){
                case 0:  // a~z
                    sb.append((char)((int)(random.nextInt(26))+97));
                    break;
                case 1:  // A~Z
                    sb.append((char)((int)(random.nextInt(26))+65));
                    break;
                case 2:  // 0~9
                    sb.append((int)(random.nextInt(10)));
                    break;
            }
        }
        return sb.toString();
    }
    // 코드 확인 메서드
    public Boolean emailCodeCheck(EmailDto dto, HttpServletRequest request) {
        HttpSession session = request.getSession();

        Map<String, String> map = new HashMap<>();
        if (session.getAttribute("emailCode") == null) {
            throw new IllegalArgumentException("이메일 인증코드를 재요청 해주세요");
        } else {
            map = (Map<String, String>) session.getAttribute("emailCode");
        }

        if (dto.getCode().equals(map.get(dto.getEmail()))) {
            session.removeAttribute("emailCode");
            return true;
        } else {
            throw new IllegalArgumentException("코드번호가 일치하지 않습니다.");
        }
    }

}
