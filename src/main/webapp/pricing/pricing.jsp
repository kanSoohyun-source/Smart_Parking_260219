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
    <div id="pricing" class="page">
        <h2>요금 부과 정책</h2>
        <div class="form-group">
            <label>기본 요금 (30분)</label>
            <input type="number" id="basePrice" value="2000">
        </div>
        <div class="form-group">
            <label>추가 요금 (10분당)</label>
            <input type="number" id="additionalPrice" value="1000">
        </div>
        <div class="form-group">
            <label>월정액 요금</label>
            <input type="number" id="monthlyPrice" value="15000">
        </div>
        <button onclick="savePricing()">저장</button>
    </div>
</div>
    <script src="../JS/menu.js"></script>
</body>
</html>
