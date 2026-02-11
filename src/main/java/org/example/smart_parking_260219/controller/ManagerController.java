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

@Log4j2
@WebServlet("/mgr/*")
public class ManagerController extends HttpServlet {

    private ManagerDAO managerDAO = ManagerDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.info("=== ManagerController doGet() 호출 ===");
        log.info("Request URI: {}", request.getRequestURI());
        log.info("Context Path: {}", request.getContextPath());
        log.info("Servlet Path: {}", request.getServletPath());
        log.info("Path Info: {}", request.getPathInfo());

        // 세션 체크 (로그인 안되어있으면 "/login"으로 추방)
        HttpSession session = request.getSession(false);
        if (session != null) {
            log.info("세션 ID: {}", session.getId());
            Object loginManager = session.getAttribute("loginManager");
            log.info("loginManager: {}", loginManager);
        } else {
            log.warn("세션이 없음");
        }

        if (session == null || session.getAttribute("loginManager") == null) {
            log.warn("미인증 - 로그인 페이지로 리다이렉트");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // pathInfo 사용
        String pathInfo = request.getPathInfo();
        log.info("처리할 pathInfo: {}", pathInfo);

        if (pathInfo == null || pathInfo.equals("/")) {
            pathInfo = "/list";
            log.info("pathInfo가 null이거나 / - /list로 변경");
        }

        switch (pathInfo) {
            /* 추가 */
            case "/add":
                log.info("관리자 추가 페이지로 포워딩");
                request.getRequestDispatcher("/WEB-INF/views/mgr_add.jsp").forward(request, response);
                break;

            /* 수정 */
            case "/view":
                log.info("관리자 수정 페이지 처리");
                request.getRequestDispatcher("/WEB-INF/views/mgr_view.jsp").forward(request, response);
                break;

            /* 정의되지 않은 경로는 404 에러 */
            default:
                log.warn("알 수 없는 경로: {}", pathInfo);
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.info("=== ManagerController doPost() 호출 ===");
        log.info("Path Info: {}", request.getPathInfo());

        request.setCharacterEncoding("UTF-8");  //한글 깨짐 방지

        // 세션 체크
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginManager") == null) {
            log.warn("미인증 POST 요청 - 로그인 페이지로 리다이렉트");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String pathInfo = request.getPathInfo();

        if ("/add".equals(pathInfo)) {
            log.info("관리자 추가 처리 시작");
            addManager(request, response);  //관리자 등록
        } else if ("/toggleActive".equals(pathInfo)) {
            log.info("관리자 활성화/비활성화 처리");
            toggleManagerActive(request, response);  //활성/비활성화 전환
        } else {
            log.warn("알 수 없는 POST 경로: {}", pathInfo);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /* 관리자 추가 처리 */
    private void addManager(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String managerId = request.getParameter("id");
        String managerName = request.getParameter("name");
        String password = request.getParameter("pw");
        String passwordConfirm = request.getParameter("passwordConfirm");
        String email = request.getParameter("email");

        log.info("관리자 추가 요청 - ID: {}, 이름: {}, 이메일: {}", managerId, managerName, email);

        // 입력값 검증
        if (managerId == null || managerId.trim().isEmpty() ||
                managerName == null || managerName.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                email == null || email.trim().isEmpty()) {

            log.warn("필수 입력값 누락");
            request.setAttribute("error", "모든 필드를 입력해주세요.");
            request.setAttribute("managerId", managerId);
            request.setAttribute("managerName", managerName);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/mgr_add.jsp").forward(request, response);
            return;
        }

        // 비밀번호 일치 확인
        if (!password.equals(passwordConfirm)) {
            log.warn("비밀번호 불일치");
            request.setAttribute("error", "비밀번호가 일치하지 않습니다.");
            request.setAttribute("managerId", managerId);
            request.setAttribute("managerName", managerName);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/mgr_add.jsp").forward(request, response);
            return;
        }

        // 비밀번호 길이 검증
        if (password.length() < 4) {
            log.warn("비밀번호 길이 부족: {}", password.length());
            request.setAttribute("error", "비밀번호는 최소 4자 이상이어야 합니다.");
            request.setAttribute("managerId", managerId);
            request.setAttribute("managerName", managerName);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/mgr_add.jsp").forward(request, response);
            return;
        }

        // 이메일 형식 검증
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            log.warn("잘못된 이메일 형식: {}", email);
            request.setAttribute("error", "올바른 이메일 형식이 아닙니다.");
            request.setAttribute("managerId", managerId);
            request.setAttribute("managerName", managerName);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/mgr_add.jsp").forward(request, response);
            return;
        }

        try {
            // 중복 ID 체크
            ManagerVO existingManager = managerDAO.selectOne(managerId);
            if (existingManager != null) {
                log.warn("중복된 관리자 ID: {}", managerId);
                request.setAttribute("error", "이미 사용 중인 아이디입니다.");
                request.setAttribute("managerId", managerId);
                request.setAttribute("managerName", managerName);
                request.setAttribute("email", email);
                request.getRequestDispatcher("/WEB-INF/views/mgr_add.jsp").forward(request, response);
                return;
            }

            // 비밀번호 암호화
            String encodedPassword = managerDAO.passEncode(password);
            log.info("비밀번호 암호화 완료");

            // 관리자 VO 생성
            ManagerVO newManager = ManagerVO.builder()
                    .managerId(managerId)
                    .managerName(managerName)
                    .password(encodedPassword)
                    .email(email)
                    .active(true)
                    .build();

            // DB에 저장
            managerDAO.insertManager(newManager);
            log.info("관리자 추가 성공 - ID: {}, 이름: {}", managerId, managerName);

            // 성공 메시지와 함께 대시보드로 리다이렉트
            HttpSession session = request.getSession();
            session.setAttribute("successMessage", "관리자가 성공적으로 추가되었습니다.");

            log.info("대시보드로 리다이렉트: {}/dashboard", request.getContextPath());
            response.sendRedirect(request.getContextPath() + "/dashboard");

        } catch (Exception e) {
            log.error("관리자 추가 중 오류 발생", e);
            request.setAttribute("error", "관리자 추가 중 오류가 발생했습니다: " + e.getMessage());
            request.setAttribute("managerId", managerId);
            request.setAttribute("managerName", managerName);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/mgr_add.jsp").forward(request, response);
        }
    }

    /* 관리자 계정 활성화/비활성화 */
    private void toggleManagerActive(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 현재 세션에 로그인된 관리자 ID 가져오기 (본인 차단 방지용)
        HttpSession session = request.getSession(false);
        String currentLoginId = (session != null) ? session.getAttribute("managerId").toString() : null;

        // 파라미터 수신
        String targetId = request.getParameter("managerId");  // 변경할 대상 ID
        String activeStr = request.getParameter("active");  // 변경할 상태값
        log.info("관리자 상태 변경 요청 - ID: {}, active: {}", targetId, activeStr);

        if (targetId == null || activeStr == null) {
            log.warn("필수 파라미터 누락- ID: {}, active: {}", targetId, activeStr);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 활성화 : string->boolean 형변환
        boolean active = Boolean.parseBoolean(activeStr);  //"true"이면 true, 그외 모두 false

        // 본인 계정은 스스로 비활성화(false) 할 수 없게 방어
        if (!active && targetId.equals(currentLoginId)) {
            log.warn("본인 계정 비활성화 시도 차단 - ID: {}", targetId);
            // 에러 메시지와 함께 목록으로 돌아가기
            response.sendRedirect(request.getContextPath() + "/mgr/list?error=self");
            return;
        }

        try {
            // DB 업데이트 수행 (DAO에는 변호나된 boolean 값 전달)
            managerDAO.updateActive(active, targetId);
            log.info("관리자 계정 상태 변경 성공 - ID: {}, 활성화: {}", targetId, active);
            // 성공 시 관리자 목록 페이지로 이동
            response.sendRedirect(request.getContextPath() + "/mgr/list");

        } catch (Exception e) {
            log.error("관리자 계정 상태 변경 중 오류 발생", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}