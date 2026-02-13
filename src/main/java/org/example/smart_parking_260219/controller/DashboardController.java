package org.example.smart_parking_260219.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.info("=== DashboardController doGet() 호출 ===");
        // 세션 상세 로깅
        HttpSession session = request.getSession(false);  // 현재 세션이 있으면 가져오고, 없으면 null 반환
        if (session != null) {
            log.info("세션 존재: {}", session.getId());
            Object loginManager = session.getAttribute("loginManager");
            Object managerId = session.getAttribute("managerId");
            log.info("loginManager: {}", loginManager);
            log.info("managerId: {}", managerId);

            if (loginManager == null) {
                log.warn("세션은 있지만 loginManager 속성이 없음 - 로그인 페이지로 리다이렉트");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
        } else {
            log.warn("세션이 없음 - 로그인 페이지로 리다이렉트");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        log.info("대시보드 페이지로 포워딩");

        // TODO: 주차장 현황, 통계 등 대시보드에 필요한 데이터를 DAO에서 조회하여 request에 담기
        // request.setAttribute("parkingStatus", parkingService.getStatus());

        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}
