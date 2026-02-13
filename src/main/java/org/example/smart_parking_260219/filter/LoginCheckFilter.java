package org.example.smart_parking_260219.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Log4j2
@WebFilter(value = "/*") // 모든 요청에 대해 필터 적용 (내부에서 예외 처리)
public class LoginCheckFilter implements Filter {

    // 로그인 검사를 제외할 경로 목록
    private static final List<String> EXCLUDE_URLS = Arrays.asList(
            "/login",
            "/login/verifyEmail", "/login/sendLoginOtp", "/login/verifyEmailOtp",  // 로그인 2차 인증
            "/logout",
            "/resources",
            "/CSS",
            "/JS"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // doFilter()는 필터가 필터링이 필요한로직을 구현하는 부분
        log.info("LoginCheckFilter doFilter() called");

        // 세션에서 로그인 정보를 확인 -> 있으면 다음 필터 동작, 없으면 로그인 페이지 이동
        // 형변환 : HTTP 전용 기능을 쓰기 위해 ServletRequest를 HttpServletRequest로 바꿈.
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 경로 추출 : 현재 사용자가 요청한 주소가 무엇인지 파악
        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = requestURI.substring(contextPath.length());  // 전체 주소에서 프로젝트 경로 제외

        // 캐시 제어 로직 추가 (세션 없이 뒤로가기 시 보안 강화)
        // 로그인/비밀번호 관련 페이지가 아니고, 정적 리소스(css, js)가 아닐 경우에만 캐시 금지 설정
        if (!path.startsWith("/login") && !path.startsWith("/password") &&
                !path.endsWith(".css") && !path.endsWith(".js")) {

            resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
            resp.setHeader("Pragma", "no-cache"); // HTTP 1.0
            resp.setHeader("Expires", "0"); // Proxies
        }

        // 상세 로깅
//        log.info("=== LoginCheckFilter ===");
//        log.info("Request URI: {}", requestURI);
//        log.info("Context Path: {}", contextPath);
//        log.info("Path: {}", path);

        // 예외 경로인지 확인 (로그인, CSS 파일 등은 검사 없이 통과)
        if (isExcluded(path)) {
            log.info("제외 경로 - 통과: {}", path);
            chain.doFilter(request, response);
            return;
        }

        // 세션 확인 (로그인 여부 판단)
        HttpSession session = req.getSession(false);  //세션 없으면 null
        boolean isLoggedIn = (session != null && session.getAttribute("loginManager") != null);

        if (isLoggedIn) {
            // 로그인 된 상태라면 요청한 페이지로 보내줌
            log.info("인증됨 - 요청 통과: {}", path);
            chain.doFilter(request, response);
            return;
        }

        // 로그인 X + 예외 경로 아님 = 로그인 페이지로 쫓아냄
        log.warn("미인증 요청 차단: {}", path);
        resp.sendRedirect(contextPath + "/login");
    }

    // 제외 경로 검사 메서드: 요청 주소가 EXCLUDE_URLS 목록으로 시작하는지 확인
    private boolean isExcluded(String path) {
        if (path.equals("/")) return false; // 루트(/)는 보통 대시보드로 가야 하므로 검사 대상

        for (String exclude : EXCLUDE_URLS) {
            if (path.startsWith(exclude)) return true;
        }
        return false;
    }
}