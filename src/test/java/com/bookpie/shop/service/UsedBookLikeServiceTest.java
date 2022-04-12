package com.bookpie.shop.service;

import com.bookpie.shop.domain.UsedBook;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.dto.UsedBookCreateDto;
import com.bookpie.shop.dto.UserCreateDto;
import com.bookpie.shop.repository.UsedBookLikeRepository;
import com.bookpie.shop.repository.UsedBookRepository;
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
class UsedBookLikeServiceTest {

    @Autowired
    UsedBookLikeService service;
    @Autowired
    UsedBookLikeRepository repository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UsedBookRepository usedBookRepository;
    @Autowired UsedBookService usedBookService;
    public User user(){
        UserCreateDto dto = new UserCreateDto();
        dto.setEmail("123");
        dto.setName("123");
        dto.setNickName("123123123");
        return User.createUser(dto);
    }
    public UsedBook usedBook(User user){
        UsedBookCreateDto dto = new UsedBookCreateDto();
        dto.setContent("asasd");
        dto.setPrice(123);
        dto.setTitle("123123");
        dto.getTags().add("tag1");
        dto.getTags().add("tag2");
        return UsedBook.createUsedBook(user,dto);
    }
    @Test
    public void UsedBookLikeServiceTest() throws Exception{
        //given
        User user = user();
        Long userId = userRepository.save(user);
        UsedBook usedBook = usedBook(user);
        Long bookId = usedBookRepository.save(usedBook);
        //when
        String like = service.like(userId, bookId);
        //then
        assertEquals("created",like);

        String like2 = service.like(userId,bookId);
        assertEquals("deleted",like2);
    }
}