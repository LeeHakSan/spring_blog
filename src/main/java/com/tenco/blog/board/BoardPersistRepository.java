package com.tenco.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository // IoC
@RequiredArgsConstructor // DI 처리 됨
public class BoardPersistRepository {

    // JPA 핵심 인터페이스
    // 영속성 컨텍스트를 관리하고 엔티티의 생명주기를 제어
    // @Autowired // DI
    private final EntityManager em; // final 사용하면 성능 개선이 조금 됨

    // 의존 주입 (외부에서 생성되어 있는 객체의 주소값을 주입 받다)
//    public BoardPersistRepository(EntityManager em) {
//        this.em = em;
//    }
    // 의존 주입 (외부에서 생성되어 있는 객체의 주소값을 주입 받다)

    // 게시글 저장
    @Transactional
    public Board save(Board board) {
        // 1. 매개 변수로 받은 board는 비영속상태
        //    -- 아직 영속성 컨테스트에 관리 되고 있지 않는 상태
        //    -- 데이터베이스와 연관 없는 순수 JAVA 객체일 뿐 아직은..

        // em.createNativeQuery("insert into board_tb ...") X
        em.persist(board); // insert 처리 완료
        // 2. 이 board 객체를 영속성 컨텍스트에 넣어 둠 (SQL 저장소에 등록)
        //    -- 영속성 컨텍스트에 들어가더라도 아직 DB 실제 insert한 상태는 아님

        // 3. 트랜잭션 커밋시점에 실제 DB에 접근해서 insert 구문이 수행이 된다.

        // 4. board 객체의 id 변수값을 1차 캐시에 map 구조로 보관 되어 진다.
        //    1차 캐시에 들어간 이제 영속상태로 변경된 Object를 리턴 한다.
        return board;
    }
}