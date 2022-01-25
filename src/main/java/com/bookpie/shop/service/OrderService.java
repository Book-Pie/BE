package com.bookpie.shop.service;

import com.bookpie.shop.domain.Order;
import com.bookpie.shop.domain.UsedBook;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.OrderCreateDto;
import com.bookpie.shop.domain.dto.OrderDto;
import com.bookpie.shop.domain.dto.OrderListDto;
import com.bookpie.shop.domain.enums.OrderState;
import com.bookpie.shop.domain.enums.SaleState;
import com.bookpie.shop.repository.OrderRepository;
import com.bookpie.shop.repository.UsedBookRepository;
import com.bookpie.shop.repository.UserRepository;
import com.bookpie.shop.utils.PageUtil.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

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
        log.debug(orderCreateDto.toString());
        User user = userRepository.findById(orderCreateDto.getUserId())
                                  .orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        UsedBook usedBook = usedBookRepository.findByIdWithUser(orderCreateDto.getUsedBookId())
                                              .orElseThrow(()->new IllegalArgumentException("중고도서를 찾을 수 없습니다."));
        if(usedBook.getSaleState() != SaleState.SALE || usedBook.getSeller().getId() == orderCreateDto.getUserId()){
            throw new IllegalArgumentException("주문할 수 없는 상품입니다.");
        }
        if (user.getPoint().getHoldPoint()<usedBook.getPrice()){
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }

        usedBook.trading();
        user.getPoint().usePoint(usedBook.getPrice());
        Order order = Order.createOrder(user,orderCreateDto,usedBook);
        return orderRepository.save(order);

    }

    @Transactional
    public boolean removeOrder(Long id){
        Order order = orderRepository.findDetailById(id).orElseThrow(()->new EntityNotFoundException("주문을 찾을 수 없습니다."));
        if (order.getOrderState() == OrderState.SOLD_OUT){
            throw new IllegalStateException("이미 완료된 주문은 취소할 수 없습니다.");
        }
        order.getBook().cancel();
        order.getBuyer().getPoint().rollback(order.getBook().getPrice());
        return orderRepository.remove(order);
    }

    public PageDto getOrdersByBuyer(Long userId, int page, int limit, Long pageCount){
        if (pageCount == 0){
            Long total = orderRepository.countByBuyer(userId);
            pageCount = total/limit;
            if(total%limit != 0) pageCount++;
        }
        int offset = page*limit - limit;
        List<Order> orders = orderRepository.findByBuyer(userId,limit,offset);
        List<OrderListDto> result = orders.stream().map(o -> new OrderListDto(o)).collect(Collectors.toList());
        return new PageDto(pageCount,result);
    }

    public PageDto getOrdersBySeller(Long userId, int page, int limit, Long pageCount){
        if (pageCount == 0){
            Long total = orderRepository.countBySeller(userId);
            pageCount = total/limit;
            if (total%limit != 0) pageCount++;
        }
        int offset = page*limit - limit;
        List<Order> orders = orderRepository.findBySeller(userId,limit,offset);
        List<OrderListDto> result = orders.stream().map(o -> new OrderListDto(o)).collect(Collectors.toList());
        return new PageDto(pageCount,result);
    }

    public OrderDto getOrderDetail(Long id){
        Order order = orderRepository.findDetailById(id).orElseThrow(()->new EntityNotFoundException("주문을 찾을 수 없습니다."));
        OrderDto orderDto = new OrderDto(order);
        return orderDto;
    }

    public OrderDto getOrderByBookId(Long bookId){
        Order order = orderRepository.findByBookId(bookId).orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));
        OrderDto orderDto = new OrderDto(order);
        return orderDto;
    }

    @Transactional
    public boolean orderEnd(Long orderId,Long userId){
        log.info(orderId.toString(),userId.toString());
        Order order = orderRepository.findDetailById(orderId).orElseThrow(()->new EntityNotFoundException("주문을 찾을 수 없습니다."));
        if (order.getBuyer().getId() != userId) throw new IllegalArgumentException("구매자가 아닙니다.");
        order.end();
        return true;
    }
}
