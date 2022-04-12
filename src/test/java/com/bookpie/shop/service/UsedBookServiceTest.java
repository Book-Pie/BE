package com.bookpie.shop.service;

import com.bookpie.shop.domain.UsedBook;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.dto.UsedBookCreateDto;
import com.bookpie.shop.dto.UserCreateDto;
import com.bookpie.shop.repository.UsedBookRepository;
import com.bookpie.shop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class UsedBookServiceTest {
    @Autowired
    UsedBookService usedBookService;

    @Autowired
    UsedBookRepository usedBookRepository;

    @Autowired
    UserRepository userRepository;
    public User user(){
        UserCreateDto dto = new UserCreateDto();
        dto.setEmail("123");
        dto.setName("123");
        dto.setNickName("123123123");
        return User.createUser(dto);
    }

    public UsedBookCreateDto usedBook(){
        UsedBookCreateDto dto = new UsedBookCreateDto();
        dto.setContent("asasd");
        dto.setPrice(123);
        dto.setTitle("123123");
        dto.getTags().add("tag1");
        dto.getTags().add("tag2");
        return dto;
    }

    @Test
    public void UsedBookServiceTest() throws Exception{
        //given
        User user = user();
        Long id = userRepository.save(user);
        UsedBookCreateDto dto = usedBook();

        //when
        //then
        List<UsedBook> users =usedBookRepository.findByUserId(id);
        System.out.println(users.stream().toString());

    }
}