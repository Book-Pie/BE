package com.bookpie.shop.repository;

import com.bookpie.shop.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 해당 카테고리의 모든 게시글 조회
    @Query(value = "SELECT * " +
            "FROM board " +
            "WHERE board_type = :boardType", nativeQuery = true)
    List<Board> findByBoardType(@Param("boardType") String boardType);
}
