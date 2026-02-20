<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.smart_parking_260219.dto.ManagerDTO" %>
<%@ page import="org.example.smart_parking_260219.vo.ManagerVO" %>

<html>
<head>
    <title>관리자 목록 - 스마트 파킹 시스템</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
    <style>
        /* 메인 컨텐츠 영역 정렬 */
        .main-content {
            padding: 40px;
            background-color: #f4f7f6;
            min-height: 100vh;
        }

        /* 테이블 디자인 수정 핵심 */
        .manager-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }

        /* 분류(Header) 부분 강조 */
        .manager-table thead th {
            background-color: #2c3e50; /* 진한 네이비톤으로 분류 명확화 */
            color: #ffffff; /* 글자는 흰색으로 대비 */
            padding: 15px;
            font-size: 1.1em;
            border-bottom: 3px solid #1a252f;
            text-align: center;
        }

        .manager-table tbody td {
            padding: 12px;
            border-bottom: 1px solid #eee;
            text-align: center;
            color: #333;
        }

        /* 행 마우스 오버 효과 */
        .manager-table tbody tr:hover {
            background-color: #f8f9fa;
        }

        .btn-add:hover {
            background: #1abc9c;
        }

        .btn-add {
            float: right;
            padding: 8px 15px;
            background: #2c3e50;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }

        /* 페이징 스타일 */
        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-top: 30px;
            gap: 5px;
        }

        .pagination a, .pagination span {
            padding: 8px 12px;
            text-decoration: none;
            border: 1px solid #ddd;
            color: #2c3e50;
            border-radius: 4px;
            transition: all 0.3s;
        }

        .pagination a:hover {
            background-color: #2c3e50;
            color: white;
        }

        .pagination .current {
            background-color: #2c3e50;
            color: white;
            font-weight: bold;
        }

        .pagination .disabled {
            color: #ccc;
            cursor: not-allowed;
            border-color: #eee;
        }

        .pagination .disabled:hover {
            background-color: transparent;
            color: #ccc;
        }

        /* ── 최고관리자 클릭 차단 안내 모달 ── */
        .modal-backdrop {
            display: none;
            position: fixed;
            inset: 0;
            background: rgba(0, 0, 0, 0.45);
            z-index: 9000;
            justify-content: center;
            align-items: center;
        }

        .modal-backdrop.show {
            display: flex;
        }

        .modal-box {
            background: #fff;
            border-radius: 12px;
            padding: 36px 32px 28px;
            max-width: 420px;
            width: 90%;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.18);
            text-align: center;
            animation: modalIn 0.18s ease;
        }

        @keyframes modalIn {
            from {
                transform: translateY(-20px);
                opacity: 0;
            }
            to {
                transform: translateY(0);
                opacity: 1;
            }
        }

        .modal-icon {
            font-size: 44px;
            margin-bottom: 14px;
        }

        .modal-title {
            font-size: 18px;
            font-weight: 700;
            color: #2c3e50;
            margin-bottom: 10px;
        }

        .modal-desc {
            font-size: 14px;
            color: #555;
            line-height: 1.7;
            margin-bottom: 24px;
        }

        .modal-desc strong {
            color: #667eea;
        }

        .modal-btn {
            display: inline-block;
            padding: 10px 32px;
            background: #2c3e50;
            color: #fff;
            border: none;
            border-radius: 6px;
            font-size: 15px;
            cursor: pointer;
            transition: background 0.2s;
        }

        .modal-btn:hover {
            background: #1a252f;
        }

        /* 최고관리자 행 강조 */
        .row-admin td {
            background-color: #f0f4ff;
        }

        .badge-admin {
            display: inline-block;
            font-size: 11px;
            background: #667eea;
            color: #fff;
            border-radius: 4px;
            padding: 1px 6px;
            margin-left: 6px;
            vertical-align: middle;
        }
    </style>
</head>
<body>
<%@ include file="../main/menu.jsp" %>

<%-- ── 최고관리자 접근 차단 안내 모달 ── --%>
<div id="adminModal" class="modal-backdrop">
    <div class="modal-box">
        <div class="modal-icon">🔒</div>
        <div class="modal-title">접근 제한</div>
        <div class="modal-desc">
            최고 관리자 계정은 이 화면에서 수정할 수 없습니다.<br>
            좌측 메뉴의 <strong>최고 관리자 정보 수정</strong> 탭을 이용해 주세요.
        </div>
        <button class="modal-btn" onclick="closeAdminModal()">확인</button>
    </div>
</div>

    <%
    /* 현재 로그인한 관리자의 role을 세션에서 꺼냄 */
    ManagerVO sessionMgr = (ManagerVO) session.getAttribute("loginManager");
    String loginRole = (sessionMgr != null) ? sessionMgr.getRole() : "";
%>

