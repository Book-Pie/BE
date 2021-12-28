package com.bookpie.shop.repository;

import com.bookpie.shop.domain.UsedBook;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.UsedBookCreateDto;
import com.bookpie.shop.domain.dto.UserCreateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Rollback(value = false)
class UsedBookRepositoryTest {

    @Autowired
    UsedBookRepository usedBookRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    public void save() throws Exception{
        //given
        UsedBookCreateDto dto = new UsedBookCreateDto();
        dto.setContent("content");
        dto.setTitle("title");
        dto.setPrice(1000);
        dto.getTags().add("tag1");
        dto.getTags().add("tag2");

        UserCreateDto useDto = new UserCreateDto();
        useDto.setUsername("test1");
        useDto.setNickName("nick");
        useDto.setPassword("1234");
        useDto.setPhone("01049432618");
        useDto.setName("kim");
        useDto.setEmail("test@gmail.com");
        userRepository.save(User.createUser(useDto));
        User user = userRepository.findByUsername("test1").get();
        //when

        UsedBook usedBook = UsedBook.createUsedBook(user,dto);
        Long id = usedBookRepository.save(usedBook);
        //then
        assertNotNull(usedBookRepository.findById(id).get());
    }
}