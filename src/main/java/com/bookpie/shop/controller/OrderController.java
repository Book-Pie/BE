package com.bookpie.shop.controller;

import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.OrderCreateDto;
import com.bookpie.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.bookpie.shop.utils.ApiUtil.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/order")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping("")
    public ResponseEntity createOrder(@RequestBody OrderCreateDto orderCreateDto){
        orderCreateDto.setUserId(getCurrentUserId());
        return new ResponseEntity(success(orderService.saveOrder(orderCreateDto)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getOrderDetail(@PathVariable(value = "id") Long orderId){
        return new ResponseEntity(success(orderService.getOrderDetail(orderId)),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteOrder(@PathVariable(value = "id")Long orderId){
        return new ResponseEntity(success(orderService.removeOrder(orderId)),HttpStatus.OK);
    }

    @GetMapping("/seller")
    public ResponseEntity getOrdersBySeller(@RequestParam(value = "id") Long userId,
                                            @RequestParam(value = "page",required = false,defaultValue = "1") int page,
                                            @RequestParam(value = "limit",required = false,defaultValue = "5") int limit,
                                            @RequestParam(value = "pageCount",required = false,defaultValue = "0")Long pageCount){
        return new ResponseEntity(success(orderService.getOrdersBySeller(userId,page,limit,pageCount)),HttpStatus.OK);
    }

    @GetMapping("/buyer")
    public ResponseEntity getOrdersByBuyer(@RequestParam(value = "id") Long userId,
                                           @RequestParam(value = "page",required = false,defaultValue = "1") int page,
                                           @RequestParam(value = "limit",required = false,defaultValue = "5") int limit,
                                           @RequestParam(value = "pageCount",required = false,defaultValue = "0") Long pageCount){
        return new ResponseEntity(success(orderService.getOrdersByBuyer(userId,page,limit,pageCount)),HttpStatus.OK);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity getOrderDetailByBookId(@PathVariable("id") Long bookId){
        return new ResponseEntity(success(orderService.getOrderByBookId(bookId)),HttpStatus.OK);
    }

    @PostMapping("/end/{id}")
    public ResponseEntity finishOrder(@PathVariable("id") Long orderId){
        return new ResponseEntity(success(orderService.orderEnd(orderId,getCurrentUserId())),HttpStatus.OK);
    }
    private Long getCurrentUserId(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }
}
