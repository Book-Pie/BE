package com.bookpie.shop.service;

import com.bookpie.shop.domain.UsedBook;
import com.bookpie.shop.domain.UsedBookLike;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.repository.UsedBookLikeRepository;
import com.bookpie.shop.repository.UsedBookRepository;
import com.bookpie.shop.repository.UserRepository;
import com.bookpie.shop.utils.ApiUtil;
import com.bookpie.shop.utils.ApiUtil.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static com.bookpie.shop.utils.ApiUtil.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UsedBookLikeService {

    private final UsedBookLikeRepository repository;
    private final UserRepository userRepository;
    private final UsedBookRepository usedBookRepository;

    @Transactional
    public String like(Long userId,Long bookId){
        Optional<UsedBookLike> usedBookLike = repository.findByUserAndBook(userId, bookId);
        if(usedBookLike.isPresent()){
            repository.delete(usedBookLike.get().getId());
            return "deleted";
        }else{
            User user = userRepository.findById(userId).orElseThrow(()->new UsernameNotFoundException("사용자가 없습니다."));
            UsedBook usedBook = usedBookRepository.findById(bookId).orElseThrow(()->new EntityNotFoundException("등록된 중고도서가 없습니다."));
            UsedBookLike like = UsedBookLike.createUsedBookLike(user, usedBook);
            repository.save(like);
            return "created";
        }
    }

}
