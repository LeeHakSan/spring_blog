package com.tenco.blog.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller // IoC
@RequiredArgsConstructor // DI 처리
public class UserController {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @PostMapping("/user/update")
    public String updateProc(UserRequest.UpdateDTO updateDTO, HttpSession session) {
        User sessionUser = (User) httpSession.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }
        try {
            updateDTO.validate();

            // 더티 체킹 전략
            User userEntity = userRepository.updateById(sessionUser.getId(), updateDTO);

            // 세션 동기화 처리
            session.setAttribute("sessionUser", userEntity);

        } catch (Exception e) {
            throw new RuntimeException();
        }
        return "redirect:/";
    }

    // 프로필 화면 요청
    // http://localhost:8080/user/update-form
    @GetMapping("/user/update-form")
    public String updateForm(HttpSession session, Model model) {
        // 인증 검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        User userEntity = userRepository.findById(sessionUser.getId());
        userEntity.setPassword("");

        // 가방에 데이터 받아서 화면에 값 내려주기
        model.addAttribute("user", userEntity);
        return "user/update-form";
    }

    // 로그인 화면 요청
    // login-form : http://localhost:8080/login-form
    @GetMapping("/login-form")
    public String loginFormPage() {
        return "user/login-form";
    }

    // 로그인 기능 요청
    @PostMapping("/login")
    public String loginProc(UserRequest.LoginDTO loginDTO) {
        // 1. 유효성 검사
        loginDTO.validate();

        User sessionUser = userRepository.findByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword());
        if (sessionUser == null) {
            // 로그인 실패 (username, password 불일치)
            throw new IllegalArgumentException("사용자명 또는 비밀번호가 잘못 됐습니다");
        }

        // 코드가 여기까지 도달한다면 우리 DB에 정상 사용자임을 논리적으로 확인 가능
        httpSession.setAttribute("sessionUser", sessionUser);

        System.out.println("로그인 성공");
        System.out.println("로그인 사용자 : " + sessionUser.getUsername());
        System.out.println("로그인 이메일 : " + sessionUser.getEmail());

        return "redirect:/";
    }

    // 로그아웃 기능 요청
    @GetMapping("/logout")
    public String logout() {
        // 세션 메모리에 내 정보를 없애버림
        // 로그아웃
        httpSession.invalidate();

        return "redirect:/";
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
    public String joinProc(UserRequest.JoinDTO joinDTO) {
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
