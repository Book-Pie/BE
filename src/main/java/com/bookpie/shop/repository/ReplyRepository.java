package com.bookpie.shop.repository;

import com.bookpie.shop.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    // 게시글의 댓글 조회
    @Query(value = "select r.reply_id, r.content, r.reply_date, r.board_id," +
            " coalesce(r.parent_reply_id, 0) as parent_reply_id, coalesce(r.used_id, 0) as used_id, r.user_id" +
            " from reply r where r.board_id = :board_id", nativeQuery = true)
    Page<Reply> findAllByBoard(@Param("board_id") Long board_id, @Param("pageable") Pageable pageable);

    // 중고도서 댓글 조회
    @Query(value = "select r.reply_id, r.content, r.reply_date, r.used_id," +
            " coalesce(r.parent_reply_id, 0) as parent_reply_id, coalesce(r.board_id, 0) as board_id, r.user_id" +
            " from reply r where r.used_id = :used_id", nativeQuery = true)
    Page<Reply> findAllByUsedBook(@Param("used_id") Long used_id, @Param("pageable") Pageable pageable);
}
