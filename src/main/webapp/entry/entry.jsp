<%@ page import="org.example.smart_parking_260219.dto.MemberDTO" %>
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
    String space = request.getParameter("id");
    String failInput = request.getParameter("fail");
    if ("false".equals(failInput)) {
        out.println("<script>alert('이미 입차된 구역입니다.'); history.back();</script>");
    }
    if ("over".equals(failInput)) {
        out.println("<script>alert('잘못된 형식의 차량 번호입니다..'); history.back();</script>");
    }

%>
<div class="main-content">
  <!-- Content -->
    <div id="entry" class="page">
        <h2>입차</h2>
        <form action="../parking/input" method="post" class="form-horizontal">
            <div class="form-group">
                <label>주차 자리</label>
                <input type="text" id="parkingSlot" placeholder="A1 - A20" name="spaceId" value="<%=(space != null) ? space : ""%>">
                <label>차량 번호</label>
                <input type="text" id="entryCarNum" placeholder="차량번호 8자리" name="carNum">
            </div>
            <button onclick="processEntry()">입차 등록</button>
        </form>

    </div>
</div>
    <script src="../JS/menu.js"></script>
    <script src="../JS/function.js"></script>
</body>
</html>
