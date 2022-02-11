package com.bookpie.shop.repository;

import com.bookpie.shop.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    @Query(value = "select * from follow where from_user_id = :from_user_id" +
            " and to_user_id = :to_user_id", nativeQuery = true)
    Optional<Follow> findFollow(@Param("from_user_id") Long from_user_id, @Param("to_user_id") Long to_user_id);

    // 내가 팔로잉 한 유저 리스트
    @Query(value = "select * from follow where from_user_id = :from_user_id", nativeQuery = true)
    List<Follow> myFollowing(@Param("from_user_id") Long from_user_id);

    // 나를 팔로잉 한 유저 리스트
    @Query(value = "select * from follow where to_user_id = :to_user_id", nativeQuery = true)
    List<Follow> myFollower(@Param("to_user_id") Long to_user_id);
}
