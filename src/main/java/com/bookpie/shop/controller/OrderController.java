package com.bookpie.shop.controller;

import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.OrderCreateDto;
import com.bookpie.shop.service.OrderService;
import com.bookpie.shop.utils.ApiUtil;
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
        return new ResponseEntity(success(orderService.saveOrder(orderCreateDto)), HttpStatus.OK);
    }

    private Long getCurrentUserId(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }
}
