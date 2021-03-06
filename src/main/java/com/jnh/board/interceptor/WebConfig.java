package com.jnh.board.interceptor;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
    // 로그인 여부에 따른 접속가능 페이지 구분
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor()) // 만든 LoginCheckInterceptor 클래스 내용을 넘김
                .order(1) // 해당 인터셉터가 적용되는 순서
                .addPathPatterns("/**") // 해당 프로젝트의 모든 주소에 대해 인터셉터를 적용 (무조건 * 두개)
                .excludePathPatterns("/", "/member/save", "/member/login", "/member/logout", "/css/**"); // 그 중에 이 주소는 제외 (로그인하지 않아도 접속 가능)
    }
}
