package com.bookpie.shop.repository;

import com.bookpie.shop.domain.Address;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.UserCreateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class UserRepositoryTest {

    @Autowired UserRepository userRepository;

    @Test
    void init() {
        UserCreateDto userCreateDto= new UserCreateDto();
        userCreateDto.setAddress(new Address("aaa","bbb","ccc"));
        userCreateDto.setUsername("test");
        userCreateDto.setEmail("test@gmail.com");
        userCreateDto.setName("kim");
        userCreateDto.setNickName("nick");
        userCreateDto.setPassword("1234");
        userCreateDto.setPhone("01049432618");
        userRepository.save(User.createUser(userCreateDto));
    }

    @Test
    void findByNickName() {
        init();
        User users = userRepository.findByNickName("nick").orElseThrow(()->new IllegalArgumentException("이미 사용중인 닉네임"));
        assertEquals("nick",users.getNickName());
    }

    @Test
    void findByUsername() {
        init();
        User user = userRepository.findByUsername("test").orElseThrow(()-> new IllegalArgumentException("이미 사용중인 아이디"));
        assertEquals("kim",user.getName());
    }
}