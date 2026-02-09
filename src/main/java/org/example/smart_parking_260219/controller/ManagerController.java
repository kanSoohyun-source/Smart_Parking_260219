package org.example.smart_parking_260219.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dto.ManagerDTO;
import org.example.smart_parking_260219.service.ManagerService;

import java.io.IOException;

@Log4j2
@WebServlet(name = "managerController", urlPatterns = {"/manager/loginProcess", "/manager/logoutProcess", "/manager/addManagerProcess"})
public class ManagerController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ManagerService managerService = ManagerService.getInstance();

        String requestURI = req.getRequestURI();  //요청 URI
        String contextPath = req.getContextPath();  //컨텍스트 경로
        String command = requestURI.substring(contextPath.length());  //요청 URI에서 컨텍스트 경로를 제거한 명령어

        log.info("command: {}", command);

        HttpSession session = req.getSession();  //세션에 로그인 정보 가져오기

        switch (command) {
            /* 로그인 처리 */
            case "/manager/loginProcess" -> {
                // 1. 폼 태그로 전달 받은 값을 저장
                String id = req.getParameter("id"); // login.jsp의 input name="id"
                String pw = req.getParameter("pw"); // login.jsp의 input name="pw"

                if (managerService.isAuth(id, pw)) {
                    // 인증 성공
                    session.setAttribute("isLoggedIn", true);
                    session.setAttribute("sessionManagerId", id);

                    ManagerDTO managerDto = managerService.getManager(id);
                    session.setAttribute("sessionManagerName", managerDto.getManagerName());

                    // 대시보드로 이동
                    resp.sendRedirect(contextPath + "/dashboard/dashboard.jsp");
                } else {
                    // 실패 시: login.jsp로 돌아가면서 error라는 파라미터를 붙여줍니다.
                    resp.sendRedirect(contextPath + "/login.jsp?error=login_fail");
                }
            }

            /* 관리자 추가 등록 (필요 시) */
            case "/manager/addManagerProcess" -> {
                log.info("새로운 관리자 등록");
                ManagerDTO managerDto = ManagerDTO.builder()
                        .managerId(req.getParameter("managerId"))
                        .managerName(req.getParameter("managerName"))
                        .password(req.getParameter("password"))
                        .email(req.getParameter("email"))
                        .active(true)
                        .build();

                managerService.addManager(managerDto);
                resp.sendRedirect(contextPath + "/login.jsp?msg=registered");
            }

            /* 로그아웃 */
            case "/manager/logout" -> {
                session.invalidate(); // 모든 세션 정보 삭제
                resp.sendRedirect(contextPath + "/login.jsp");
            }
        }
    }
}
