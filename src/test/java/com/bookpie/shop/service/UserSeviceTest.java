package com.bookpie.shop.service;

import com.bookpie.shop.config.JwtTokenProvider;
import com.bookpie.shop.domain.Point;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.dto.LoginDto;
import com.bookpie.shop.dto.UserCreateDto;
import com.bookpie.shop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSeviceTest {

    @InjectMocks
    UserSevice userSevice;

    @Spy
    BCryptPasswordEncoder encoder;

    @Mock
    UserRepository userRepository;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    User user(Long id){
        return User.builder()
                .id(id)
                .name("name"+ id)
                .email("email"+id+"@gmail.com")
                .point(Point.createDefaultPoint())
                .nickName("nick"+id)
                .password(encoder.encode("password"+id)).build();

    }
    @Test
    public void signupSuccessTest() throws Exception{
        //given
        UserCreateDto dto = new UserCreateDto();
        dto.setEmail("email@gmail.com");
        dto.setName("name");
        dto.setPassword("1234");

        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(null));
        when(userRepository.findByNickName(any())).thenReturn(Optional.ofNullable(null));

        //when
        userSevice.signup(dto);

        //then
        verify(userRepository).save(any());

    }
    @Test
    public void signupFailTest() throws Exception{
        //given
        User user = user(1l);
        UserCreateDto dto = new UserCreateDto();
        dto.setNickName("asd123");
        dto.setName("name");
        dto.setPassword("123123");
        dto.setEmail("test@gmail.com");
        //when
        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(user));
        //when(userRepository.findByNickName(any())).thenReturn(Optional.ofNullable(user));

        //then
        assertThrows(IllegalArgumentException.class,()->userSevice.signup(dto));

    }

    @Test
    public void loginSuccessTest() throws Exception{
        //given
        User user = user(1l);
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(user.getEmail());
        loginDto.setPassword("password1");

        //when
        when(userRepository.findByEmailAllgrade(any())).thenReturn(Optional.ofNullable(user));
        when(jwtTokenProvider.createToken(any(),any())).thenReturn("token");

        String token = userSevice.login(loginDto);
        //then
        assertEquals("token",token);


    }

    @Test
    public void loginFailTest() throws Exception{
        //given
        User user = user(1l);
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(user.getEmail());
        loginDto.setPassword("password1234");

        //when
        when(userRepository.findByEmailAllgrade(any())).thenReturn(Optional.ofNullable(user));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userSevice.login(loginDto));

        //then
        assertEquals("잘못된 비밀번호 입니다.",exception.getMessage());
    }

    @Test
    public void validationTest() throws Exception{
        //given
        User user = user(1l);
        String validEmail = "validEmail@gmail.com";
        String nonvalidEmail = "nonvalidEmail@gmail.com";
        String validNickname = "validNickname";
        String nonvalidNickname = "nonvaliNickname";
        when(userRepository.findByNickName(validNickname)).thenReturn(Optional.ofNullable(null));
        when(userRepository.findByNickName(nonvalidNickname)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findByEmail(validEmail)).thenReturn(Optional.ofNullable(null));
        when(userRepository.findByEmail(nonvalidEmail)).thenReturn(Optional.ofNullable(user));

        //when
        boolean ev = userSevice.emailValidation(validEmail);
        boolean env = userSevice.emailValidation(nonvalidEmail);
        boolean nv = userSevice.nickNameValidation(validNickname);
        boolean nnv = userSevice.nickNameValidation(nonvalidNickname);

        //then
        assertEquals(ev,true);
        assertEquals(env,false);
        assertEquals(nv,true);
        assertEquals(nnv,false);
    }

}