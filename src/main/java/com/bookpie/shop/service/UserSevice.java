package com.bookpie.shop.service;

import com.bookpie.shop.config.ApiConfig;
import com.bookpie.shop.config.JwtTokenProvider;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.dto.*;
import com.bookpie.shop.enums.Grade;
import com.bookpie.shop.enums.LoginType;
import com.bookpie.shop.repository.UserRepository;
import com.bookpie.shop.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.util.Random;
import java.util.concurrent.TimeUnit;


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
    private final HttpSession session;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Value("${path.image.dev}")
    private String filePath;

    @Transactional
    public Long signup(UserCreateDto userCreateDto){
        userCreateDto.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        User user = User.createUser(userCreateDto);
        if (emailValidation(user.getEmail()) && nickNameValidation(user.getNickName())){
            return userRepository.save(user);}
        else {
            throw new IllegalArgumentException("?????? ????????? ????????? ?????????.");
        }
    }


    public String login(LoginDto loginDto){
        User user = userRepository.findByEmailAllgrade(loginDto.getEmail())
                .orElseThrow(()->new UsernameNotFoundException("???????????? ?????? ??????????????????."));
        if(!passwordEncoder.matches(loginDto.getPassword(),user.getPassword())){
            throw new IllegalArgumentException("????????? ???????????? ?????????.");
        }
        if(user.getGrade() == Grade.WITH_DRAW){
            throw new IllegalArgumentException("????????? ???????????????.");
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
        User user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("???????????? ?????? ??? ????????????."));
        user.changeNickname(nickName);
        return user.getNickName();
    }

    public UserDetailDto getUserDetail(Long id){
        User user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("???????????? ?????? ??? ????????????."));
        return UserDetailDto.createUserDetailDto(user);

    }

    @Transactional
    public boolean deleteAccount(Long id,String reason){
        User user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("???????????? ?????? ??? ????????????."));
        if(user.getLoginType() == LoginType.LOCAL) {
            user.deleteAccount(reason);
            return true;
        }
        return userRepository.remove(user);
    }

    public boolean checkPassword(Long id, String password){
        User user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("???????????? ?????? ??? ????????????."));
        return passwordEncoder.matches(password,user.getPassword());

    }

    @Transactional
    public boolean changePassword(Long id,String password){
        User user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("???????????? ?????? ??? ????????????."));
        user.changePassword(passwordEncoder.encode(password));
        return true;
    }

    @Transactional
    public boolean updateUserInfo(Long id, UserUpdateDto userUpdateDto){
        User user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("???????????? ?????? ??? ????????????."));
        user.update(userUpdateDto);
        return true;
    }

    @Transactional
    public String uploadImage(Long id,MultipartFile file) throws Exception {
        User user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("???????????? ?????? ??? ????????????."));
        String fileName = FileUtil.save(filePath,file);
        user.changeImage(fileName);
        return fileName;

    }
    public boolean emailValidation(String email){
        return userRepository.findByEmail(email).isEmpty();
    }


    public String findId(FindUserDto findUserDto) throws Exception{
        User user = userRepository.findByNameAndPhone(findUserDto.getName(), findUserDto.getPhone()).orElseThrow(()->new UsernameNotFoundException("???????????? ?????? ??? ????????????."));
        //if (user.getName().equals(findUserDto.getName()) && user.getPhone().equals(findUserDto.getPhone())){
        return user.getUsername();
        //}else{
        //    throw new UsernameNotFoundException("???????????? ?????? ??? ????????????.");
       // }
    }

    @Transactional
    public boolean findPassword(FindUserDto findUserDto) throws Exception{
        User user = userRepository.findByEmail(findUserDto.getEmail()).orElseThrow(()->new UsernameNotFoundException("???????????? ?????? ??? ????????????."));
        if (user.getName().equals(findUserDto.getName()) &&
            user.getPhone().equals(findUserDto.getPhone())){
        return changePassword(user.getId(),findUserDto.getPassword());
        }else{
            throw new IllegalArgumentException("??????, ?????????, ?????????????????? ???????????? ??????????????????.");
        }
    }

    @Cacheable(cacheNames = "totalUser")
    public Long totalUser(){
        return userRepository.count();
    }

    // ????????? ??????????????? ??????
    public boolean emailCheck(String email) {
        final String FROM_ADDRESS = apiConfig.getAdminMail();
        final String TITLE = "????????? ???????????? ??? ????????? ?????? ???????????????.";

        // ????????????
        String code = createKey();
        final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(email, code);
        redisTemplate.expire(email, 60*5, TimeUnit.SECONDS);

        final String result = valueOperations.get(email);
        log.info("redis ????????? ??? : " + result);
        log.info("?????? ?????? : " + redisTemplate.getExpire(email));

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.addRecipients(Message.RecipientType.TO, email);
            message.setSubject(TITLE);
            StringBuilder sb = new StringBuilder();
            sb.append("<div style='margin:100px;'>");
            sb.append("<h1> ??????????????? ??????????????????. </h1>");
            sb.append("<br>");
            sb.append("<p>?????? ????????? ???????????? ????????? ????????? ????????? ?????????.</p>");
            sb.append("<br>");
            sb.append("<div align='center' style='border:1px solid black; font-family:verdana';>");
            sb.append("<h3 style='color:blue;'>????????? ?????? ???????????????.</h3>");
            sb.append("<div style='font-size:130%'>");
            sb.append("CODE : <strong>");
            sb.append(code+"</strong></div><br>");
            sb.append("</div>");
            message.setText(sb.toString(), "utf-8", "html");
            message.setFrom(FROM_ADDRESS);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("???????????? ????????? ??????");
        }

        javaMailSender.send(message);
        return true;
    }
    // ????????? ?????? ?????? ?????????
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
    // ?????? ?????? ?????????
    public Boolean emailCodeCheck(EmailDto dto) {
        final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String code = valueOperations.get(dto.getEmail());

        if (code.equals(dto.getCode())) return true;
        else throw new IllegalArgumentException("????????? ???????????? ????????????.");
    }

}
