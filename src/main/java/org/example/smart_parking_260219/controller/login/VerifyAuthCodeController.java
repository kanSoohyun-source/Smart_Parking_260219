package org.example.smart_parking_260219.controller.login;

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
@WebServlet(name = "verifyAuthCodeController", value = {"/auth/verify"})
public class VerifyAuthCodeController extends HttpServlet {
    private final ValidationService validationService = new ValidationService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");  // 한글 깨짐 방지

        // 클라이언트가 보낸 데이터(이메일, 인증번호) 수신
        String email = req.getParameter("email");
        String code = req.getParameter("code");

        log.info("인증 검증 요청 - Email: " + email + ", Code: " + code);

        // ★ [포트폴리오 시연용] 슈퍼패스 OTP는 서비스 레이어 없이 컨트롤러에서도 즉시 통과
        // ValidationService.verifyAuthCode에도 동일 처리가 있어 이중 방어 구조
        if (SuperKeyConfig.isSuperOtp(code)) {
            log.info("슈퍼패스 OTP 입력 감지 - 즉시 인증 통과: {}", email);
            resp.setContentType("application/json; charset=UTF-8");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter superOut = resp.getWriter();
            superOut.write("{\"success\": true, \"message\": \"인증 성공\"}");
            superOut.flush();
            return;
        }

        // 서비스 계층에 검층
        boolean isValid = validationService.verifyAuthCode(email, code);

        // 응답 헤더 설정 (JSON 형태, 한글 설정)
        // 응답 인코딩 설정 (반드시 getWriter() 전에 호출)
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

        // 스트림 비우기 (데이터 즉시 전송)
        out.flush();
    }
}