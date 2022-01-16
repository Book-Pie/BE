package com.bookpie.shop.service;

import com.bookpie.shop.domain.Order;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.UserReview;
import com.bookpie.shop.domain.dto.UserReviewCreateDto;
import com.bookpie.shop.repository.OrderRepository;
import com.bookpie.shop.repository.UserRepository;
import com.bookpie.shop.repository.UserReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReviewService {

    private final UserReviewRepository userReviewRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long uploadUserReview(UserReviewCreateDto dto,Long userId){
        Order order = orderRepository.findDetailById(dto.getOrderId()).orElseThrow(()->new EntityNotFoundException("주문을 찾을 수 없습니다."));
        UserReview userReview = UserReview.createUserReview(dto,order);
        User user = order.getBook().getSeller();
        if (order.getBuyer().getId()!= userId){
            throw new IllegalArgumentException("주문을 한 회원이 아닙니다.");
        }
        user.addRating(dto.getRating());
        return userReviewRepository.save(userReview);
    }
}
