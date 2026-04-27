package com.tenco.blog.board;

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
    private String username;
    private String title;
    private String content;

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
}
