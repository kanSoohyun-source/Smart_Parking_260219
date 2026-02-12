package org.example.smart_parking_260219.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.service.ValidationService;

import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
@WebServlet("/auth/verify")
public class VerifyAuthCodeController extends HttpServlet {
    private final ValidationService validationService = new ValidationService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // ✅ 요청 인코딩 설정
        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String code = req.getParameter("code");

        log.info("인증 검증 요청 - Email: " + email + ", Code: " + code);

        boolean isValid = validationService.verifyAuthCode(email, code);

        // ✅ 응답 인코딩 설정 (반드시 getWriter() 전에 호출)
        resp.setContentType("application/json; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();

        if (isValid) {
            log.info("인증 성공: " + email);
            out.write("{\"success\": true, \"message\": \"인증 성공\"}");
        } else {
            log.warn("인증 실패: " + email);
            out.write("{\"success\": false, \"message\": \"인증 실패 또는 만료\"}");
        }

        out.flush();
    }
}
