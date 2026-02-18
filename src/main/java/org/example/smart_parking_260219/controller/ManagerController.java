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
@WebServlet(name = "managerController", value = {"/mgr/*"})
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

        // pathInfo 사용 ("/mgr/list"에서 "/list" 부분만 잘라온 값)
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

                // URL에서 ?id=admin 처럼 넘겨온 ID 값을 읽음
                String viewId = request.getParameter("id");
                log.info("조회할 관리자 ID: {}", viewId);

                // 만약 id 파라미터가 없다면, 현재 로그인한 '나'의 정보를 보여줌.
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
                        // DAO를 통해 DB에서 관리자 정보 조회 후 읽어옴
                        ManagerVO manager = managerDAO.selectOne(viewId);

                        if (manager != null) {
                            // 조회한 정보 'manager'를 가져옴
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

                // 조회한 정보 'manager'를 JSP에 전달(setAttribute)
                request.getRequestDispatcher("/WEB-INF/views/mgr_view.jsp").forward(request, response);
                break;

            /* 수정 */
            case "/modify":
                log.info("관리자 수정 페이지 처리");

                // URL에서 ?id=admin 처럼 넘겨온 ID 값을 읽음
                String modifyId = request.getParameter("id");
                log.info("수정할 관리자 ID: {}", modifyId);

                // 만약 id 파라미터가 없다면, 현재 로그인한 '나'의 정보를 보여줌.
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
                        // DAO를 통해 DB에서 관리자 정보 조회 후 읽어옴
                        ManagerVO manager = managerDAO.selectOne(modifyId);

                        if (manager != null) {
                            // 조회한 정보 'manager'를 가져옴
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

                // 조회한 정보 'manager'를 JSP에 전달(setAttribute)
                request.getRequestDispatcher("/WEB-INF/views/mgr_modify.jsp").forward(request, response);
                break;

            /* 관리자 목록 조회 */
            case "/list":
                log.info("관리자 목록 조회 요청 처리 중..");
                try {
                    // Service계층 호출하여 전체 관리자 리스트를 DTO형태로 가져옴
                    List<ManagerDTO> dtoList = ManagerService.INSTANCE.getAllManagers();
                    // JSP 파일에서 반복문을 돌릴 수 있도록 리스트로 전달
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
        // 세션이 없거나, 세션 안에 로그인 정보(loginManager) 없으면 가짜로 요청으로 간주
        if (session == null || session.getAttribute("loginManager") == null) {
            log.warn("미인증 POST 요청 - 로그인 페이지로 리다이렉트");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 경로 추출(pathInfo)
        String pathInfo = request.getPathInfo();

        // 경로(URL)에 따른 분기 로직
        if ("/add".equals(pathInfo)) {
            log.info("관리자 추가 처리 시작");
            addManager(request, response);
        } else if ("/modify".equals(pathInfo)) {
            log.info("관리자 수정 처리 시작");
            modifyManager(request, response);
        } else if ("/toggleActive".equals(pathInfo)) {
            log.info("관리자 활성화 토글 처리 시작");
            toggleManagerActive(request, response);
        } else {
            log.warn("알 수 없는 POST 경로: {}", pathInfo);
            // 엉뚱 URL 요청 시, 404 Not Found
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /* 관리자 수정 처리 */
    private void modifyManager(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 세션 체크
        HttpSession session = request.getSession(false);

        // JSP 파일에서 사용자가 입력한 값을 변수에 담음
        String managerId = request.getParameter("id");  // 누구를 수정할 지 결정하는 키 값
        String managerName = request.getParameter("name");
        String password = request.getParameter("pw");  // 변경할 새 비밀번호 (비어있을 수 있음)
        String passwordConfirm = request.getParameter("passwordConfirm");  // 확인용 비밀번호
        String email = request.getParameter("email");

        log.info("관리자 정보 수정 요청 - ID: {}, 이름: {}, 이메일: {}", managerId, managerName, email);
        log.info("비밀번호 변경 여부: {}", (password != null && !password.trim().isEmpty() ? "Yes" : "No"));

        // 필수 입력 항목이 비어있는지 확인
        if (managerId == null || managerId.trim().isEmpty() ||
                managerName == null || managerName.trim().isEmpty() ||
                email == null || email.trim().isEmpty()) {

            log.warn("필수 입력값 누락");
            request.setAttribute("error", "ID, 이름, 이메일은 필수 입력값입니다.");

            try {
                // 에러 발생 시, 기존 정보를 다시 DB에서 읽어와 화면에 뿌려줘야 입력폼이 유지됨
                ManagerVO manager = managerDAO.selectOne(managerId);
                request.setAttribute("manager", manager);
            } catch (Exception e) {
                log.error("관리자 정보 재조회 실패", e);
            }
            request.getRequestDispatcher("/WEB-INF/views/mgr_modify.jsp").forward(request, response);
            return;
        }

        // 비밀번호 변경 시 일치 확인
        // 비밀번호 칸에 무언가 적었을 때만 실행
        if (password != null && !password.trim().isEmpty()) {
            // 새 비밀번호 & 확인용 비밀번호가 동일한지 확인
            if (!password.equals(passwordConfirm)) {
                log.warn("비밀번호 불일치");
                request.setAttribute("error", "비밀번호가 일치하지 않습니다.");

                try {
                    // 기존 정보를 다시 DB에서 읽어와 화면에 뿌려줘야 입력폼이 유지됨
                    ManagerVO manager = managerDAO.selectOne(managerId);
                    request.setAttribute("manager", manager);
                } catch (Exception e) {
                    log.error("관리자 정보 재조회 실패", e);
                }

                request.getRequestDispatcher("/WEB-INF/views/mgr_modify.jsp").forward(request, response);
                return;
            }

            // 비밀번호 길이 검증
            if (password.length() < 4) {
                log.warn("비밀번호 길이 부족: {}", password.length());
                request.setAttribute("error", "비밀번호는 최소 4자 이상이어야 합니다.");

                try {
                    // 기존 정보를 다시 DB에서 읽어와 화면에 뿌려줘야 입력폼이 유지됨
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
            // 빌더 패턴으로 수정할 정보를 담은 객체(VO)를 만듬
            // DAO의 updateManager()에서 BCrypt 해싱 처리
            ManagerVO managerVO = ManagerVO.builder()
                    .managerId(managerId)
                    .managerName(managerName)
                    .password(password)  // 평문으로 넘기면 DAO 내부에서 암호화(BCrypt)수행
                    .email(email)
                    .build();

            // DB 업데이트 (DAO 내부에서 BCrypt 해싱 수행)
            managerDAO.updateManager(managerVO);
            log.info("관리자 정보 수정 완료 - ID: {}", managerId);

            // 본인 정보 수정 시 재로그인 요구
            ManagerVO loginManager = (ManagerVO) session.getAttribute("loginManager");

            // 수정된 아이디가 현재 로그인 중인 ID와 같을 경우 (본인 정보 수정한 경우)
            if (loginManager != null && managerId.equals(loginManager.getManagerId())) {
                log.info("최고 관리자 정보 수정 - 재로그인 필요");

                // 성공 메시지 설정
                session.setAttribute("logoutMessage", "관리자 정보가 변경되었으니 다시 로그인해주세요.");
                // 모든 세션 정보 삭제 (로그아웃)
                session.invalidate();
                // 로그인 페이지로 리다이렉트
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                // 다른 관리자의 정보를 수정한 경우
                log.info("일반 관리자 정보 수정 완료 - ID: {}", managerId);
                // 성공 메시지와 함께 조회 페이지로 리다이렉트
                session.setAttribute("successMessage", "관리자 정보가 성공적으로 수정되었습니다.");
                // 관리자 목록 페이지로 리다이렉트
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

        // JSP 파일에서 사용자가 입력한 값을 변수에 담음
        String managerId = request.getParameter("id");
        String managerName = request.getParameter("name");
        String password = request.getParameter("pw");
        String passwordConfirm = request.getParameter("passwordConfirm");
        String email = request.getParameter("email");

        log.info("관리자 추가 요청 - ID: {}, 이름: {}, 이메일: {}", managerId, managerName, email);

        // 필수 입력값 검증
        if (managerId == null || managerId.trim().isEmpty() ||
                managerName == null || managerName.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                email == null || email.trim().isEmpty()) {

            log.warn("필수 입력값 누락");
            request.setAttribute("error", "모든 필드를 입력해주세요.");
            // 에러가 나서 돌아가더라도 사용자가 이미 입력했던 값들은 그대로 유지
            request.setAttribute("managerId", managerId);
            request.setAttribute("managerName", managerName);
            request.setAttribute("email", email);

            // 다시 입력 폼으로 이동
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

            // 빌더 패턴으로 수정할 정보를 담은 객체(VO)를 만듬
            // DAO의 insertManager()에서 BCrypt 해싱 처리
            ManagerVO newManager = ManagerVO.builder()
                    .managerId(managerId)
                    .managerName(managerName)
                    .password(password)  // 평문으로 넘기면 DAO 내부에서 암호화(BCrypt)수행
                    .email(email)
                    .active(true)  // 새로 생성된 계정은 바로 사용할 수 있게 활성화 상태로 생성
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
        String currentLoginId = null;

        if (session != null) {
            ManagerVO loginVO = (ManagerVO) session.getAttribute("loginManager");
            if (loginVO != null) {
                currentLoginId = loginVO.getManagerId();  // 현재 로그인한 ID
            }
        }

        // 파라미터 수신
        String targetId = request.getParameter("managerId");  // 변경할 대상 ID
        String activeStr = request.getParameter("active");  // 변경할 상태값
        log.info("관리자 상태 변경 요청 - ID: {}, active: {}", targetId, activeStr);

        // 값이 하나라도 없으면 400 에러 처리
        if (targetId == null || activeStr == null) {
            log.warn("필수 파라미터 누락- ID: {}, active: {}", targetId, activeStr);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 문자열 "true"를 자바의 논리값 true로 변경 (활성화 : string->boolean 형변환)
        boolean active = Boolean.parseBoolean(activeStr);  //"true"이면 true, 그외 모두 false

        // 본인 계정(currentLoginId)은 스스로 비활성화(false) 할 수 없게 방어
        if (!active && targetId.equals(currentLoginId)) {
            log.warn("본인 계정 비활성화 시도 차단 - ID: {}", targetId);
            // JSP에서 에러를 보여주기 위해 세션에 에러 메시지 저장
            session.setAttribute("error", "본인 계정은 비활성화할 수 없습니다.");
            response.sendRedirect(request.getContextPath() + "/mgr/view?id=" + targetId);
            return;
        }

        try {
            // DB 업데이트 수행 (DAO를 호출해 해당 ID의 'active' 컬럼 값을 변경)
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