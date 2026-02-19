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
import org.example.smart_parking_260219.util.PasswordUtil;
import org.example.smart_parking_260219.vo.ManagerVO;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

@Log4j2
@WebServlet(name = "loginController", value = {"/login", "/login/verifyEmail", "/login/sendLoginOtp", "/login/verifyEmailOtp"})
public class LoginController extends HttpServlet {

    private final ManagerDAO managerDAO = ManagerDAO.getInstance();
    private final MailService mailService = new MailService(); // MailService 인스턴스 추가

    @Override
    /* 로그인 폼 요청 처리 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();  // 사용자가 들어온 경로 확인

        // 로그인 없이, /login/verifyEmail 또는 /login/verifyEmailOtp 경로로 직접 접근 시도 차단
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

        String servletPath = request.getServletPath();  // 사용자가 들어온 경로 확인
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

        // 1차 인증 처리 (기존 로직) -> 파라미터 수집, JSP의 <input name=" "> 값 가져옴
        String managerId = request.getParameter("id");
        String password = request.getParameter("pw");

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
            // DAO를 통해 아이디에 해다하는 관리자 객체(VO) 가져옴
            ManagerVO manager = managerDAO.selectOne(managerId);

            // 계정 존재 여부 확인
            if (manager == null) {
                log.warn("존재하지 않는 관리자 ID: {}", managerId);
                request.setAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }

            // 계정 활성화(active) 여부 확인
            if (!manager.isActive()) {
                log.warn("비활성화된 계정 로그인 시도: {}", managerId);
                request.setAttribute("error", "비활성화된 계정입니다.<br> 관리자에게 문의하세요.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }

            // BCrypt로 비밀번호 검증
            // 입력된 평문 암호와 DB의 암호화를 BCrypt로 비교
            boolean passwordMatch = PasswordUtil.checkPassword(password, manager.getPassword());

            // 암호화된 비밀번호 확인
            if (!passwordMatch) {
                log.warn("비밀번호 불일치 - ID: {}", managerId);
                request.setAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }

            log.info("1차 인증 성공: {}, 권한: {}", managerId, manager.getRole());

            // 로그인 성공 - 세션 생성
            // 세션에 임시 정보 저장 (2차 인증 전 단계)
            HttpSession session = request.getSession();
            session.setAttribute("loginManager", manager);  // 객체 전체 저장 -> 2차 인증에서 꺼내 쓰기 위해 선언
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
            // 세션 타임아웃 설정 (30분) - 테스트 해봐야함.
            session.setMaxInactiveInterval(30 * 60);

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

        // 기존 세션 가져오기 (없으면 null 반환)
        // 1차 인증 때 이미 세션이 생성되었어야 하므로, 여기서 null이면 비정상적인 접근임
        HttpSession session = request.getSession(false);

        // 세션 존재 여부 검증
        if (session == null) {
            log.warn("세션이 null입니다");
            request.setAttribute("error", "세션이 만료되었습니다. 다시 로그인해주세요.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        // 세션에 저장된 관리자 객체 꺼내기
        // 1차 로그인 성공 시 doPost에 저장했던 "loginManager" 객체를 가져옴
        ManagerVO manager = (ManagerVO) session.getAttribute("loginManager");

        if (manager == null) {
            log.warn("세션에 loginManager 정보 없음");
            request.setAttribute("error", "세션 정보가 없습니다. 다시 로그인해주세요.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        // 사용자 입력 값 - JSP 화면에서 사용자가 입력한 이메일 값 가져옴, <input name = "email">
        String inputEmail = request.getParameter("email");
        log.info("입력된 이메일: {}", inputEmail);

        // 입력값 검증 - 이메일을 입력하지 않았을 경우 다시 이메일 입력 페이지로 포워딩
        if (inputEmail == null || inputEmail.trim().isEmpty()) {
            log.warn("이메일 입력 없음");
            request.setAttribute("error", "이메일을 입력해주세요.");
            request.getRequestDispatcher("/WEB-INF/views/login_email.jsp").forward(request, response);
            return;
        }

        // DB 값 비교, 세션 내 manager 객체에 저장된 실제 이메일 주소를 가져옴
        String registeredEmail = manager.getEmail();
        log.info("등록된 이메일: {}", registeredEmail);

        // DB에 등록된 이메일 정보가 없으면 에러 처리
        if (registeredEmail == null || registeredEmail.trim().isEmpty()) {
            log.error("DB에 등록된 이메일 없음 - ID: {}", manager.getManagerId());
            request.setAttribute("error", "등록된 이메일 정보가 없습니다. 관리자에게 문의하세요.");
            request.getRequestDispatcher("/WEB-INF/views/login_email.jsp").forward(request, response);
            return;
        }

        // 이메일 일치 확인 (대소문자 구분 없이)
        // 사용자가 입력한 이메일(inputEmail) & DB에 등록된 이메일(registeredEmail) 비교
        if (!inputEmail.trim().equalsIgnoreCase(registeredEmail.trim())) {
            log.warn("이메일 불일치 - ID: {}, 입력: {}, 등록: {}",
                    manager.getManagerId(), inputEmail, registeredEmail);
            request.setAttribute("error", "등록된 이메일 주소와 일치하지 않습니다.");
            request.getRequestDispatcher("/WEB-INF/views/login_email.jsp").forward(request, response);
            return;
        }

        log.info("2차 인증 성공 - ID: {}, 이메일: {}", manager.getManagerId(), inputEmail);

        // 2차 인증 완료
        // 2차 인증 대기 임시 플래그 제거
        session.removeAttribute("awaitingSecondAuth");
        // 1차(ID/PW), 2차(OTP) 모두 통과한 '완전 인증' 표시를 저장
        session.setAttribute("fullyAuthenticated", true);

        // 세션 타임아웃 설정 (30분)
        // session.setMaxInactiveInterval(30 * 60);

        // 로그인 완료 - 대시보드로 리다이렉트
        /** 최종 merge 진행 시 경로 재설정 */
        log.info("로그인 완료 - 대시보드로 이동: {}", manager.getManagerId());
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    /* OTP 발송 처리 (최고 관리자) - 네이버 이메일로 실제 발송 */
    private void sendLoginOtp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");  // 응답을 HTML이 아닌 JSON 형태로 보냄
        response.setCharacterEncoding("UTF-8");  // 한글 깨짐 방지
        PrintWriter out = response.getWriter();  // 글자를 써서 보낼 펜 역할을 하는 객체

