package org.example.smart_parking_260219.controller.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
@WebServlet(name = "logoutController", value = {"/logout"})
public class LogoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);  // 기존 세션이 없으면 null 반환

        if (session != null) {
            String managerId = (String) session.getAttribute("managerId");
            log.info("로그아웃 처리 - ID: {}", managerId);
            session.invalidate();  // 세션 무효화
        }
        // 로그아웃 -> 로그인 페이지로 리다이렉트
        response.sendRedirect(request.getContextPath() + "/login");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // POST 요청도 동일하게 처리
        doGet(request, response);
    }
}
