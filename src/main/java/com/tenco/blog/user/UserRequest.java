package com.tenco.blog.user;

import lombok.Data;

public class UserRequest {

    // 회원가입 DTO
    @Data
    public static class joinDTO {
        private String username;
        private String password;
        private String email;

        // 메서드 편의 기능 추가 - 내가 가지고 있는 멤버 변수의 값으로 User 엔티티 생성
        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .build();
        }

        // 유효성 검사 메서드 만들기
        public void validate() {
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("사용자 명은 필수입니다");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("비밀번호는 필수입니다");
            }
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("이메일은 필수 입니다.");
            }
            if (!email.contains("@")) {
                throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다");
            }
        }

    }

}
