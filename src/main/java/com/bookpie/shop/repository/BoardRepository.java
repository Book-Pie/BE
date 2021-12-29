package com.bookpie.shop.repository;

import com.bookpie.shop.domain.Board;
import com.bookpie.shop.domain.enums.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 해당 카테고리의 모든 게시글 조회
    //@Query(value = "select * from board where board_type = :boardType", nativeQuery = true)
    Page<Board> findByBoardType(@Param("boardType") BoardType boardType, @Param("pageable") Pageable pageable);

    @Modifying
    @Query(value = "update board p set p.view = p.view + 1 where p.board_id = :board_id", nativeQuery = true)
    void viewPlus(@Param("board_id") Long board_id);
}
