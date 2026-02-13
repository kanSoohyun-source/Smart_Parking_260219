package org.example.smart_parking_260219.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dao.ManagerDAO;
import org.example.smart_parking_260219.vo.ManagerVO;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

@Log4j2
@WebServlet({"/login", "/login/verifyEmail", "/login/sendLoginOtp", "/login/verifyEmailOtp"})
public class LoginController extends HttpServlet {

    private final ManagerDAO managerDAO = ManagerDAO.getInstance();

    @Override
    /* 로그인 폼 요청 처리 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();

        // /login/verifyEmail 또는 /login/verifyEmailOtp 경로로 직접 접근 시도 차단
        if ("/login/verifyEmail".equals(servletPath) || "/login/verifyEmailOtp".equals(servletPath)) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("loginManager") == null) {
                log.warn("2차 인증 페이지 직접 접근 시도 차단");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            // 세션이 있으면 해당 2차 인증 페이지로 포워딩
            log.info("2차 인증 페이지로 포워딩");
            if ("/login/verifyEmail".equals(servletPath)) {
                request.getRequestDispatcher("/WEB-INF/views/login_email.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/WEB-INF/views/login_email_otp.jsp").forward(request, response);
            }
            return;
        }

        // 세션 확인 - 이미 로그인된 경우 대시보드로 리다이렉트 (중복 로그인 방지)
        HttpSession session = request.getSession(false);  // 기존 세션이 없으면 null 반환
        if (session != null && session.getAttribute("loginManager") != null) {
            Boolean fullyAuth = (Boolean) session.getAttribute("fullyAuthenticated");
            if (fullyAuth != null && fullyAuth) {
                log.info("이미 로그인된 사용자 - 대시보드로 리다이렉트");
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }
        }
        // 로그인하지 않은 상태라면 로그인 페이지(jsp) 페이지로 포워딩
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    /* 로그인 데이터 처리 */
    // 1차 로그인 데이터 처리 및 2차 인증
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");  // 한글 깨짐 방지

        String servletPath = request.getServletPath();
        log.info("doPost 호출 - servletPath: {}", servletPath);

        // 이메일 인증 처리 (일반 관리자)
        if ("/login/verifyEmail".equals(servletPath)) {
            log.info("일반관리자 이메일 인증 처리 시작");
            verifyEmail(request, response);
            return;
        }

        // OTP 발송 처리 (최고 관리자)
        if ("/login/sendLoginOtp".equals(servletPath)) {
            log.info("OTP 발송 처리 시작");
            sendLoginOtp(request, response);
            return;
        }

        // 이메일 + OTP 인증 처리 (최고 관리자)
        if ("/login/verifyEmailOtp".equals(servletPath)) {
            log.info("최고관리자 이메일+OTP 인증 처리 시작");
            verifyEmailOtp(request, response);
            return;
        }

        // 1차 인증 처리 (기존 로직)
        String managerId = request.getParameter("id");  //<input name="id"> 값 가져오기
        String password = request.getParameter("pw");  //<input name="pw"> 값 가져오기

        log.info("로그인 시도 - ID: {}", managerId);

