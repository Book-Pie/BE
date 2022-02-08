package com.bookpie.shop.controller;

import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.point.PointDto;
import com.bookpie.shop.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.bookpie.shop.utils.ApiUtil.success;
@RestController
@RequiredArgsConstructor
@Slf4j
public class PointController {
    private final PointService pointService;

    // 포인트 충전
    @PostMapping("/api/point")
    public ResponseEntity charge(@RequestBody PointDto dto) {
        return new ResponseEntity(success(pointService.charge(dto, getCurrentUserId())), HttpStatus.OK);
    }

    // 포인트 결제 내역 조회
    @GetMapping("/api/point")
    public ResponseEntity getPointList() {
        return new ResponseEntity(success(pointService.getPointList(getCurrentUserId())), HttpStatus.OK);
    }

    // 포인트 환불
    @PostMapping("/api/point/cancel")
    public ResponseEntity cancel(@RequestBody PointDto dto) {
        return new ResponseEntity(success(pointService.cancel(dto, getCurrentUserId())), HttpStatus.OK);
    }

    private Long getCurrentUserId(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }
}
