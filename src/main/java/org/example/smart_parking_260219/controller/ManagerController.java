package org.example.smart_parking_260219.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.example.smart_parking_260219.dao.ManagerDAO;
import org.example.smart_parking_260219.dto.ManagerDTO;
import org.example.smart_parking_260219.service.ManagerService;
import org.example.smart_parking_260219.vo.ManagerVO;

import java.io.IOException;
import java.util.List;

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

            /* 조회 */
            case "/view":
                log.info("관리자 조회 페이지 처리");

                // 1. URL 파라미터에서 조회할 아이디를 가져옴 (예: /mgr/view?id=admin)
                String viewId = request.getParameter("id");
                log.info("조회할 관리자 ID: {}", viewId);

                // 2. 만약 파라미터가 없다면 세션에서 로그인한 본인 아이디를 사용
                if (viewId == null || viewId.trim().isEmpty()) {
                    log.info("ID 파라미터가 없어 세션에서 정보를 찾습니다.");
                    ManagerVO loginManager = (ManagerVO) session.getAttribute("loginManager");
                    if (loginManager != null) {
                        viewId = loginManager.getManagerId();
                    }
                }

                log.info("최종 조회할 관리자 ID: {}", viewId);

                if (viewId != null && !viewId.isEmpty()) {
                    try {
                        // DAO를 통해 DB에서 관리자 정보 조회
                        ManagerVO manager = managerDAO.selectOne(viewId);

                        if (manager != null) {
                            request.setAttribute("manager", manager);
                            log.info("관리자 데이터 조회 성공: {}", manager.getManagerName());
                        } else {
                            log.warn("ID가 {}인 관리자를 찾을 수 없음", viewId);
                            request.setAttribute("error", "존재하지 않는 관리자입니다.");
                        }
                    } catch (Exception e) {
                        log.error("관리자 조회 중 DB 오류", e);
                        request.setAttribute("error", "데이터를 가져오는 중 오류가 발생했습니다.");
                    }
                } else {
                    log.warn("조회할 ID를 찾을 수 없음 (파라미터X, 세션X)");
                    request.setAttribute("error", "조회할 관리자 정보를 특정할 수 없습니다.");
                }

                request.getRequestDispatcher("/WEB-INF/views/mgr_view.jsp").forward(request, response);
                break;

            /* 수정 */
            case "/modify":
                log.info("관리자 수정 페이지 처리");

                // URL 파라미터에서 수정할 아이디를 가져옴
                String modifyId = request.getParameter("id");
                log.info("수정할 관리자 ID: {}", modifyId);

                // 파라미터가 없으면 세션에서 로그인한 본인 아이디를 사용
                if (modifyId == null || modifyId.trim().isEmpty()) {
                    log.info("ID 파라미터가 없어 세션에서 정보를 찾습니다.");
                    ManagerVO loginManager = (ManagerVO) session.getAttribute("loginManager");
                    if (loginManager != null) {
                        modifyId = loginManager.getManagerId();
                    }
                }

                log.info("최종 수정할 관리자 ID: {}", modifyId);

                if (modifyId != null && !modifyId.isEmpty()) {
                    try {
                        // DAO를 통해 DB에서 관리자 정보 조회
                        ManagerVO manager = managerDAO.selectOne(modifyId);

                        if (manager != null) {
                            request.setAttribute("manager", manager);
                            log.info("관리자 데이터 조회 성공: {}", manager.getManagerName());
                        } else {
                            log.warn("ID가 {}인 관리자를 찾을 수 없음", modifyId);
                            request.setAttribute("error", "존재하지 않는 관리자입니다.");
                        }
                    } catch (Exception e) {
                        log.error("관리자 조회 중 DB 오류", e);
                        request.setAttribute("error", "데이터를 가져오는 중 오류가 발생했습니다.");
                    }
                } else {
                    log.warn("수정할 ID를 찾을 수 없음 (파라미터X, 세션X)");
                    request.setAttribute("error", "수정할 관리자 정보를 특정할 수 없습니다.");
                }

                request.getRequestDispatcher("/WEB-INF/views/mgr_modify.jsp").forward(request, response);
                break;

            /* 관리자 목록 조회 */
            case "/list":
                log.info("관리자 목록 조회 요청 처리 중..");
                try {
                    // Service를 통해 DTO 리스트를 가져옴
                    List<ManagerDTO> dtoList = ManagerService.INSTANCE.getAllManagers();
                    request.setAttribute("managerList", dtoList);
                    log.info("목록 조회 완료: {}명", dtoList.size());

                } catch (Exception e) {
                    log.error("목록 조회 중 오류 발생", e);
                    request.setAttribute("error", "목록을 불러오는 중 오류가 발생했습니다.");
                }
                // 목록 페이지로 포워딩
                request.getRequestDispatcher("/WEB-INF/views/mgr_list.jsp").forward(request, response);
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
        } else if ("/modify".equals(pathInfo)) {
            log.info("관리자 수정 처리 시작");
            modifyManager(request, response);  //관리자 정보 수정
        } else if ("/toggleActive".equals(pathInfo)) {
            log.info("관리자 활성화/비활성화 처리");
            toggleManagerActive(request, response);  //활성/비활성화 전환
        } else {
            log.warn("알 수 없는 POST 경로: {}", pathInfo);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /* 관리자 정보 수정 처리 */
    private void modifyManager(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // 폼에서 전달된 파라미터 가져오기
        String managerId = request.getParameter("managerId");  // hidden 필드의 원본 ID
        String managerName = request.getParameter("name");
        String password = request.getParameter("pw");  // 새 비밀번호
        String passwordConfirm = request.getParameter("passwordConfirm");
        String email = request.getParameter("email");

        log.info("관리자 수정 요청 - ID: {}, 이름: {}, 이메일: {}", managerId, managerName, email);

        // 필수 입력값 검증
        if (managerId == null || managerId.trim().isEmpty() ||
                managerName == null || managerName.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                email == null || email.trim().isEmpty()) {

            log.warn("필수 입력값 누락");
            request.setAttribute("error", "모든 필드를 입력해주세요.");

            // 기존 데이터 다시 조회해서 보내기
            try {
                ManagerVO manager = managerDAO.selectOne(managerId);
                request.setAttribute("manager", manager);
            } catch (Exception e) {
                log.error("관리자 정보 재조회 실패", e);
            }

            request.getRequestDispatcher("/WEB-INF/views/mgr_modify.jsp").forward(request, response);
            return;
        }

        // 비밀번호가 입력된 경우에만 검증
        if (password != null && !password.trim().isEmpty()) {
            // 비밀번호 길이 검증
            if (password.length() < 4) {
                log.warn("비밀번호 길이 부족: {}", password.length());
                request.setAttribute("error", "비밀번호는 최소 4자 이상이어야 합니다.");

                try {
                    ManagerVO manager = managerDAO.selectOne(managerId);
                    request.setAttribute("manager", manager);
                } catch (Exception e) {
                    log.error("관리자 정보 재조회 실패", e);
                }

                request.getRequestDispatcher("/WEB-INF/views/mgr_modify.jsp").forward(request, response);
                return;
            }

            // 비밀번호 일치 확인
            if (!password.equals(passwordConfirm)) {
                log.warn("비밀번호 불일치");
                request.setAttribute("error", "비밀번호가 일치하지 않습니다.");

                try {
                    ManagerVO manager = managerDAO.selectOne(managerId);
                    request.setAttribute("manager", manager);
                } catch (Exception e) {
                    log.error("관리자 정보 재조회 실패", e);
                }

                request.getRequestDispatcher("/WEB-INF/views/mgr_modify.jsp").forward(request, response);
                return;
            }
        }

        // 이메일 형식 검증
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            log.warn("잘못된 이메일 형식: {}", email);
            request.setAttribute("error", "올바른 이메일 형식이 아닙니다.");

            try {
                ManagerVO manager = managerDAO.selectOne(managerId);
                request.setAttribute("manager", manager);
            } catch (Exception e) {
                log.error("관리자 정보 재조회 실패", e);
            }

            request.getRequestDispatcher("/WEB-INF/views/mgr_modify.jsp").forward(request, response);
            return;
        }

        try {
            // 기존 관리자 정보 조회
            ManagerVO existingManager = managerDAO.selectOne(managerId);
            if (existingManager == null) {
                log.warn("수정할 관리자를 찾을 수 없음: {}", managerId);
                request.setAttribute("error", "존재하지 않는 관리자입니다.");
                request.getRequestDispatcher("/WEB-INF/views/mgr_modify.jsp").forward(request, response);
                return;
            }

            // 수정할 VO 생성 (Builder 패턴 사용)
            ManagerVO.ManagerVOBuilder builder = ManagerVO.builder()
                    .managerNo(existingManager.getManagerNo())  // 기존 번호 유지
                    .managerId(managerId)  // 아이디는 변경 불가
                    .managerName(managerName)  // 새 이름
                    .email(email)  // 새 이메일
                    .active(existingManager.isActive());  // 활성 상태 유지

            // 비밀번호가 입력된 경우에만 암호화해서 업데이트
            if (password != null && !password.trim().isEmpty()) {
                String encodedPassword = managerDAO.passEncode(password);
                builder.password(encodedPassword);
                log.info("비밀번호 변경 요청 - 암호화 완료");
            } else {
                // 비밀번호 입력 없으면 기존 비밀번호 유지
                builder.password(existingManager.getPassword());
                log.info("비밀번호 변경 없음 - 기존 비밀번호 유지");
            }

            ManagerVO updatedManager = builder.build();

            // DB 업데이트 (DAO에 updateManager 메소드)
            managerDAO.updateManager(updatedManager);
            log.info("관리자 정보 수정 성공 - ID: {}, 이름: {}", managerId, managerName);

            // 세션의 로그인 정보도 업데이트 (본인이 본인 정보를 수정한 경우)
            ManagerVO loginManager = (ManagerVO) session.getAttribute("loginManager");
            boolean isSelfModify = loginManager != null && loginManager.getManagerId().equals(managerId);

            if (isSelfModify) {
                // 본인이 본인 정보를 수정한 경우
                log.info("본인 정보 수정 - 로그아웃 처리 진행");

                // 성공 메시지 설정
                session.setAttribute("logoutMessage", "최고 관리자의 정보가 변경되었으니 다시 로그인을 해야합니다.");

                // 세션 무효화 (로그아웃)
                session.invalidate();

                // 로그인 페이지로 리다이렉트
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                // 다른 관리자의 정보를 수정한 경우
                log.info("타 관리자 정보 수정 완료 - ID: {}", managerId);

                // 성공 메시지와 함께 조회 페이지로 리다이렉트
                session.setAttribute("successMessage", "관리자 정보가 성공적으로 수정되었습니다.");
                response.sendRedirect(request.getContextPath() + "/mgr/view?id=" + managerId);
            }

        } catch (Exception e) {
            log.error("관리자 정보 수정 중 오류 발생", e);
            request.setAttribute("error", "정보 수정 중 오류가 발생했습니다: " + e.getMessage());

            try {
                ManagerVO manager = managerDAO.selectOne(managerId);
                request.setAttribute("manager", manager);
            } catch (Exception ex) {
                log.error("관리자 정보 재조회 실패", ex);
            }

            request.getRequestDispatcher("/WEB-INF/views/mgr_modify.jsp").forward(request, response);
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
//        String currentLoginId = (session != null) ? session.getAttribute("managerId").toString() : null;

        String currentLoginId = null;
        if (session != null) {
            ManagerVO loginVO = (ManagerVO) session.getAttribute("loginManager");
            if (loginVO != null) {
                currentLoginId = loginVO.getManagerId();
            }
        }

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
            // JSP에서 에러를 보여주기 위해 세션에 에러 메시지 저장
            session.setAttribute("error", "본인 계정은 비활성화할 수 없습니다.");
            response.sendRedirect(request.getContextPath() + "/mgr/view?id=" + targetId);
            return;
        }

        try {
            // DB 업데이트 수행 (DAO에는 변환된 boolean 값 전달)
            managerDAO.updateActive(active, targetId);
            log.info("관리자 계정 상태 변경 성공 - ID: {}, 활성화: {}", targetId, active);

            // 성공 메시지 저장 및 리다이렉트 경로
            String statusText = active ? "활성화" : "비활성화";
            session.setAttribute("successMessage", targetId + " 계정이 " + statusText + " 되었습니다.");

            response.sendRedirect(request.getContextPath() + "/mgr/view?id=" + targetId);

        } catch (Exception e) {
            log.error("관리자 계정 상태 변경 중 오류 발생", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}