        // 입력값 검증
        if (managerId == null || managerId.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "아이디와 비밀번호를 입력해주세요.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        try {
            // DB에서 관리자 정보 조회 (1차 인증)
            ManagerVO manager = managerDAO.selectOne(managerId);

            // 계정 존재 여부 확인
            if (manager == null) {
                log.warn("존재하지 않는 관리자 ID: {}", managerId);
                request.setAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }

            // 계정 활성화 여부 확인
            if (!manager.isActive()) {
                log.warn("비활성화된 계정 로그인 시도: {}", managerId);
                request.setAttribute("error", "비활성화된 계정입니다.<br> 관리자에게 문의하세요.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }

            // 비밀번호 검증 (암호화된 비밀번호와 비교)
            String encodedPassword = managerDAO.passEncode(password);
            if (!manager.getPassword().equals(encodedPassword)) {
                log.warn("비밀번호 불일치 - ID: {}", managerId);
                request.setAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }
            // 임시 테스트용 (DB에 1234가 평문으로 있을 때)
//            if (manager.getPassword().equals(password)) {
//                // 성공 처리
//                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
//                return;
//            }

            log.info("1차 인증 성공: {}, 권한: {}", managerId, manager.getRole());

            // 로그인 성공 - 세션 생성
            // 세션에 임시 정보 저장 (2차 인증 전 단계)
            HttpSession session = request.getSession();
            session.setAttribute("loginManager", manager);  // 객체 전체 저장
            session.setAttribute("managerId", manager.getManagerId());  // ID 별도 저장
            session.setAttribute("managerName", manager.getManagerName());  // 이름 별도 저장
            session.setAttribute("managerRole", manager.getRole());  // 관리자 권한 저장

            // 2차 인증 대기 상태 플래그 설정
            session.setAttribute("awaitingSecondAuth", true);

            // 권한(Role)에 따른 2차 인증 페이지 분기
            if ("ADMIN".equals(manager.getRole())) {
                // 최고관리자: 이메일 + OTP
                log.info("최고관리자 2차 인증(이메일+OTP) 단계로 이동");
                // WEB-INF 내부에 있는 파일은 forward로만 접근 가능
                request.getRequestDispatcher("/WEB-INF/views/login_email_otp.jsp").forward(request, response);
            } else {
                log.info("일반관리자 2차 인증(이메일) 단계로 이동");
                request.getRequestDispatcher("/WEB-INF/views/login_email.jsp").forward(request, response);
            }
            // 세션 타임아웃 설정 (30분), 임의로 3분 설정해봄
            session.setMaxInactiveInterval(3 * 60);

            // 대시보드로 리다이렉트
//            response.sendRedirect(request.getContextPath() + "/dashboard");

        } catch (Exception e) {
            log.error("로그인 처리 중 오류 발생", e);
            request.setAttribute("error", "시스템 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }

    /* 2차 인증 - 이메일 확인 처리 */
    private void verifyEmail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.info("verifyEmail, 이메일 확인 처리 메서드 시작");

        HttpSession session = request.getSession(false);

        // 세션 검증
        if (session == null) {
            log.warn("세션이 null입니다");
            request.setAttribute("error", "세션이 만료되었습니다. 다시 로그인해주세요.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        ManagerVO manager = (ManagerVO) session.getAttribute("loginManager");
        if (manager == null) {
            log.warn("세션에 loginManager가 없습니다");
            request.setAttribute("error", "세션이 만료되었습니다. 다시 로그인해주세요.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        // 1차 인증 완료 여부 확인
        Boolean awaitingSecondAuth = (Boolean) session.getAttribute("awaitingSecondAuth");
        if (awaitingSecondAuth == null || !awaitingSecondAuth) {
            log.warn("2차 인증 시도 - 1차 인증 미완료");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String inputEmail = request.getParameter("email");
        log.info("입력받은 이메일: {}", inputEmail);

        // 입력값 검증
        if (inputEmail == null || inputEmail.trim().isEmpty()) {
            log.warn("이메일이 입력되지 않음");
            request.setAttribute("error", "이메일을 입력해주세요.");
            request.getRequestDispatcher("/WEB-INF/views/login_email.jsp").forward(request, response);
            return;
        }

        inputEmail = inputEmail.trim().toLowerCase(); // 이메일은 대소문자 구분 없이 처리

        // 세션에서 관리자 정보 가져오기
        String registeredEmail = manager.getEmail();
        log.info("등록된 이메일: {}", registeredEmail);

        if (registeredEmail == null || registeredEmail.trim().isEmpty()) {
            log.error("관리자 이메일 정보 없음 - ID: {}", manager.getManagerId());
            request.setAttribute("error", "등록된 이메일 정보가 없습니다. 관리자에게 문의하세요.");
            request.getRequestDispatcher("/WEB-INF/views/login_email.jsp").forward(request, response);
            return;
        }

        registeredEmail = registeredEmail.trim().toLowerCase();

        // 이메일 일치 여부 확인
        if (!inputEmail.equals(registeredEmail)) {
            log.warn("이메일 불일치 - ID: {}, 입력: {}, 등록: {}",
                    manager.getManagerId(), inputEmail, registeredEmail);
            request.setAttribute("error", "등록된 이메일 주소와 일치하지 않습니다.");
            request.getRequestDispatcher("/WEB-INF/views/login_email.jsp").forward(request, response);
            return;
        }

        log.info("2차 인증 성공 - ID: {}, 이메일: {}", manager.getManagerId(), inputEmail);

        // 2차 인증 완료 - 플래그 제거 및 완전한 로그인 상태로 전환
        session.removeAttribute("awaitingSecondAuth");
        session.setAttribute("fullyAuthenticated", true);
        // 세션 타임아웃 설정 (30분)
        session.setMaxInactiveInterval(30 * 60);

        // 로그인 완료 - 대시보드로 리다이렉트
        log.info("로그인 완료 - 대시보드로 이동: {}", manager.getManagerId());
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    /* OTP 발송 처리 (최고 관리자) */
    private void sendLoginOtp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session = request.getSession(false);

            if (session == null || session.getAttribute("loginManager") == null) {
                log.warn("OTP 발송 요청 - 유효하지 않은 세션");
                out.print("{\"success\":false,\"message\":\"세션이 만료되었습니다.\"}");
                return;
            }

            ManagerVO manager = (ManagerVO) session.getAttribute("loginManager");
            String inputEmail = request.getParameter("email");

            log.info("OTP 발송 요청 - ID: {}, 입력 이메일: {}", manager.getManagerId(), inputEmail);

            if (inputEmail == null || inputEmail.trim().isEmpty()) {
                out.print("{\"success\":false,\"message\":\"이메일을 입력해주세요.\"}");
                return;
            }

            String registeredEmail = manager.getEmail();

            if (registeredEmail == null || registeredEmail.trim().isEmpty()) {
                log.error("등록된 이메일 없음 - ID: {}", manager.getManagerId());
                out.print("{\"success\":false,\"message\":\"등록된 이메일 정보가 없습니다.\"}");
                return;
            }

            // 이메일 일치 확인 (대소문자 구분 없이)
            if (!inputEmail.trim().equalsIgnoreCase(registeredEmail.trim())) {
                log.warn("이메일 불일치 - 입력: {}, 등록: {}", inputEmail, registeredEmail);
                out.print("{\"success\":false,\"message\":\"등록된 이메일 주소와 일치하지 않습니다.\"}");
                return;
            }

            // 6자리 OTP 생성
            String otp = generateOTP();
            log.info("OTP 생성 완료 - ID: {}, OTP: {}", manager.getManagerId(), otp);

            // 세션에 OTP 저장 (5분 유효)
            session.setAttribute("loginOtp", otp);
            session.setAttribute("otpGeneratedTime", System.currentTimeMillis());
            session.setAttribute("otpVerifiedEmail", inputEmail.trim().toLowerCase());

            // 실제 환경에서는 이메일 발송 서비스 호출
            // EmailService.sendOTP(inputEmail, otp);

            // 개발/테스트 환경: 콘솔에 OTP 출력
            log.info("========================================");
            log.info("테스트용 OTP: {}", otp);
            log.info("발송 대상: {}", inputEmail);
            log.info("========================================");

            out.print("{\"success\":true,\"message\":\"인증번호가 발송되었습니다.\"}");

        } catch (Exception e) {
            log.error("OTP 발송 중 오류", e);
            out.print("{\"success\":false,\"message\":\"OTP 발송 중 오류가 발생했습니다.\"}");
        }
    }

    /**
     * 이메일 + OTP 인증 처리 (최고 관리자)
     */
    private void verifyEmailOtp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.info("verifyEmailOtp 메서드 시작");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loginManager") == null) {
            log.warn("유효하지 않은 세션");
            request.setAttribute("error", "세션이 만료되었습니다. 다시 로그인해주세요.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        ManagerVO manager = (ManagerVO) session.getAttribute("loginManager");
        String inputEmail = request.getParameter("email");
        String inputOtp = request.getParameter("otp");

        log.info("입력 - 이메일: {}, OTP: {}", inputEmail, inputOtp);

        // 입력값 검증
        if (inputEmail == null || inputEmail.trim().isEmpty()) {
            request.setAttribute("error", "이메일을 입력해주세요.");
            request.getRequestDispatcher("/WEB-INF/views/login_email_otp.jsp").forward(request, response);
            return;
        }

        if (inputOtp == null || inputOtp.trim().isEmpty()) {
            request.setAttribute("error", "인증번호를 입력해주세요.");
            request.getRequestDispatcher("/WEB-INF/views/login_email_otp.jsp").forward(request, response);
            return;
        }

        // 세션에 저장된 OTP 정보 확인
        String sessionOtp = (String) session.getAttribute("loginOtp");
        String otpVerifiedEmail = (String) session.getAttribute("otpVerifiedEmail");
        Long otpGeneratedTime = (Long) session.getAttribute("otpGeneratedTime");

        if (sessionOtp == null || otpVerifiedEmail == null || otpGeneratedTime == null) {
            log.warn("OTP 정보 없음 - 먼저 인증번호를 발송받아야 함");
            request.setAttribute("error", "먼저 인증번호를 발송받아주세요.");
            request.getRequestDispatcher("/WEB-INF/views/login_email_otp.jsp").forward(request, response);
            return;
        }

        // OTP 유효 시간 확인 (5분)
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - otpGeneratedTime;
        if (elapsedTime > 5 * 60 * 1000) {
            log.warn("OTP 만료 - 경과 시간: {}ms", elapsedTime);
            session.removeAttribute("loginOtp");
            session.removeAttribute("otpGeneratedTime");
            session.removeAttribute("otpVerifiedEmail");
            request.setAttribute("error", "인증번호가 만료되었습니다. 다시 발송받아주세요.");
            request.getRequestDispatcher("/WEB-INF/views/login_email_otp.jsp").forward(request, response);
            return;
        }

        // 이메일 일치 확인
        if (!inputEmail.trim().equalsIgnoreCase(otpVerifiedEmail)) {
            log.warn("이메일 불일치 - 입력: {}, OTP 발송: {}", inputEmail, otpVerifiedEmail);
            request.setAttribute("error", "인증번호를 발송받은 이메일과 일치하지 않습니다.");
            request.getRequestDispatcher("/WEB-INF/views/login_email_otp.jsp").forward(request, response);
            return;
        }

        // OTP 일치 확인
        if (!inputOtp.trim().equals(sessionOtp)) {
            log.warn("OTP 불일치 - 입력: {}, 저장: {}", inputOtp, sessionOtp);
            request.setAttribute("error", "인증번호가 일치하지 않습니다.");
            request.getRequestDispatcher("/WEB-INF/views/login_email_otp.jsp").forward(request, response);
            return;
        }

        log.info("이메일+OTP 인증 성공 - ID: {}", manager.getManagerId());

        // 2차 인증 완료 - OTP 정보 삭제
        session.removeAttribute("loginOtp");
        session.removeAttribute("otpGeneratedTime");
        session.removeAttribute("otpVerifiedEmail");
        session.removeAttribute("awaitingSecondAuth");
        session.setAttribute("fullyAuthenticated", true);
        session.setMaxInactiveInterval(30 * 60);

        log.info("최고관리자 로그인 완료 - 대시보드로 리다이렉트: {}", manager.getManagerId());
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    /**
     * 6자리 랜덤 OTP 생성
     */
    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    /* 에러 메시지 처리를 위한 공통 메서드 */
    private void sendError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
}