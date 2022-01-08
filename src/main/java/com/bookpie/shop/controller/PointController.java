package com.bookpie.shop.controller;

import com.bookpie.shop.domain.dto.point.PointDto;
import com.bookpie.shop.service.PointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.bookpie.shop.utils.ApiUtil.success;
@RestController
@Slf4j
public class PointController {
    @Autowired
    private PointService pointService;

    // 포인트 충전
    @PostMapping("/api/point")
    public ResponseEntity charge(@RequestBody PointDto dto) {
        log.info("paid_amount : " + dto.getAmount() + ", user_id : " + dto.getUser_id() + ", " +
                "point_id : " + dto.getImp_uid() + ", merchant_uid : " + dto.getMerchant_uid());
        return new ResponseEntity(success(pointService.charge(dto)), HttpStatus.OK);
    }

    // 포인트 결제 내역 조회
    @GetMapping("/api/point/{user_id}")
    public ResponseEntity getPointList(@PathVariable Long user_id) {
        return new ResponseEntity(success(pointService.getPointList(user_id)), HttpStatus.OK);
    }

    // 포인트 환불
    @PostMapping("/api/point/cancel")
    public ResponseEntity cancel(@RequestBody PointDto dto) {
        return new ResponseEntity(success(pointService.cancel(dto)), HttpStatus.OK);
    }

}
