package com.bookpie.shop.service;

import com.bookpie.shop.domain.Address;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.LoginDto;
import com.bookpie.shop.domain.dto.UserCreateDto;
import com.bookpie.shop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class UserSeviceTest {

    @Autowired UserSevice userSevice;
    @Autowired UserRepository userRepository;

    @Test
    UserCreateDto init() {
        UserCreateDto userCreateDto= new UserCreateDto();
        userCreateDto.setAddress(new Address("aaa","bbb","ccc"));
        userCreateDto.setEmail("test@gmail.com");
        userCreateDto.setName("kim");
        userCreateDto.setNickName("nick");
        userCreateDto.setPassword("1234");
        userCreateDto.setPhone("01049432618");
        return userCreateDto;
    }

    @Test
    public void signupTest() throws Exception{
        //given
        UserCreateDto userCreateDto = init();
        //when
        userSevice.signup(userCreateDto);
        User user = userRepository.findByEmail("test@gmail.com").get();
        //then
        assertEquals("kim",user.getName());
    }

    @Test
    public void loginSuccessTest() throws Exception{
        //given
        UserCreateDto userCreateDto = init();
        userSevice.signup(userCreateDto);
        //when
        LoginDto loginDto= new LoginDto();
        loginDto.setEmail("test@gmail.com");
        loginDto.setPassword("1234");
        //then
        String token = userSevice.login(loginDto);
        assertNotNull(token);

    }

    @Test
    public void loginFailTest() throws Exception{
        //given
        UserCreateDto userCreateDto = init();
        userSevice.signup(userCreateDto);
        //when
        LoginDto loginDto= new LoginDto();
        loginDto.setEmail("test@gmail.com");
        loginDto.setPassword("12345");
        //then
        assertThrows(IllegalArgumentException.class,()->userSevice.login(loginDto));
    }

    @Test
    public void validationTest() throws Exception{
        //given
        UserCreateDto userCreateDto = init();
        userSevice.signup(userCreateDto);
        //when
        boolean exist_id = userSevice.emailValidation("test@gmail.com");
        boolean empty_id = userSevice.emailValidation("test11@gmail.com");
        boolean exist_nick = userSevice.nickNameValidation("nick");
        boolean empty_nick = userSevice.nickNameValidation("nickkkk");
        //then

        assertEquals(false,exist_id);
        assertEquals(true,empty_id);
        assertEquals(false,exist_nick);
        assertEquals(true,empty_nick);
    }

}