<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String error = request.getParameter("error");
%>
<html>
<head>
    <title>회원 조회</title>
    <link rel="stylesheet" href="../CSS/style.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<body>
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
    <div id="memberSearch" class="page">
        <h2>회원 조회</h2>

        <!-- ✅ 에러 메시지 표시 -->
        <% if ("notFound".equals(error)) { %>
        <div class="alert alert-warning" role="alert">
            <strong>알림!</strong> 해당 차량번호로 등록된 회원이 없습니다.
        </div>
        <% } %>

        <% if ("missing".equals(error)) { %>
        <div class="alert alert-danger" role="alert">
            <strong>오류!</strong> 차량번호를 입력해주세요.
        </div>
        <% } %>

        <% if ("fail".equals(error)) { %>
        <div class="alert alert-danger" role="alert">
            <strong>오류!</strong> 조회 중 오류가 발생했습니다.
        </div>
        <% } %>

        <div class="search-box">
            <form action="/member/member_detail" method="get">
                <input type="text" id="getOneMember" name="carNum"
                       placeholder="차량번호 뒤 4자리를 입력해 주세요"
                       maxlength="4"
                       pattern="[0-9]{4}"
                       title="숫자 4자리를 입력하세요"
                       required>
                <button type="submit">검색</button>
            </form>
            <small class="form-text text-muted">예: 1234</small>
        </div>
    </div>
</div>
<script src="../JS/menu.js"></script>
<style>
    .search-box {
        max-width: 500px;
        margin: 30px auto;
        text-align: center;
    }

    .search-box form {
        display: flex;
        gap: 10px;
        margin-bottom: 10px;
    }

    .search-box input {
        flex: 1;
        padding: 12px;
        font-size: 16px;
        border: 2px solid #ddd;
        border-radius: 4px;
    }

    .search-box input:focus {
        outline: none;
        border-color: #3498db;
    }

    .search-box button {
        padding: 12px 30px;
        background-color: #3498db;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 16px;
        font-weight: bold;
        transition: background-color 0.3s;
    }

    .search-box button:hover {
        background-color: #2980b9;
    }

    .alert {
        max-width: 500px;
        margin: 20px auto;
        padding: 15px;
        border-radius: 4px;
    }

    .alert-warning {
        background-color: #fff3cd;
        border: 1px solid #ffc107;
        color: #856404;
    }

    .alert-danger {
        background-color: #f8d7da;
        border: 1px solid #f5c6cb;
        color: #721c24;
    }

    .form-text {
        margin-top: 10px;
        color: #6c757d;
        font-size: 14px;
    }
</style>
</body>
</html>