        try {
            HttpSession session = request.getSession(false);  // 기존 세션 확인, 없으면 null

            if (session == null || session.getAttribute("loginManager") == null) {
                log.warn("OTP 발송 요청 - 유효하지 않은 세션");
                out.print("{\"success\":false,\"message\":\"세션이 만료되었습니다.\"}");
                return;
            }

            ManagerVO manager = (ManagerVO) session.getAttribute("loginManager");
            String inputEmail = request.getParameter("email");  // JSP 파일에서 입력한 이메일

            log.info("OTP 발송 요청 - ID: {}, 입력 이메일: {}", manager.getManagerId(), inputEmail);

            if (inputEmail == null || inputEmail.trim().isEmpty()) {
                out.print("{\"success\":false,\"message\":\"이메일을 입력해주세요.\"}");
                return;
            }

            // 1차 로그인한 계정의 DB 저장 이메일(registeredEmail)과 지금 입력한 이메일이 같은지 확인
            String registeredEmail = manager.getEmail();

            if (registeredEmail == null || registeredEmail.trim().isEmpty()) {
                log.error("등록된 이메일 없음 - ID: {}", manager.getManagerId());
                out.print("{\"success\":false,\"message\":\"등록된 이메일 정보가 없습니다.\"}");
                return;
            }

            // 이메일 일치 확인 (대소문자 구분 없이)
            // 일치하지 않을 경우, 실패 메시지를 JSON으로 보냄
            if (!inputEmail.trim().equalsIgnoreCase(registeredEmail.trim())) {
                log.warn("이메일 불일치 - 입력: {}, 등록: {}", inputEmail, registeredEmail);
                out.print("{\"success\":false,\"message\":\"등록된 이메일 주소와 일치하지 않습니다.\"}");
                return;
            }

            // 6자리 OTP 생성
            String otp = generateOTP();
            log.info("OTP 생성 완료 - ID: {}, OTP: {}", manager.getManagerId(), otp);

            // 세션에 OTP 저장 (5분 유효)
            session.setAttribute("loginOtp", otp);  // 생성 OTP를 세션에 임시 보관
            session.setAttribute("otpGeneratedTime", System.currentTimeMillis());  // 생성 시간 설정
            session.setAttribute("otpVerifiedEmail", inputEmail.trim().toLowerCase());  // 인증된 이메일 주소 저장

            // 실제 네이버 이메일로 OTP 발송
            try {
                String emailTitle = "[보안인증] 로그인 인증번호";
                // buildOtpEmailContent : HTML 디자인이 입혀진 이메일 본문을 만드는 메서드 호출
                String emailBody = buildOtpEmailContent(manager.getManagerName(), otp);

                // mailService를 통해 실제 메일 전송
                mailService.sendMailWithHtml(emailTitle, emailBody, inputEmail);

                log.info("OTP 이메일 발송 성공, 유효시간 5분");
                log.info("📧 수신 이메일: {}, 🔐 OTP 코드: {}", inputEmail, otp);

                // 전송 성공 시 화면(JavaScript)에 성공 메시지 전달
                out.print("{\"success\":true,\"message\":\"인증번호가 이메일로 발송되었습니다.\"}");

            } catch (Exception emailError) {
                log.error("이메일 발송 실패", emailError);
                out.print("{\"success\":false,\"message\":\"이메일 발송에 실패했습니다. 잠시 후 다시 시도해주세요.\"}");
                return;
            }

        } catch (Exception e) {
            log.error("OTP 발송 중 오류", e);
            out.print("{\"success\":false,\"message\":\"OTP 발송 중 오류가 발생했습니다.\"}");
        }
    }

    /* 이메일 + OTP 인증 처리 (최고 관리자) */
    private void verifyEmailOtp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.info("verifyEmailOtp 메서드 시작");

        HttpSession session = request.getSession(false);  // 기존 세션 가져오기

        if (session == null || session.getAttribute("loginManager") == null) {
            log.warn("유효하지 않은 세션");
            request.setAttribute("error", "세션이 만료되었습니다. 다시 로그인해주세요.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        ManagerVO manager = (ManagerVO) session.getAttribute("loginManager");
        // 사용자가 화면 <input>에 입력한 이메일과 OTP 번호 가져옴
        String inputEmail = request.getParameter("email");
        String inputOtp = request.getParameter("otp");

        log.info("입력 - 이메일: {}, OTP: {}", inputEmail, inputOtp);

        // 입력값 검증
        // 아무것도 입력 하지 않고 '인증' 눌렀을 경우를 대비
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
        // sendLoginOtp 메서드에서 메모해두었던 진짜 인증 번호 꺼냄
        String sessionOtp = (String) session.getAttribute("loginOtp");
        String otpVerifiedEmail = (String) session.getAttribute("otpVerifiedEmail");
        Long otpGeneratedTime = (Long) session.getAttribute("otpGeneratedTime");

        // 메일 발송 누르지도 않고 인증 시도 -> 세션값(메모)가 없으므로 에러 발생
        if (sessionOtp == null || otpVerifiedEmail == null || otpGeneratedTime == null) {
            log.warn("OTP 정보 없음 - 먼저 인증번호를 발송받아야 함");
            request.setAttribute("error", "먼저 인증번호를 발송받아주세요.");
            request.getRequestDispatcher("/WEB-INF/views/login_email_otp.jsp").forward(request, response);
            return;
        }

        // OTP 유효 시간 확인 (5분)
        long currentTime = System.currentTimeMillis();  // 지금시간
        long elapsedTime = currentTime - otpGeneratedTime;  // 경과 시간
        if (elapsedTime > 5 * 60 * 1000) {
            log.warn("OTP 만료 - 경과 시간: {}ms", elapsedTime);
            // 만료 시 세션에 있는 OTP 정보를 싹 지워서 보안을 유지
            session.removeAttribute("loginOtp");
            session.removeAttribute("otpGeneratedTime");
            session.removeAttribute("otpVerifiedEmail");
            request.setAttribute("error", "인증번호가 만료되었습니다. 다시 발송받아주세요.");
            request.getRequestDispatcher("/WEB-INF/views/login_email_otp.jsp").forward(request, response);
            return;
        }

        // 이메일 일치 확인 (인증번호를 받은 이메일 & 지금 입력한 이메일 비교)
        if (!inputEmail.trim().equalsIgnoreCase(otpVerifiedEmail)) {
            log.warn("이메일 불일치 - 입력: {}, OTP 발송: {}", inputEmail, otpVerifiedEmail);
            request.setAttribute("error", "인증번호를 발송받은 이메일과 일치하지 않습니다.");
            request.getRequestDispatcher("/WEB-INF/views/login_email_otp.jsp").forward(request, response);
            return;
        }

        // OTP 일치 확인 (사용자가 입력한 번호 & 서버가 보낸 번호 비교)
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
        session.setAttribute("fullyAuthenticated", true);  // 모든 인증을 마친 사용자임을 표시
        session.setMaxInactiveInterval(30 * 60);  // 로그인 유지 시간 설정

        log.info("최고관리자 로그인 완료 - 대시보드로 리다이렉트: {}", manager.getManagerId());
        /** 최종 merge 이후 해당 경로 수정 */
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    /* 6자리 랜덤 OTP 생성 */
    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    /* OTP 이메일 HTML 템플릿 생성  */
    // 최고 관리자 전용 - 로그인 인증 이메일 HTML
    private String buildOtpEmailContent(String managerName, String otp) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <style>" +
                "        body { font-family: 'Malgun Gothic', '맑은 고딕', sans-serif; line-height: 1.6; margin: 0; padding: 0; }" +
                "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }" +
                "        .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }" +
                "        .otp-box { background: white; border: 2px dashed #667eea; padding: 20px; margin: 20px 0; text-align: center; border-radius: 8px; }" +
                "        .otp-code { font-size: 36px; font-weight: bold; color: #667eea; letter-spacing: 8px; margin: 15px 0; }" +
                "        .warning { background: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0; }" +
                "        .footer { text-align: center; color: #666; font-size: 12px; margin-top: 20px; padding-top: 20px; border-top: 1px solid #ddd; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>" +
                "            <h1 style='margin: 0;'>🔐 로그인 인증번호</h1>" +
                "            <p style='margin: 10px 0 0 0;'>Smart Parking 관리자 시스템</p>" +
                "        </div>" +
                "        <div class='content'>" +
                "            <p>안녕하세요, <strong>" + managerName + "</strong>님</p>" +
                "            <p>귀하의 계정으로 로그인을 시도하고 있습니다.</p>" +
                "            <p>아래의 인증번호를 입력하여 로그인을 완료해주세요.</p>" +
                "            " +
                "            <div class='otp-box'>" +
                "                <p style='margin: 0; color: #666; font-size: 14px;'>인증번호</p>" +
                "                <div class='otp-code'>" + otp + "</div>" +
                "                <p style='margin: 0; color: #666; font-size: 14px;'>유효시간: <strong>5분</strong></p>" +
                "            </div>" +
                "            " +
                "            <div class='warning'>" +
                "                <strong>⚠️ 보안 안내</strong><br>" +
                "                • 본인이 요청하지 않은 경우 이 이메일을 무시하세요.<br>" +
                "                • 인증번호는 타인에게 절대 알려주지 마세요.<br>" +
                "                • 인증번호는 5분간 유효합니다." +
                "            </div>" +
                "            " +
                "            <p>감사합니다.</p>" +
                "            <p><strong>Smart Parking 관리팀</strong></p>" +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p>본 메일은 발신 전용입니다. 문의사항은 관리자에게 연락해주세요.</p>" +
                "            <p style='margin-top: 5px;'>© 2026 Smart Parking System. All rights reserved.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    /* 에러 메시지 처리를 위한 공통 메서드 */
    private void sendError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
}