package com.bookpie.shop.service;

import com.bookpie.shop.domain.Order;
import com.bookpie.shop.domain.Point;
import com.bookpie.shop.domain.UsedBook;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.OrderCreateDto;
import com.bookpie.shop.repository.OrderRepository;
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
class OrderServiceTest {

    @Autowired OrderService orderService;
    @Autowired UserRepository userRepository;
    @Autowired UsedBookRepository usedBookRepository;
    @Autowired OrderRepository orderRepository;
    User user(){
        return User.builder()
                        .name("kim")
                        .email("test@gmail")
                        .point(Point.createDefaultPoint())
                        .build();

    }

    UsedBook usedBook(){
        return UsedBook.builder()
                .title("title")
                .build();
    }

    @Test
    public void orderSaveTest() throws Exception{
        //given
        User user = user();
        userRepository.save(user);
        UsedBook usedBook = usedBook();
        usedBookRepository.save(usedBook);
        //when
        OrderCreateDto dto = new OrderCreateDto();
        dto.setUsedBookId(usedBook.getId());
        dto.setUserId(user.getId());
        //then
        Long id = orderService.saveOrder(dto);
        Order saveOrder = orderRepository.findDetailById(id).get();
        assertEquals("kim",saveOrder.getBuyer().getName());
        assertEquals("title",saveOrder.getBook().getTitle());
    }
}