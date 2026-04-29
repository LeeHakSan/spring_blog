package com.tenco.blog.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller // IoC
@RequiredArgsConstructor // DI 처리
public class UserController {

    private final UserRepository userRepository;

    // 로그인 화면 요청
    // login-form : http://localhost:8080/login-form
    @GetMapping("/login-form")
    public String loginFormPage () {
        return "user/login-form";
    }

    // 회원가입 화면 요청
    // 주소설계 : http://localhost:8080/join-form
    @GetMapping("/join-form")
    public String joinFormPage() {

        return "user/join-form";
    }

    // 회원가입 기능 요청
    // 주소설계 : http://localhost:8080/join
    @PostMapping("/join")
    // 메세지 컨버터가 구문을 분석해서 자동으로 파싱 처리 및 매핑 해준다
    // 파싱 전략 1 - key=value 구조 (@RequestPharam)
    // 파싱 전략 2 - ObjectDTO 설계
    public String joinProc(UserRequest.joinDTO joinDTO) {
        log.info("username: " + joinDTO.getUsername());
        log.info("username: " + joinDTO.getPassword());
        log.info("username: " + joinDTO.getEmail());
        // 1. 유효성 검사 하기
        joinDTO.validate(); // 유효성 검사 --> 오류 --> 예외 처리로 넘어감
//        회원가입 요청 전 중복 username 검사
        User userCheckName = userRepository.findByUsername(joinDTO.getUsername());
        if (userCheckName != null) {
            throw new IllegalArgumentException("이미 사용중인 username 입니다 : " + joinDTO.getUsername());
        }
        userRepository.save(joinDTO.toEntity());

        // todo 로그인 화면으로 리다이렉트 처리 예정
        return "redirect:/";
    }
}
