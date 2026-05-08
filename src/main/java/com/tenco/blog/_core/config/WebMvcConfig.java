package com.tenco.blog._core.config;

import com.tenco.blog._core.interceptor.LoginInterceptor;
import com.tenco.blog._core.interceptor.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Component
@Configuration // IoC 대상 하나 이상의 IoC 처리를 하고싶을 때 사용한다
// 자바 코드로 스프링 부트 설정파일을 다룰 수 있다

public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired // DI 처리
    private  LoginInterceptor loginInterceptor;

    @Autowired // DI 처리
    private SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 회면에 SessionUser 정보를 내려줄 때 사용됨
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/**"); // 모든 URL 요청에서 동작함


        // 인증처리 인터셉터 동작함
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/board/**", "/user/**")
                .excludePathPatterns(
                        // 로그인 관련 (인증이 필요 없는 페이지)
                        "/login-form", // 로그인 화면 요청 시
                        "/join-form", // 회원 가입 화면 요청시 제외
                        "/logout", // 로그아웃

                        // 게시글 조회 관련 (인증 없이도 볼 수 있는 페이지)
                        "/board/list", // 게시글 목록 화면 요청
                        "/"          , // 메인 페이지
                        "/index"          , // 메인 페이지
                        "/board/{id:\\d+}", // 게시글 상세 조회 (숫자 ID 만 허용)

                        // 정적 리소스 (CSS, JS, 이미지 등)
                        "/css/**",          // CSS 파일 제외
                        "/js/**",           // JS 파일 제외
                        "/images/**",       // 이미지 파일 제외
                        "/favicon.ico",     // 파비콘 제외

                        // H2 데이터베이스 콘솔 (개발 환경용)
                        "/h2-console/**"    // h2 콘솔 제외
                );

    }
}
