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
        <h2>정산</h2>
        <div class="form-group">
            <label>차량 번호</label>
            <input type="text" id="regCarNum" placeholder="차량번호 8자리" maxlength="8">
        </div>
        <div class="form-group">
            <label>총 주차 시간</label>
            <input type="text" id="totalParkingTime" placeholder="총 주차 시간">
        </div>
        <div class="form-group">
            <label>차종(할인울)</label>
            <input type="text" id="carType" placeholder="차종(할인울)">
        </div>
        <div class="form-group">
            <label>총 주차 요금</label>
            <input type="text" id="totalParkingFee" placeholder="총 주차 요금">
        </div>
        <button onclick="registerMember()">결제</button>
    </div>

</div>
    <script src="../JS/menu.js"></script>
    <script src="../JS/function.js"></script>
</body>
</html>
