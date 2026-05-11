package com.tenco.blog.reply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {

    // 1. 등록 및 수정 : save(Board)
    // 2. 단건 조회 : findById(Integer id)
    // 3. 전체 조회 : findAll()
    // 4. 삭제 : deleteById(Integer id) - reply
    // 5. 데이터 개수 : count()
    // 6. 존재 여부 확인 : existsById(Integer id)

    // 즉 board_id 로 댓글을 삭제하는 기능은 없다

    @Query("""
           SELECT r FROM Reply r 
           JOIN FETCH r.user JOIN FETCH r.board 
           WHERE r.board.id = :boardId 
           ORDER BY r.createdAt ASC
           """)
    public List<Reply> findByBoardIdWithUser(@Param("boardId") Integer boardId);

    // 즉 board_id 로 댓글을 삭제하는 기능은 없다

    /**
     * 이전 수정 또는 삭제 기능에서는 수정은 더티 체킹으로 처리 하였고 삭제는 기본적으로
     * 제공히는 em.remove() 메서드를 사용해서 처리했다
     * 지금은 직접 JPQL쿼리를 선언래서 DELETE 처리하는 구문이랑 다른 상황이다.
     * @Query(...) <- JPA 기본적으로 SELECT 쿼리로만 인식을 하기 때문에
     * INSERT, UPDATE, DELETE 는 JPA 에게 SELECT 쿼리가 아니야 라고 알려줘야 제대로 동작
     * 그 어노테이션이 @Modifying 이다
     */
    @Modifying
    @Query("DELETE FROM Reply r WHERE r.board.id = :boardId")
    void deleteByBoardId(@Param("boardId") Integer boardId);
}
