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
    <div id="entry" class="page">
        <h2>입차</h2>
        <div class="form-group">
            <label>주차 자리</label>
            <input type="text" id="parkingSlot" placeholder="A1 - A20">
            <label>차량 번호</label>
            <input type="text" id="entryCarNum" placeholder="차량번호 8자리">
        </div>
        <button onclick="processEntry()">입차 등록</button>
    </div>
</div>
    <script src="../JS/menu.js"></script>
    <script src="../JS/function.js"></script>
</body>
</html>
