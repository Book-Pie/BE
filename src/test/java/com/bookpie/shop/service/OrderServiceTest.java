package com.bookpie.shop.service;

import com.bookpie.shop.domain.Order;
import com.bookpie.shop.domain.Point;
import com.bookpie.shop.domain.UsedBook;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.dto.OrderCreateDto;
import com.bookpie.shop.enums.BookState;
import com.bookpie.shop.enums.OrderState;
import com.bookpie.shop.enums.SaleState;
import com.bookpie.shop.repository.OrderRepository;
import com.bookpie.shop.repository.UsedBookRepository;
import com.bookpie.shop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    @Spy
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UsedBookRepository usedBookRepository;


    User user(Long id){
        return User.builder()
                    .id(id)
                   .name("user"+ id)
                   .nickName("nick"+ id)
                   .point(Point.createDefaultPoint())
                   .email("user"+ id +"@gmail.com")
                   .build();
    }


    UsedBook book(User user,Long id){
        return UsedBook.builder()
                       .id(id)
                       .price(1000)
                       .title("책 팝니다")
                       .seller(user)
                       .bookState(BookState.UNRELEASED)
                       .saleState(SaleState.SALE)
                       .build();
    }

    OrderCreateDto dto(Long userId,Long bookId){
        return OrderCreateDto.builder()
                .userId(userId)
                .usedBookId(bookId)
                .build();
    }

    @Test
    public void orderSaveTest() throws Exception{
        //given
        User user1 = user(1l);
        User user2 = user(2l);
        user2.getPoint().chargePoint(1000);
        UsedBook usedBook = book(user1,3l);

        when(userRepository.findById(2l)).thenReturn(Optional.ofNullable(user2));
        when(usedBookRepository.findByIdWithUser(3l)).thenReturn(Optional.ofNullable(usedBook));
        OrderCreateDto dto = dto(user2.getId(), usedBook.getId());

        //when
        orderService.saveOrder(dto);

        //then
        verify(orderRepository).save(any());
        assertEquals(SaleState.TRADING,usedBook.getSaleState());
        assertEquals(0,user2.getPoint().getHoldPoint());
        assertEquals(1000,user2.getPoint().getUsedPoint());

    }

    @Test
    public void orderEndTest() throws Exception{
        //given
        User user1 = user(1l);
        User user2 = user(2l);
        UsedBook usedBook = book(user1,3l);
        OrderCreateDto dto = new OrderCreateDto();
        dto.setUserId(2l);
        Order order = Order.createOrder(user2,dto,usedBook);
        //when
        order.end();
        //then
        assertEquals(OrderState.SOLD_OUT,order.getOrderState());
        assertEquals(1000,user1.getPoint().getHoldPoint());
    }

    @Test
    public void orderRemoveTest() throws Exception{
        //given
        User user1 = user(1l);
        User user2 = user(2l);
        UsedBook usedBook = book(user1,3l);
        OrderCreateDto dto = new OrderCreateDto();
        dto.setUserId(2l);
        Order order = Order.createOrder(user2,dto,usedBook);
        when(orderRepository.findDetailById(any())).thenReturn(Optional.ofNullable(order));

        //when
        orderService.removeOrder(order.getId());

        //then
        verify(orderRepository).findDetailById(any());
        verify(orderRepository).remove(any());
        assertEquals(SaleState.SALE,usedBook.getSaleState());
        assertEquals(1000,user2.getPoint().getHoldPoint());
    }

}