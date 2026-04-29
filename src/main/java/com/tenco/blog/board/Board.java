package com.tenco.blog.board;

import com.tenco.blog.user.User;
import com.tenco.blog.util.MyDateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data // get, set, toString 자동으로 만들어줌
// @Entity : JPA가 이 클래스를 데이터베이스 테이블과 매핑하는 객체로 인식 하게 설정
// 즉, 이 어노테이션이 있어야 JPA가 관리함
@Entity
@Table(name = "board_tb")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Board {

    // @Id : 이 필드가 기본키임을 설정함
    @Id
    // IDENTITY 전략 : 데이터 베이스에 기본 AUTO_INCREMENT 기능 사용
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
   // private String username; 삭제 해야함
    private String title;
    private String content;

    // 연관관계 설정을 해줘야함
    // N : 1 연관관계 : 여러개 게시글이 하나의 사용자에게 속한다
    // FetchType 전략 : EAGER, LAZY
    // EAGER - 조회 시 한 번에 다 가져온다 (1번 게시글 조회시 한 번 조인까지 해라)
    // LAZY - 처음부터 Board 엔티티를 조회할 때 User 정보를 가져오지않음. 필요할 때 한 번 더 조회함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 외래키 컬럼명 표시 됨
    private User user;

    // @CreationTimestamp : 하이버 네이트가 제공하는 어노테이션
    // 특정 하나의 엔티티가 저장이 될 때 현재 시간을 자동으로 저장해 설정
    // now() 명시할 필요 없음
    // pc의 시간을 db에 (자동 날짜 주입)
    @CreationTimestamp
    private Timestamp createdAt;

    // createdAt -> 포멧 하는 메서드 만들어 보기
    public String getTime() {
        return MyDateUtil.timestampFormat(this.createdAt);
    }

    // 수정 편의 기능 만들기
    public void update(BoardRequest.UpdateDTO updateDTO) {
//        this.username = updateDTO.getUsername();
        this.title = updateDTO.getTitle();
        this.content = updateDTO.getContent();

        // 변경 감지 동작 과정 (더티체킹)
        // 1. 최초 조회시 영속성 컨텍스트 1차 캐쉬에 데이터를 스냅샷으로 보관함
        // 2. 영속화된 엔티티가(board)의 멤버 변수 값이 변경 된다면
        // 1차에서 보관했던 값과 2차에서 수정된 필드값을 비교함
        // 3. 변화가 발생이 되었다면 트랜잭션 커밋 시점에 변경된 필드값 UPDATE 쿼리 자동 생성
        // 4. 물리적인 DB에 반영됨
    }
}