<div class="main-content">
    <div id="entry" class="page">
        <div style="overflow: hidden;">
            <h2 style="display: inline-block;">관리자 계정 목록</h2>

            <%--            <a href="${pageContext.request.contextPath}/mgr/add" class="btn-add">신규 관리자 추가</a>--%>

        </div>

        <div class="form-group">
            <table class="manager-table">
                <thead>
                <tr>
                    <th>번호</th>
                    <th>아이디</th>
                    <th>이름</th>
                    <th>이메일</th>
                    <th>상태</th>
                </tr>
                </thead>
                <tbody>
                <%
                    // 컨트롤러에서 보낸 "managerList"를 가져옴
                    List<ManagerDTO> managerList = (List<ManagerDTO>) request.getAttribute("managerList");

                    // 페이징 처리를 위한 변수
                    int pageSize = 5; // 한 페이지에 5명씩
                    int currentPage = 1; // 현재 페이지 (기본값 1)

                    // URL에서 page 파라미터 가져오기
                    String pageParam = request.getParameter("page");
                    if (pageParam != null) {
                        try {
                            currentPage = Integer.parseInt(pageParam);
                        } catch (NumberFormatException e) {
                            currentPage = 1;
                        }
                    }

                    int totalCount = 0;
                    int totalPages = 0;
                    int startIndex = 0;
                    int endIndex = 0;

                    if (managerList != null && !managerList.isEmpty()) {
                        totalCount = managerList.size();
                        totalPages = (int) Math.ceil((double) totalCount / pageSize);

                        // 현재 페이지가 유효한 범위인지 체크
                        if (currentPage < 1) currentPage = 1;
                        if (currentPage > totalPages) currentPage = totalPages;

                        // 현재 페이지에 표시할 데이터의 시작/끝 인덱스
                        startIndex = (currentPage - 1) * pageSize;
                        endIndex = Math.min(startIndex + pageSize, totalCount);

                        // 현재 페이지의 데이터만 출력
                        for (int i = startIndex; i < endIndex; i++) {
                            ManagerDTO mgr = managerList.get(i);
                %>
                <tr class="<%= "ADMIN".equals(mgr.getRole()) ? "row-admin" : "" %>">
                    <td><%= mgr.getManagerNo() %>
                    </td>
                    <td><%= mgr.getManagerId() %>
                    </td>
                    <td>
                        <% if ("ADMIN".equals(mgr.getRole())) { %>
                        <%-- 최고관리자: 클릭 시 view/modify 진입 차단 → 안내 모달 표시 --%>
                        <a href="javascript:void(0);"
                           onclick="openAdminModal();"
                           style="color: #667eea; font-weight: bold; cursor: pointer;">
                            <%= mgr.getManagerName() %>
                        </a>
                        <span class="badge-admin">최고관리자</span>
                        <% } else { %>
                        <%-- 일반관리자: 기존처럼 view 페이지로 이동 --%>
                        <a href="${pageContext.request.contextPath}/mgr/view?id=<%= mgr.getManagerId() %>"
                           style="color: #007bff; font-weight: bold;">
                            <%= mgr.getManagerName() %>
                        </a>
                        <% } %>
                    </td>
                    <td><%= mgr.getEmail() %>
                    </td>
                    <td>
                            <span class="<%= mgr.isActive() ? "status-active" : "status-inactive" %>">
                                <%= mgr.isActive() ? "활성" : "비활성" %>
                            </span>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="5" style="padding: 30px; color: #999;">등록된 관리자가 없습니다.</td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>

            <%
                // 페이징 네비게이션 출력
                if (managerList != null && !managerList.isEmpty() && totalPages > 1) {
            %>
            <div class="pagination">
                <!-- 이전 페이지 -->
                <% if (currentPage > 1) { %>
                <a href="?page=<%= currentPage - 1 %>">이전</a>
                <% } else { %>
                <span class="disabled">이전</span>
                <% } %>

                <!-- 페이지 번호 -->
                <%
                    // 페이지 번호 표시 범위 (현재 페이지 기준 앞뒤 2개씩)
                    int startPage = Math.max(1, currentPage - 2);
                    int endPage = Math.min(totalPages, currentPage + 2);

                    // 첫 페이지
                    if (startPage > 1) {
                %>
                <a href="?page=1">1</a>
                <% if (startPage > 2) { %>
                <span>...</span>
                <% } %>
                <% } %>

                <!-- 페이지 번호들 -->
                <% for (int i = startPage; i <= endPage; i++) { %>
                <% if (i == currentPage) { %>
                <span class="current"><%= i %></span>
                <% } else { %>
                <a href="?page=<%= i %>"><%= i %>
                </a>
                <% } %>
                <% } %>

                <!-- 마지막 페이지 -->
                <% if (endPage < totalPages) { %>
                <% if (endPage < totalPages - 1) { %>
                <span>...</span>
                <% } %>
                <a href="?page=<%= totalPages %>"><%= totalPages %>
                </a>
                <% } %>

                <!-- 다음 페이지 -->
                <% if (currentPage < totalPages) { %>
                <a href="?page=<%= currentPage + 1 %>">다음</a>
                <% } else { %>
                <span class="disabled">다음</span>
                <% } %>
            </div>
            <% } %>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/JS/menu.js"></script>
<script src="${pageContext.request.contextPath}/JS/function.js"></script>
<script>
    /* ── 최고관리자 접근 차단 모달 제어 ── */
    function openAdminModal() {
        document.getElementById('adminModal').classList.add('show');
    }

    function closeAdminModal() {
        document.getElementById('adminModal').classList.remove('show');
    }

    /* 모달 바깥 영역 클릭 시 닫기 */
    document.getElementById('adminModal').addEventListener('click', function (e) {
        if (e.target === this) closeAdminModal();
    });
    /* ESC 키로 닫기 */
    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') closeAdminModal();
    });
</script>
</html>
