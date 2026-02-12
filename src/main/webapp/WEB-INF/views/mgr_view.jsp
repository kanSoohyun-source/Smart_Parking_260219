<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.smart_parking_260219.vo.ManagerVO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>관리자 정보</title>
    <link rel="stylesheet" href="/CSS/style.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        /*body {*/
        /*    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;*/
        /*    background: #f5f5f5;*/
        /*    display: flex;*/
        /*}*/
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f5f5;
            margin: 0;

            display: flex;
            justify-content: center; /* 가로 중앙 */
            align-items: center;     /* 세로 중앙 */
            min-height: 100vh;
        }
        /*.main-content {*/
        /*    flex: 1;*/
        /*    padding: 20px;*/
        /*    margin-left: 250px; !* 사이드바 너비만큼 *!*/
        /*}*/
        .main-content {
            width: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        /*.container {*/
        /*    max-width: 800px;*/
        /*    margin: 0 auto;*/
        /*    background: white;*/
        /*    padding: 30px;*/
        /*    border-radius: 10px;*/
        /*    box-shadow: 0 2px 10px rgba(0,0,0,0.1);*/
        /*}*/
        .container {
            width: 100%;
            max-width: 600px;
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            /* 기존 margin: 0 auto; 는 flex 환경에서 무시되므로 삭제해도 무방합니다. */
        }
        h2 {
            color: #333;
            margin-bottom: 30px;
            padding-bottom: 10px;
            border-bottom: 2px solid #667eea;
        }
        .message {
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
            text-align: center;
        }
        .success-message {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .error-message {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .info-section {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        .info-row {
            display: flex;
            padding: 12px 0;
            border-bottom: 1px solid #e0e0e0;
        }
        .info-row:last-child {
            border-bottom: none;
        }
        .info-label {
            flex: 0 0 150px;
            font-weight: 600;
            color: #555;
        }
        .info-value {
            flex: 1;
            color: #333;
        }
        .status-badge {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 14px;
            font-weight: 500;
        }
        .status-active {
            background: #d4edda;
            color: #155724;
        }
        .status-inactive {
            background: #f8d7da;
            color: #721c24;
        }
        .btn-group {
            display: flex;
            gap: 10px;
            margin-top: 30px;
        }
        .btn {
            padding: 12px 24px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }
        .btn-primary {
            background: #667eea;
            color: white;
        }
        .btn-primary:hover {
            background: #5568d3;
        }
        .btn-secondary {
            background: #6c757d;
            color: white;
        }
        .btn-secondary:hover {
            background: #5a6268;
        }
        .btn-danger {
            background: #dc3545;
            color: white;
        }
        .btn-danger:hover {
            background: #c82333;
        }
        .btn-success {
            background: #28a745;
            color: white;
        }
        .btn-success:hover {
            background: #218838;
        }
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #999;
        }
        .empty-state-icon {
            font-size: 48px;
            margin-bottom: 20px;
        }
        .empty-state-text {
            font-size: 18px;
            margin-bottom: 30px;
        }
    </style>
</head>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>

<div class="main-content">
    <div class="container">

<%--        <%--%>
<%--            out.print("디버깅 - manager 객체 존재 여부: " + (request.getAttribute("manager") != null));--%>
<%--        %>--%>

        <h2>관리자 정보</h2>

        <%-- 성공 메시지 표시 --%>
        <% String successMessage = (String) session.getAttribute("successMessage");
            if (successMessage != null) {
                session.removeAttribute("successMessage"); %>
        <div class="message success-message">
            <%= successMessage %>
        </div>
        <% } %>

        <%-- 에러 메시지 표시 --%>
        <% String error = (String) request.getAttribute("error");
            if (error != null) { %>
        <div class="message error-message">
            <%= error %>
        </div>
        <% } %>

        <%
            ManagerVO manager = (ManagerVO) request.getAttribute("manager");
            if (manager != null) {
        %>
        <!-- 관리자 정보 표시 -->
        <div class="info-section">
<%--            <div class="info-row">--%>
<%--                <div class="info-label">관리자 번호</div>--%>
<%--                <div class="info-value"><%= manager.getManagerNo() %></div>--%>
<%--            </div>--%>
            <div class="info-row">
                <div class="info-label">아이디</div>
                <div class="info-value"><%= manager.getManagerId() %></div>
            </div>
            <div class="info-row">
                <div class="info-label">이름</div>
                <div class="info-value"><%= manager.getManagerName() %></div>
            </div>
            <div class="info-row">
                <div class="info-label">이메일</div>
                <div class="info-value"><%= manager.getEmail() %></div>
            </div>
            <div class="info-row">
                <div class="info-label">계정 상태</div>
                <div class="info-value">
                    <% if (manager.isActive()) { %>
                    <span class="status-badge status-active">활성</span>
                    <% } else { %>
                    <span class="status-badge status-inactive">비활성</span>
                    <% } %>
                </div>
            </div>
        </div>

        <!-- 버튼 그룹 -->
        <div class="btn-group">
            <button type="button" class="btn btn-secondary"
                    onclick="location.href='${pageContext.request.contextPath}/dashboard'">
                돌아가기
            </button>
            
            <% if (manager.isActive()) { %>
            <form action="${pageContext.request.contextPath}/mgr/toggleActive" method="post" style="flex: 1;">
                <input type="hidden" name="managerId" value="<%= manager.getManagerId() %>">
                <input type="hidden" name="active" value="false">
                <button type="submit" class="btn btn-danger" style="width: 100%;"
                        onclick="return confirm('이 관리자 계정을 비활성화 하시겠습니까?');">
                    계정 비활성화
                </button>
            </form>
            <% } else { %>
            <form action="${pageContext.request.contextPath}/mgr/toggleActive" method="post" style="flex: 1;">
                <input type="hidden" name="managerId" value="<%= manager.getManagerId() %>">
                <input type="hidden" name="active" value="true">
                <button type="submit" class="btn btn-success" style="width: 100%;"
                        onclick="return confirm('이 관리자 계정을 활성화 하시겠습니까?');">
                    계정 활성화
                </button>
            </form>
            <% } %>
        </div>

        <% } else { %>
        <!-- 조회할 관리자가 없을 때 -->
        <div class="empty-state">
            <div class="empty-state-icon">👤</div>
            <div class="empty-state-text">조회할 관리자 정보가 없습니다.</div>
            <button type="button" class="btn btn-primary"
                    onclick="location.href='${pageContext.request.contextPath}/mgr/add'">
                관리자 추가
            </button>
        </div>
        <% } %>
    </div>
</div>

<script>
    // 성공 메시지가 있으면 3초 후 자동으로 사라지게
    window.onload = function() {
        const successMsg = document.querySelector('.success-message');
        if (successMsg) {
            setTimeout(() => {
                successMsg.style.transition = 'opacity 0.5s';
                successMsg.style.opacity = '0';
                setTimeout(() => successMsg.remove(), 500);
            }, 3000);
        }
    };
</script>
</body>
</html>
