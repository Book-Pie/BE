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
        List<User> users = userRepository.findByNickName("nick");
        assertEquals("nick",users.get(0).getNickName());
    }

    @Test
    void findByEmail() {
        init();
        User user = userRepository.findByEmail("test@gmail.com").get();
        assertEquals("kim",user.getName());
    }
}