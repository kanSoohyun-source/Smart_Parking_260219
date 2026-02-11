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
@WebServlet("/login")
public class LoginController extends HttpServlet {

    private ManagerDAO managerDAO = ManagerDAO.getInstance();

    @Override
    /* 로그인 폼 요청 처리 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 세션 확인 - 이미 로그인된 경우 대시보드로 리다이렉트
        HttpSession session = request.getSession(false);  // 기존 세션이 없으면 null 반환
        if (session != null && session.getAttribute("loginManager") != null) {
            // 이미 로그인 정보가 세션에 있다면 굳이 로그인 창을 보여줄 필요가 없으므로 대시보드로 이동
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        // 로그인하지 않은 상태라면 로그인 페이지(jsp) 페이지로 포워딩
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    /* 로그인 데이터 처리 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");  // 한글 깨짐 방지

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
            // DB에서 관리자 정보 조회
            ManagerVO manager = managerDAO.selectOne(managerId);

            // 관리자 존재 여부 확인
            if (manager == null) {
                log.warn("존재하지 않는 관리자 ID: {}", managerId);
                request.setAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }

            // 계정 활성화 여부 확인
            if (!manager.isActive()) {
                log.warn("비활성화된 계정 로그인 시도: {}", managerId);
                request.setAttribute("error", "비활성화된 계정입니다. 관리자에게 문의하세요.");
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

            // 로그인 성공 - 세션 생성
            HttpSession session = request.getSession();
            session.setAttribute("loginManager", manager);  // 객체 전체 저장
            session.setAttribute("managerId", manager.getManagerId());  // ID 별도 저장
            session.setAttribute("managerName", manager.getManagerName());  // 이름 별도 저장

            // 세션 타임아웃 설정 (30분)
            // session.setMaxInactiveInterval(30 * 60);

            log.info("로그인 성공 - 관리자: {} ({})", manager.getManagerName(), managerId);

            // 대시보드로 리다이렉트
            response.sendRedirect(request.getContextPath() + "/dashboard");

        } catch (Exception e) {
            log.error("로그인 처리 중 오류 발생", e);
            request.setAttribute("error", "시스템 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}