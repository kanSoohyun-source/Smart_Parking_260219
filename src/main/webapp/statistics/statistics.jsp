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
    <div id="statistics" class="page">
        <h2>매출 통계</h2>
        <div style="margin: 30px 0;">
            <h3>오늘 매출: <span id="todaySales">0</span>원</h3>
            <h3>이번 달 매출: <span id="monthlySales">0</span>원</h3>
            <h3>총 매출: <span id="totalSales">0</span>원</h3>
        </div>
    </div>
</div>
    <script src="../JS/menu.js"></script>
</body>
</html>
