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
@WebFilter(urlPatterns = "/*") // 모든 요청에 대해 필터 적용 (내부에서 예외 처리)
public class LoginCheckFilter implements Filter {

    // 로그인 검사를 제외할 경로 목록
    private static final List<String> EXCLUDE_URLS = Arrays.asList(
            "/login",
            "/logout",
            "/resources",
            "/css",
            "/js",
            "/images"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // doFilter()는 필터가 필터링이 필요한로직을 구현하는 부분
        log.info("LoginCheckFilter doFilter() called");

        // 세션에서 로그인 정보를 확인
        // 있으면 다음 필터 동작, 없으면 로그인 페이지 이동
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();

        String path = requestURI.substring(contextPath.length());

        // 1. 제외 경로인지 확인 (로그인 페이지 등은 통과)
        if (isExcluded(path)) {
            chain.doFilter(request, response);
            return;
        }

        // 2. 세션 확인
        HttpSession session = req.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("loginManager") != null);

        if (isLoggedIn) {
            // 로그인 된 상태면 통과
            chain.doFilter(request, response);
            return;
        }

        // 3. 로그인이 안 된 경우
        log.info("미인증 요청 차단: {}", path);

        // (선택사항) 자동 로그인 로직 구현 위치
        // 현재 LoginController에서 쿠키 생성을 주석 처리했으므로,
        // 여기서도 쿠키 체크 로직은 제외하거나 주석 처리하는 것이 맞습니다.
        /*
        Cookie rememberCookie = findCookie(req.getCookies(), "rememberId");
        if (rememberCookie != null) {
            // TODO: ManagerDAO를 통해 쿠키 값으로 사용자 조회 및 세션 생성 로직 필요
            // 1. String managerId = rememberCookie.getValue();
            // 2. ManagerVO manager = managerDAO.selectOne(managerId);
            // 3. if (manager != null) { session 생성 후 chain.doFilter(); return; }
        }
        */

        // 로그인 페이지로 리다이렉트
        resp.sendRedirect(contextPath + "/login");
    }

    // 제외 경로 확인 메소드
    private boolean isExcluded(String path) {
        // 루트 경로(/)로 오면 로그인으로 보낼지 결정해야 함. 보통은 필터 타게 둠.
        if (path.equals("/")) return false;

        for (String exclude : EXCLUDE_URLS) {
            if (path.startsWith(exclude)) {
                return true;
            }
        }
        return false;
    }
}
