package com.tenco.blog.reply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {

//    select * from reply_tb r
//    join board_tb b on r.board_id = b.id
//    join user_tb u on r.user_id = u.id
//    where r.board_id = 1
//    order by r.created_at asc;
    // 게시글 id로 댓글 목록 조회(한 번에 댓글 작성자 정보 포함 - JOIN FETCH)
    @Query("""
           SELECT r FROM Reply r 
           JOIN FETCH r.user JOIN FETCH r.board 
           WHERE r.board.id = :boardId 
           ORDER BY r.createdAt ASC
           """)
    public List<Reply> findByBoardIdWithUser(@Param("boardId") Integer boardId);
}
