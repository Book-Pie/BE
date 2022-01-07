package com.bookpie.shop.repository;

import com.bookpie.shop.domain.Order;
import com.bookpie.shop.domain.UsedBook;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.enums.BookState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class OrderRepositoryTest {
    @Autowired UserRepository userRepository;
    @Autowired UsedBookRepository usedBookRepository;
    @Autowired OrderRepository orderRepository;

    User user1(){
        return User.builder()
                .name("user1")
                .nickName("nick1")
                .email("user1@gmail.com")
                .build();
    }

    User user2(){
        return User.builder()
                .name("user2")
                .nickName("nick2")
                .email("user2@gmail.com")
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
    @Transactional
    public void saveAndFind() throws Exception{
        //given
        User user1 = user1();
        User user2 = user2();
        UsedBook book = book(user1);
        userRepository.save(user1);
        userRepository.save(user2);
        usedBookRepository.save(book);
        //when
        Order order = Order.createOrder(user2,null,book);
        Long id = orderRepository.save(order);
        //then
        Order savedOrder = orderRepository.findById(id).get();
        assertEquals(user2,savedOrder.getBuyer());

    }
}