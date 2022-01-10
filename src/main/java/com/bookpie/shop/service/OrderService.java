package com.bookpie.shop.service;

import com.bookpie.shop.domain.Order;
import com.bookpie.shop.domain.UsedBook;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.OrderCreateDto;
import com.bookpie.shop.domain.dto.OrderDto;
import com.bookpie.shop.repository.OrderRepository;
import com.bookpie.shop.repository.UsedBookRepository;
import com.bookpie.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final UsedBookRepository usedBookRepository;

    @Transactional
    public Long saveOrder(OrderCreateDto orderCreateDto){
        User user = userRepository.findById(orderCreateDto.getUserId())
                                  .orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        UsedBook usedBook = usedBookRepository.findById(orderCreateDto.getUsedBookId())
                                              .orElseThrow(()->new IllegalArgumentException("중고도서를 찾을 수 없습니다."));
        Order order = Order.createOrder(user, orderCreateDto.getAddress(),usedBook);
        return orderRepository.save(order);

    }

    @Transactional
    public boolean removeOrder(Long id){
        return orderRepository.remove(id);
    }

    public OrderDto getOrderDetail(Long id){
        Order order = orderRepository.findDetailById(id).orElseThrow(()->new EntityNotFoundException("주문을 찾을 수 없습니다."));
        OrderDto orderDto = new OrderDto(order);
        return orderDto;
    }

}
