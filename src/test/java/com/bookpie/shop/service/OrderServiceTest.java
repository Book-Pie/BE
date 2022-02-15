package com.bookpie.shop.service;

import com.bookpie.shop.domain.Order;
import com.bookpie.shop.domain.Point;
import com.bookpie.shop.domain.UsedBook;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.OrderCreateDto;
import com.bookpie.shop.domain.dto.OrderDto;
import com.bookpie.shop.domain.enums.BookState;
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


    User user1(){
        return User.builder()
                   .name("user1")
                   .nickName("nick1")
                   .point(Point.createDefaultPoint())
                   .email("user1@gmail.com")
                   .build();
    }

    User user2(){
        return User.builder()
                   .name("user2")
                   .nickName("nick2")
                   .email("user2@gmail.com")
                   .point(Point.createDefaultPoint())
                   .build();
    }

    UsedBook book(User user){
        return UsedBook.builder()
                       .price(1000)
                       .title("책 팝니다")
                       .seller(user)
                       .bookState(BookState.UNRELEASED)
                       .build();
    }
    @Test
    public void orderSaveTest() throws Exception{
        //given
        User user = user1();
        userRepository.save(user);
        UsedBook usedBook = book(user);
        usedBookRepository.save(usedBook);
        //when
        OrderCreateDto dto = new OrderCreateDto();
        dto.setUsedBookId(usedBook.getId());
        dto.setUserId(user.getId());
        //then
        Long id = orderService.saveOrder(dto);
        Order saveOrder = orderRepository.findDetailById(id).get();
        assertEquals("user1",saveOrder.getBuyer().getName());
        assertEquals("책 팝니다",saveOrder.getBook().getTitle());
    }

    @Test
    public void findByIdTest() throws Exception{
        //given
        User user1 = user1();
        User user2 = user2();
        UsedBook book = book(user1);
        usedBookRepository.save(book);
        userRepository.save(user1);
        userRepository.save(user2);
        Order order = Order.createOrder(user2,null,book);
        Long id = orderRepository.save(order);
        //when
        OrderDto dto = orderService.getOrderDetail(id);
        //then
        assertEquals(user2.getNickName(),dto.getBuyer().getNickName());
        assertEquals(user1.getNickName(),dto.getSeller().getNickName());
    }
}