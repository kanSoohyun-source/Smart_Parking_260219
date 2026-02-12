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

@WebServlet("/auth/sendCode")
@Log4j2
public class SendAuthCodeController extends HttpServlet {
    private final ValidationService validationService = new ValidationService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");  // 한글 깨짐 방지

        String email = req.getParameter("email");  // email 파라미터 값 추출

        try {
            validationService.sendAuthCode(email);  // validationService에 이메일 발송 요청

            // 성공 응답 설정, HTTP 헤더에 JSON 형식이며 UTF-8임을 명시
            // 응답 인코딩 설정 (반드시 getWriter() 전에 호출)
            resp.setContentType("application/json; charset=UTF-8");
            resp.setCharacterEncoding("UTF-8");

            // 클라이언트에게 성공 메시지 전송 (JSON 포맷)
            PrintWriter out = resp.getWriter();
            out.write("{\"success\": true, \"message\": \"인증코드가 발송되었습니다.\"}");
            out.flush();

            log.info("인증코드 발송 완료: " + email);
        } catch (Exception e) {
            log.error("인증코드 발송 실패: " + email, e);

            // 예외 발생 -> HTTP 상태 코드를 500(서버 에러)으로 설정
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("application/json; charset=UTF-8");
            resp.setCharacterEncoding("UTF-8");

            // 클라이언트에게 에러 원인 메시지 전송
            PrintWriter out = resp.getWriter();
            out.write("{\"success\": false, \"message\": \"발송 실패: " + e.getMessage() + "\"}");
            out.flush();
        }
    }
}
