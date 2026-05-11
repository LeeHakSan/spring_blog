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

    private final UserService userService;

    // 프로필 수정 기능 요청
    @PostMapping("/user/update")
    public String updateProc(UserRequest.UpdateDTO updateDTO, HttpSession session) {
        updateDTO.validate();
        User sessionUser = (User) session.getAttribute("sessionUser");
        User updateUser = userService.회원정보수정(sessionUser.getId(), updateDTO);
        session.setAttribute("sessionUser", updateUser);
        return "redirect:/";
    }

    // 프로필 화면 요청
    @GetMapping("/user/update-form")
    public String updateForm(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        User user = userService.회원정보수정화면(sessionUser.getId());
        model.addAttribute("user", user);
        return "user/update-form";
    }

    // 로그인 화면 요청
    // login-form : http://localhost:8080/login-form
    @GetMapping("/login-form")
    public String loginFormPage() {
        // 인증 검사 x, 유효성 검사 x
        return "user/login-form";
    }

    // 로그인 기능 요청
    @PostMapping("/login")
    public String loginProc(UserRequest.LoginDTO reqloginDTO, HttpSession session) {
        // 인증 검사 x, 유효성 검사 o
        reqloginDTO.validate();
        User user = userService.로그인(reqloginDTO);
        session.setAttribute("sessionUser", user);

        return "redirect:/";
    }

    // 로그아웃 기능 요청
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 세션 메모리에 내 정보를 없애버림
        // 로그아웃
        session.invalidate();

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
    public String joinProc(UserRequest.JoinDTO joinDTO) {
        //  인증검사 x, 유효성 검사 하기 o
        joinDTO.validate(); // 유효성 검사 --> 오류 --> 예외 처리로 넘어감
        userService.회원가입(joinDTO);

        return "redirect:/login-form";
    }


}
