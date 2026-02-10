<%@ page import="org.example.smart_parking_260219.dto.ParkingDTO" %>
<%@ page import="org.example.smart_parking_260219.dto.MemberDTO" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="org.example.smart_parking_260219.service.ParkingService" %>
<%@ page import="org.example.smart_parking_260219.service.MemberService" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="org.example.smart_parking_260219.dao.ParkingDAO" %>
<%@ page import="org.example.smart_parking_260219.dao.ParkingDAOImpl" %><%--
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
    String carNum = (String) request.getAttribute("carNum");
    ParkingDTO parkingDTO = ParkingService.INSTANCE.getParkingByCarNum(carNum);
    MemberDTO memberDTO = null;
    try {
        memberDTO = MemberService.Instance.getOneMember(carNum);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
%>
<div class="main-content">
  <!-- Content -->
    <div id="register" class="page">
        <h2>출차</h2>
        <form action = "../parking/get" method="post" class="form-horizontal">
            <div class="form-group">
                <label>주차 구역</label>
                <input type="text" id="spaceId" placeholder="주차 구역" value="<%=parkingDTO.getSpaceId()%>">
            </div>
            <div class="form-group">
                <label>전화 번호</label>
                <input type="text" id="phone" placeholder="전화번호" value="<%=memberDTO.getPhone()%>">
            </div>
            <div class="form-group">
                <label>차량 번호</label>
                <input type="text" id="regCarNum" placeholder="차량번호 8자리" maxlength="8" value="<%=memberDTO.getCarNum()%>">
            </div>
            <div class="form-group">
                <label>총 주차 시간</label>
                <input type="text" id="totalParkingTime" placeholder="총 주차 시간" value="<%=parkingDTO.getTotalTime()%>">
            </div>
            <div class="form-group">
                <label>입차 시간</label>
                <input type="text" id="entryTime" placeholder="입차 시간" value="<%=parkingDTO.getEntryTime()%>">
            </div>
            <button onclick="registerMember()">정산</button>

        </form>
    </div>

</div>
    <script src="../JS/menu.js"></script>
    <script src="../JS/function.js"></script>
</body>
</html>
