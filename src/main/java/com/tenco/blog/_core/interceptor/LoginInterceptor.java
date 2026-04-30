package com.tenco.blog._core.interceptor;

import com.tenco.blog._core.errors.Exception401;
import com.tenco.blog.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component // IoC 대상 - 싱글톤 패턴
public class LoginInterceptor implements HandlerInterceptor {

    // 컨트롤러에 들어오기 전에 먼저 동작
    // return에 true 가 있으면 --> Controller 로 진행됨
    // return에 false 가 있으면 --> 안 들여 보냄 (Controller 진입 불가)
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            throw new Exception401("로그인 먼저 해주세요");
        }

        return true;
    }

    // 뷰가 랜더링 되기 전에 낚아챈다
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    // 요청 처리가 완료된 후, 즉 뷰가 완전 랜더링 된 후 호출
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}

