package org.example.smart_parking_260219.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dao.ManagerDAO;
import org.example.smart_parking_260219.mail.MailService;
import org.example.smart_parking_260219.service.ValidationService;
import org.example.smart_parking_260219.util.PasswordUtil;
import org.example.smart_parking_260219.vo.ManagerVO;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;

/**
 * 비밀번호 찾기 컨트롤러
 *
 * 엔드포인트 구성:
 *  GET  /forgot-password           → 비밀번호 찾기 페이지 (find_password.jsp)
 *  POST /forgot-password/checkId   → STEP1: 아이디 존재 여부 확인 (JSON)
 *  POST /forgot-password/sendOtp   → STEP2: DB 이메일 일치 확인 + OTP 발송 (JSON)
 *  POST /forgot-password/verify    → STEP2: OTP 검증 + 임시 비밀번호 발급 (JSON)
 */
@Log4j2
@WebServlet(name = "forgotPasswordController",
        value = {"/forgot-password",
                "/forgot-password/checkId",
                "/forgot-password/sendOtp",
                "/forgot-password/verify"})
public class ForgotPasswordController extends HttpServlet {

    private final ManagerDAO        managerDAO        = ManagerDAO.getInstance();
    private final ValidationService validationService = new ValidationService();
    private final MailService       mailService       = new MailService();

