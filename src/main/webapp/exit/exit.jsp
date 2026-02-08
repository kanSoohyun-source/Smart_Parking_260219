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
<%

%>
<div class="main-content">
  <!-- Content -->
    <div id="exit" class="page">
        <form action="../parking/output" method="post" class="form-horizontal">
            <h2>출차</h2>
            <div class="form-group">
                <label>차량 번호</label>
                <input type="text" id="exitCarNum" placeholder="차량번호 8자리" name="carNum">
            </div>
            <button>정산</button>
        </form>
    </div>
</div>
    <script src="../JS/menu.js"></script>
    <script src="../JS/function.js"></script>
</body>
</html>
