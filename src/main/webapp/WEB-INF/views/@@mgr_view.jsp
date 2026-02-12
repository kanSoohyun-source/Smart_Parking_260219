<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.smart_parking_260219.dto.ManagerDTO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>관리자 관리</title>
    <link rel="stylesheet" href="/CSS/style.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f5f5;
            margin: 0;
            display: flex;
        }
        .main-content {
            flex: 1;
            padding: 40px;
            margin-left: 250px;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
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
        .actions {
            margin-bottom: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            font-size: 14px;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-block;
        }
        .btn-primary {
            background: #667eea;
            color: white;
        }
        .btn-primary:hover {
            background: #5568d3;
        }
        .btn-danger {
            background: #dc3545;
            color: white;
            font-size: 12px;
            padding: 6px 12px;
        }
        .btn-danger:hover {
            background: #c82333;
        }
        .btn-success {
            background: #28a745;
            color: white;
            font-size: 12px;
            padding: 6px 12px;
        }
        .btn-success:hover {
            background: #218838;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #667eea;
            color: white;
            font-weight: 600;
        }
        tr:hover {
            background-color: #f5f5f5;
        }
        .status-badge {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 12px;
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
        .role-badge {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 500;
        }
        .role-admin {
            background: #fff3cd;
            color: #856404;
        }
        .role-normal {
            background: #d1ecf1;
            color: #0c5460;
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
        .action-buttons {
            display: flex;
            gap: 5px;
        }
    </style>
</head>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>

<div class="main-content">
    <div class="container">
        <h2>관리자 관리</h2>

        <%-- 성공 메시지 표시 --%>
        <% 
            String successMessage = (String) session.getAttribute("successMessage");
            if (successMessage != null) {
                session.removeAttribute("successMessage"); 
        %>
        <div class="message success-message">
            <%= successMessage %>
        </div>
        <% } %>

        <%-- 에러 메시지 표시 --%>
        <% 
            String errorMessage = (String) session.getAttribute("error");
            if (errorMessage != null) {
                session.removeAttribute("error");
        %>
        <div class="message error-message">
            <%= errorMessage %>
        </div>
        <% } %>

        <div class="actions">
            <div>
                <strong>전체 관리자: 
                    <% 
                        @SuppressWarnings("unchecked")
                        List<ManagerDTO> managerList = (List<ManagerDTO>) request.getAttribute("managerList");
                        int totalCount = (managerList != null) ? managerList.size() : 0;
                    %>
                    <%= totalCount %>명
                </strong>
            </div>
            <a href="${pageContext.request.contextPath}/mgr/add" class="btn btn-primary">
                + 관리자 추가
            </a>
        </div>

        <%
            if (managerList != null && !managerList.isEmpty()) {
        %>
        <!-- 관리자 목록 테이블 -->
        <table>
            <thead>
                <tr>
                    <th>아이디</th>
                    <th>이름</th>
                    <th>이메일</th>
                    <th>권한</th>
                    <th>상태</th>
                    <th>관리</th>
                </tr>
            </thead>
            <tbody>
            <%
                for (ManagerDTO manager : managerList) {
                    boolean isAdmin = "ADMIN".equals(manager.getRole());
            %>
                <tr>
                    <td><%= manager.getManagerId() %></td>
                    <td><%= manager.getManagerName() %></td>
                    <td><%= manager.getEmail() %></td>
                    <td>
                        <% if (isAdmin) { %>
                        <span class="role-badge role-admin">최고관리자</span>
                        <% } else { %>
                        <span class="role-badge role-normal">일반관리자</span>
                        <% } %>
                    </td>
                    <td>
                        <% if (manager.getActive()) { %>
                        <span class="status-badge status-active">활성</span>
                        <% } else { %>
                        <span class="status-badge status-inactive">비활성</span>
                        <% } %>
                    </td>
                    <td>
                        <div class="action-buttons">
                            <%-- admin 계정이 아닌 경우에만 활성화/비활성화 및 삭제 버튼 표시 --%>
                            <% if (!isAdmin) { %>
                                <% if (manager.getActive()) { %>
                                <form action="${pageContext.request.contextPath}/mgr/toggleActive" method="post" style="display: inline;">
                                    <input type="hidden" name="managerId" value="<%= manager.getManagerId() %>">
                                    <input type="hidden" name="active" value="false">
                                    <button type="submit" class="btn btn-danger"
                                            onclick="return confirm('계정을 비활성화 하시겠습니까?');">
                                        비활성화
                                    </button>
                                </form>
                                <% } else { %>
                                <form action="${pageContext.request.contextPath}/mgr/toggleActive" method="post" style="display: inline;">
                                    <input type="hidden" name="managerId" value="<%= manager.getManagerId() %>">
                                    <input type="hidden" name="active" value="true">
                                    <button type="submit" class="btn btn-success"
                                            onclick="return confirm('계정을 활성화 하시겠습니까?');">
                                        활성화
                                    </button>
                                </form>
                                <% } %>
                                
                                <form action="${pageContext.request.contextPath}/mgr/delete" method="post" style="display: inline;">
                                    <input type="hidden" name="managerId" value="<%= manager.getManagerId() %>">
                                    <button type="submit" class="btn btn-danger"
                                            onclick="return confirm('정말 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.');">
                                        삭제
                                    </button>
                                </form>
                            <% } else { %>
                                <span style="color: #999; font-size: 12px;">보호된 계정</span>
                            <% } %>
                        </div>
                    </td>
                </tr>
            <%
                }
            %>
            </tbody>
        </table>

        <% } else { %>
        <!-- 관리자가 없을 때 -->
        <div class="empty-state">
            <div class="empty-state-icon">👥</div>
            <div class="empty-state-text">등록된 관리자가 없습니다.</div>
            <a href="${pageContext.request.contextPath}/mgr/add" class="btn btn-primary">
                관리자 추가
            </a>
        </div>
        <% } %>
    </div>
</div>

<script>
    // 성공 메시지 자동으로 사라지게
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
