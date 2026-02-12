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
    <div class="container mt-4" style="max-width: 500px;">
        <h2 class="mb-4">회원 조회</h2>

        <% if ("notFound".equals(error)) { %>
        <div class="alert alert-warning">해당 차량번호로 등록된 회원이 없습니다.</div>
        <% } %>

        <% if ("missing".equals(error)) { %>
        <div class="alert alert-danger">차량번호를 입력해주세요.</div>
        <% } %>

        <% if ("fail".equals(error)) { %>
        <div class="alert alert-danger">조회 중 오류가 발생했습니다.</div>
        <% } %>

        <div class="card">
            <div class="card-body">
                <form action="/member/member_detail" method="get">
                    <div class="form-group">
                        <label>차량번호 뒤 4자리</label>
                        <input type="text" class="form-control" name="carNum"
                               placeholder="차량번호 뒤 4자리를 입력해 주세요"
                               maxlength="4"
                               pattern="[0-9]{4}"
                               title="숫자 4자리를 입력하세요"
                               required>
                        <small class="text-muted">예: 1234</small>
                    </div>
                    <button type="submit" class="btn btn-primary btn-block">검색</button>
                </form>
            </div>
        </div>

    </div>
</div>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<script src="../JS/menu.js"></script>
</body>
</html>