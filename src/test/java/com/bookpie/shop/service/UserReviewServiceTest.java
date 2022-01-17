package com.bookpie.shop.service;

import com.bookpie.shop.domain.*;
import com.bookpie.shop.domain.dto.UserReviewCreateDto;
import com.bookpie.shop.domain.enums.BookState;
import com.bookpie.shop.repository.OrderRepository;
import com.bookpie.shop.repository.UsedBookRepository;
import com.bookpie.shop.repository.UserRepository;
import com.bookpie.shop.repository.UserReviewRepository;
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
class UserReviewServiceTest {
    @Autowired
    UserReviewService userReviewService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    UsedBookRepository usedBookRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserReviewRepository userReviewRepository;
    @Test
    public void UserReviewUploadTest() throws Exception{
        //given
        User user1 = user1();
        User user2 = user2();
        UsedBook usedBook = book(user1);
        userRepository.save(user1);
        userRepository.save(user2);
        usedBookRepository.save(usedBook);
        Order order = order(user2,usedBook);
        orderRepository.save(order);
        //when
        UserReviewCreateDto dto = new UserReviewCreateDto(order.getId(),"리뷰",4.0f);
        Long id = userReviewService.uploadUserReview(dto,user2.getId());
        //then
        UserReview review = userReviewRepository.findById(id).get();
        assertEquals(id,review.getId());
        assertEquals(order.getReview(),review);
        assertEquals(4.0f,user1.getRating());
    }

    @Test
    public void UserReviewDeleteTest() throws Exception{
        //given

        User user1 = user1();
        User user2 = user2();
        UsedBook usedBook = book(user1);
        userRepository.save(user1);
        userRepository.save(user2);
        usedBookRepository.save(usedBook);
        Order order = order(user2,usedBook);
        orderRepository.save(order);
        UserReviewCreateDto dto = new UserReviewCreateDto(order.getId(),"리뷰",4.0f);
        Long id = userReviewService.uploadUserReview(dto,user2.getId());
        //when
        UserReview review = userReviewRepository.findById(id).get();

        //then
        assertThrows(IllegalArgumentException.class,()->userReviewService.deleteUserReview(id,user1.getId()));
        assertEquals(true,userReviewService.deleteUserReview(id,user2.getId()));
    }


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

    Order order(User user ,UsedBook usedBook){
        return Order.builder().usedBook(usedBook)
                .buyer(user)
                .build();
    }
}