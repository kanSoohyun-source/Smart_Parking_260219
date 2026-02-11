<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>관리자 추가</title>
<%--    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">--%>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f5f5;
            display: flex;
        }
        .main-content {
            flex: 1;
            padding: 20px;
            margin-left: 250px; /* 사이드바 너비만큼 */
        }
        .container {
            max-width: 600px;
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
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #555;
            font-weight: 500;
        }
        input[type="text"],
        input[type="password"],
        input[type="email"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
        }
        input:focus {
            outline: none;
            border-color: #667eea;
        }
        .btn-group {
            display: flex;
            gap: 10px;
            margin-top: 30px;
        }
        .btn {
            flex: 1;
            padding: 12px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            transition: all 0.3s;
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
    </style>
</head>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>

<div class="main-content">
    <div class="container">
        <h2>관리자 추가</h2>

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

<%--        <form action="${pageContext.request.contextPath}/manager_add" method="post">--%>
        <form action="${pageContext.request.contextPath}/mgr/add" method="post">
            <div class="form-group">
                <label for="id">아이디 *</label>
                <input type="text" id="id" name="id" 
                       value="<%= request.getAttribute("managerId") != null ? request.getAttribute("managerId") : "" %>" 
                       required>
            </div>

            <div class="form-group">
                <label for="pw">비밀번호 *</label>
                <input type="password" id="pw" name="pw" required>
            </div>

            <div class="form-group">
                <label for="passwordConfirm">비밀번호 확인 *</label>
                <input type="password" id="passwordConfirm" name="passwordConfirm" required>
            </div>

            <div class="form-group">
                <label for="name">이름 *</label>
                <input type="text" id="name" name="name" 
                       value="<%= request.getAttribute("managerName") != null ? request.getAttribute("managerName") : "" %>" 
                       required>
            </div>

            <div class="form-group">
                <label for="email">이메일 *</label>
                <input type="email" id="email" name="email" 
                       value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" 
                       placeholder="example@email.com" required>
            </div>

            <div class="btn-group">
                <button type="submit" class="btn btn-primary">추가하기</button>
                <button type="button" class="btn btn-secondary"
                        onclick="location.href='${pageContext.request.contextPath}/dashboard'">
                    취소
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
