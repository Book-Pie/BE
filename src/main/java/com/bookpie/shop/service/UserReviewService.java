package com.bookpie.shop.service;

import com.bookpie.shop.domain.Order;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.UserReview;
import com.bookpie.shop.domain.dto.UserReviewCreateDto;
import com.bookpie.shop.domain.dto.UserReviewDto;
import com.bookpie.shop.domain.dto.UserReviewUpdateDto;
import com.bookpie.shop.repository.OrderRepository;
import com.bookpie.shop.repository.UserReviewRepository;
import com.bookpie.shop.utils.PageUtil.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReviewService {

    private final UserReviewRepository userReviewRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Map<String, Long> uploadUserReview(UserReviewCreateDto dto, Long userId){
        Order order = orderRepository.findDetailById(dto.getOrderId()).orElseThrow(()->new EntityNotFoundException("주문을 찾을 수 없습니다."));
        UserReview userReview = UserReview.createUserReview(dto,order);
        User user = order.getBook().getSeller();
        if (order.getBuyer().getId()!= userId){
            throw new IllegalArgumentException("주문을 한 회원이 아닙니다.");
        }
        user.addRating(dto.getRating());
        Long reviewId = userReviewRepository.save(userReview);
        Map<String,Long> map = new HashMap<>();
        map.put("reviewId",reviewId);
        map.put("orderId",order.getId());
        return map;
    }

    @Transactional
    public Long deleteUserReview(Long reviewId,Long userId){
        UserReview userReview = userReviewRepository.findById(reviewId)
                                                    .orElseThrow(()->new EntityNotFoundException("등록된 리뷰가 없습니다."));
        if(userReview.getOrder().getBuyer().getId() != userId){
            throw new IllegalArgumentException("리뷰 등록자가 아닙니다.");
        }
        userReview.getOrder().removeReview();
        userReviewRepository.remove(userReview);
        return reviewId;
    }

    @Transactional
    public UserReviewDto updateUserReview(UserReviewUpdateDto userReviewUpdateDto, Long userId){
        UserReview userReview = userReviewRepository.findById(userReviewUpdateDto.getUserReviewId()).orElseThrow(() -> new EntityNotFoundException("등록된 리뷰가 없습니다."));
        if(userReview.getOrder().getBuyer().getId() != userId){
            throw new IllegalArgumentException("리뷰 수정 권한이 없습니다.");
        }

        userReview.getOrder().getBook().getSeller().fixRating(userReview.getRating(),userReviewUpdateDto.getRating());
        userReview.update(userReviewUpdateDto.getContent(),userReviewUpdateDto.getRating());
        return new UserReviewDto(userReview);
    }
    public PageDto getUserReviewsByWriter(Long userId, int page, int limit){
        Long total = userReviewRepository.countByWriter(userId);
        Long pageCount = total/limit;
        if(total % limit !=0) pageCount++;
        int offset = page*limit - limit;
        List<UserReview> result = userReviewRepository.findByWriter(userId,limit,offset);
        List<UserReviewDto> reviews = result.stream().map(UserReviewDto::new).collect(Collectors.toList());
        return new PageDto(pageCount,total,reviews);
    }

    public PageDto getUserReviewsByReader(Long userId, int page, int limit){

        Long total = userReviewRepository.countByReader(userId);
        Long pageCount = total/limit;
        if(total%limit != 0 ) pageCount++;
        int offset = page*limit - limit;
        List<UserReview> result = userReviewRepository.findByReader(userId,limit,offset);
        List<UserReviewDto> reviews = result.stream().map(UserReviewDto::new).collect(Collectors.toList());
        return new PageDto(pageCount,total,reviews);
    }
}
