package com.tenco.blog.board;

import lombok.Builder;
import lombok.Data;

// 요청데이터를 담는 DTO 클래스
// 컨트롤러... 비즈니스... 데이터 계층 사이에서
public class BoardRequest {

    @Data
    @Builder
    public static class SaveDTO {
        private String username;
        private String title;
        private String content;

        // 편의 기능 설계 가능
        // DTO에서 Entity로 변환 해주는 편의 메서드
        public Board toEntity() {
            return Board.builder()
                    .username(username)
                    .title(title)
                    .content(content)
                    .build();
        }
    }

    // 내부 정적 클래스 게시글 수정 DTO 설계
    @Data
    public static class UpdateDTO {
        private String username;
        private String title;
        private String content;


        // 게시글 수정 시 유효성 검사 편의 메서드
        public void validate() {
            if (this.username.trim().isEmpty()) {
                throw new IllegalArgumentException("유저 이름은 공백일 수 없습니다.");
            }
            if (this.title.trim().isEmpty()) {
                throw new IllegalArgumentException("제목은 공백일 수 없습니다.");
            }
            if (this.content.trim().isEmpty() || this.content.trim().length() <= 3) {
                throw new IllegalArgumentException("컨텐츠 내용은 3자 이하일 수 없습니다.");
            }
        }
    }
}
