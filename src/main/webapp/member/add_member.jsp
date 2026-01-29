<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 26. 1. 28.
  Time: 오후 9:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="../CSS/style.css">
</head>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
  <!-- Content -->
    <div id="register" class="page">
        <h2>회원 등록</h2>
        <div class="form-group">
            <label>차량 번호</label>
            <input type="text" id="regCarNum" placeholder="차량번호 8자리" maxlength="8">
        </div>
        <div class="form-group">
            <label>소유주 이름</label>
            <input type="text" id="regOwner" placeholder="이름">
        </div>
        <div class="form-group">
            <label>차종</label>
            <input type="text" id="regCarType" placeholder="차종">
        </div>
        <div class="form-group">
            <label>연락처</label>
            <input type="text" id="regPhone" placeholder="연락처">
        </div>
        <button onclick="registerMember()">완료</button>
    </div>
</div>
    <script src="../JS/menu.js"></script>
    <script src="../JS/function.js"></script>
</body>
</html>
