package com.bookpie.shop.repository;

import com.bookpie.shop.domain.UsedBook;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.dto.UsedBookCreateDto;
import com.bookpie.shop.dto.UserCreateDto;
import com.bookpie.shop.enums.SaleState;
import com.querydsl.core.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        useDto.setNickName("nick");
        useDto.setPassword("1234");
        useDto.setPhone("01049432618");
        useDto.setName("kim");
        useDto.setEmail("test@gmail.com");
        userRepository.save(User.createUser(useDto));
        User user = userRepository.findByEmail("test@gmail.com").get();
        //when

        UsedBook usedBook = UsedBook.createUsedBook(user,dto);
        Long id = usedBookRepository.save(usedBook);
        //then
        assertNotNull(usedBookRepository.findById(id).get());
    }

    @Test
    public void UsedBookRepositoryTest() throws Exception{
        //given
        UsedBookCreateDto dto = new UsedBookCreateDto();
        UsedBook book1 = UsedBook.createUsedBook(null,dto);
        UsedBook book2 = UsedBook.createUsedBook(null,dto);
        //when
        book1.trading();
        book2.soldout();
        usedBookRepository.save(book1);
        usedBookRepository.save(book2);
        //then
        Map<SaleState,Long> map = new HashMap();
        List<Tuple> result = usedBookRepository.groupCount();
        result.stream().forEach(res->map.put(res.get(0,SaleState.class),res.get(1,Long.class)));
        assertEquals(1,map.get(SaleState.SOLD_OUT));
    }

}