    // =========================================================================
    // GET: 비밀번호 찾기 페이지 진입
    // =========================================================================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        // 이미 로그인된 상태면 대시보드로 리다이렉트
        HttpSession session = req.getSession(false);
        if (session != null && Boolean.TRUE.equals(session.getAttribute("fullyAuthenticated"))) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        req.getRequestDispatcher("/WEB-INF/views/find_password.jsp").forward(req, resp);
    }

    // =========================================================================
    // POST: 경로별 분기
    // =========================================================================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String path = req.getServletPath() +
                (req.getPathInfo() != null ? req.getPathInfo() : "");

        switch (path) {
            case "/forgot-password/checkId":
                checkId(req, resp);
                break;
            case "/forgot-password/sendOtp":
                sendOtp(req, resp);
                break;
            case "/forgot-password/verify":
                verifyAndIssue(req, resp);
                break;
            default:
                sendJson(resp, false, "잘못된 요청입니다.");
        }
    }

    // =========================================================================
    // STEP 1: 아이디 존재 여부 확인
    // =========================================================================
    private void checkId(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String managerId = req.getParameter("managerId");
        log.info("비밀번호 찾기 - 아이디 조회: {}", managerId);

        if (managerId == null || managerId.trim().isEmpty()) {
            sendJson(resp, false, "아이디를 입력해주세요.");
            return;
        }

        ManagerVO manager = managerDAO.selectOne(managerId.trim());

        if (manager == null) {
            log.warn("비밀번호 찾기 - 존재하지 않는 아이디: {}", managerId);
            sendJson(resp, false, "존재하지 않는 아이디입니다.");
            return;
        }

        if (!manager.isActive()) {
            log.warn("비밀번호 찾기 - 비활성화된 계정: {}", managerId);
            sendJson(resp, false, "비활성화된 계정입니다. 최고 관리자에게 문의하세요.");
            return;
        }

        log.info("비밀번호 찾기 - 아이디 확인 완료: {}", managerId);
        sendJson(resp, true, "아이디가 확인되었습니다.");
    }

    // =========================================================================
    // STEP 2: DB 이메일 일치 확인 후 OTP 발송
    // =========================================================================
    private void sendOtp(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String managerId = req.getParameter("managerId");
        String inputEmail = req.getParameter("email");

        log.info("비밀번호 찾기 - OTP 발송 요청 - ID: {}, Email: {}", managerId, inputEmail);

        // 입력값 검증
        if (managerId == null || managerId.trim().isEmpty()) {
            sendJson(resp, false, "아이디 정보가 없습니다. 처음부터 다시 시도해주세요.");
            return;
        }
        if (inputEmail == null || inputEmail.trim().isEmpty()) {
            sendJson(resp, false, "이메일을 입력해주세요.");
            return;
        }

        // DB에서 관리자 조회
        ManagerVO manager = managerDAO.selectOne(managerId.trim());
        if (manager == null || !manager.isActive()) {
            sendJson(resp, false, "유효하지 않은 계정입니다.");
            return;
        }

        // ✅ 핵심: DB에 저장된 이메일과 입력된 이메일 대조
        String registeredEmail = manager.getEmail();
        if (registeredEmail == null || registeredEmail.trim().isEmpty()) {
            log.error("비밀번호 찾기 - 등록된 이메일 없음 - ID: {}", managerId);
            sendJson(resp, false, "등록된 이메일 정보가 없습니다. 최고 관리자에게 문의하세요.");
            return;
        }

        if (!inputEmail.trim().equalsIgnoreCase(registeredEmail.trim())) {
            log.warn("비밀번호 찾기 - 이메일 불일치 - ID: {}, 입력: {}, 등록: {}",
                    managerId, inputEmail, registeredEmail);
            sendJson(resp, false, "등록된 이메일 주소와 일치하지 않습니다.");
            return;
        }

        // ✅ OTP 생성 및 발송 (FORGOT_PASSWORD 템플릿)
        try {
            validationService.sendAuthCode(registeredEmail.trim(), ValidationService.Purpose.FORGOT_PASSWORD);
            log.info("비밀번호 찾기 - OTP 발송 완료 - ID: {}", managerId);
            sendJson(resp, true, "인증번호가 이메일로 발송되었습니다.");
        } catch (Exception e) {
            log.error("비밀번호 찾기 - OTP 발송 실패 - ID: {}", managerId, e);
            sendJson(resp, false, "인증번호 발송에 실패했습니다. 잠시 후 다시 시도해주세요.");
        }
    }

    // =========================================================================
    // STEP 3: OTP 검증 → 임시 비밀번호 발급 및 DB 저장
    // =========================================================================
    private void verifyAndIssue(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String managerId  = req.getParameter("managerId");
        String inputEmail = req.getParameter("email");
        String inputOtp   = req.getParameter("otp");

        log.info("비밀번호 찾기 - OTP 검증 요청 - ID: {}", managerId);

        // 입력값 검증
        if (managerId == null || managerId.trim().isEmpty() ||
                inputEmail == null || inputEmail.trim().isEmpty() ||
                inputOtp   == null || inputOtp.trim().isEmpty()) {
            sendJson(resp, false, "필수 정보가 누락되었습니다.");
            return;
        }

        // ✅ OTP 검증 (ValidationService - DB 비교 + 만료 시간 체크)
        boolean otpValid = validationService.verifyAuthCode(inputEmail.trim(), inputOtp.trim());
        if (!otpValid) {
            log.warn("비밀번호 찾기 - OTP 불일치 또는 만료 - ID: {}", managerId);
            sendJson(resp, false, "인증번호가 일치하지 않거나 만료되었습니다.");
            return;
        }

        // ✅ 임시 비밀번호 생성
        String tempPassword = generateTempPassword();
        log.info("비밀번호 찾기 - 임시 비밀번호 생성 완료 - ID: {}", managerId);

        // ✅ 임시 비밀번호 DB 저장 (BCrypt 해싱은 DAO 내부에서 처리)
        try {
            managerDAO.updatePassword(managerId.trim(), tempPassword);
            log.info("비밀번호 찾기 - 임시 비밀번호 DB 저장 완료 - ID: {}", managerId);
        } catch (Exception e) {
            log.error("비밀번호 찾기 - DB 저장 실패 - ID: {}", managerId, e);
            sendJson(resp, false, "임시 비밀번호 저장에 실패했습니다. 잠시 후 다시 시도해주세요.");
            return;
        }

        // ✅ 임시 비밀번호 이메일 발송
        try {
            String title = "[Smart Parking] 임시 비밀번호가 발급되었습니다.";
            String body  = validationService.buildTempPasswordBody(tempPassword);
            mailService.sendMailWithHtml(title, body, inputEmail.trim());
            log.info("비밀번호 찾기 - 임시 비밀번호 이메일 발송 완료 - ID: {}", managerId);
        } catch (Exception e) {
            // 이메일 발송 실패해도 DB는 이미 변경됨 → 경고 로그만 남기고 성공 처리
            log.error("비밀번호 찾기 - 임시 비밀번호 이메일 발송 실패 - ID: {}", managerId, e);
        }

        sendJson(resp, true, "임시 비밀번호가 이메일로 발송되었습니다.");
    }

    // =========================================================================
    // 임시 비밀번호 생성
    // 영문 대소문자 + 숫자 조합, 10자리
    // =========================================================================
    private String generateTempPassword() {
        final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";
        // 혼동 문자(0, O, 1, I, l) 제외
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    // =========================================================================
    // JSON 응답 헬퍼
    // =========================================================================
    private void sendJson(HttpServletResponse resp, boolean success, String message)
            throws IOException {
        PrintWriter out = resp.getWriter();
        // message 내 큰따옴표 이스케이프
        String safeMessage = message.replace("\"", "\\\"");
        out.write("{\"success\":" + success + ",\"message\":\"" + safeMessage + "\"}");
        out.flush();
    }
}