<%@ page import="org.example.smart_parking_260219.dto.ParkingDTO" %>
<%@ page import="org.example.smart_parking_260219.dto.MemberDTO" %>
<%@ page import="org.example.smart_parking_260219.service.ParkingService" %>
<%@ page import="org.example.smart_parking_260219.service.MemberService" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.Duration" %>
<%@ page import="java.util.Objects" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/payment_style.css">
</head>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>
<%
    String space = request.getParameter("id");
    if (space == null) {
        space = (String) request.getAttribute("id");
    }
    String carNum = request.getParameter("carNum");
    System.out.println(carNum);
    ParkingDTO parkingDTO = ParkingService.INSTANCE.getParkingByCarNum(carNum);
    long totalTime = Duration.between(parkingDTO.getEntryTime(), LocalDateTime.now()).toMinutes();
%>
<div class="main-content">
  <!-- Content -->
    <div id="register" class="page">
        <h2>출차</h2>
        <form action = "../parking/get" method="post" class="form-horizontal">
            <div class="form-group">
                <label>주차 구역</label>
                <input type="text" id="spaceId" placeholder="주차 구역" name="spaceId" value="<%=parkingDTO.getSpaceId()%>">
            </div>
            <div class="form-group">
                <label>전화 번호</label>
                <input type="text" id="phone" placeholder="전화번호" name="phone" value="<%=parkingDTO.getPhone()%>">
            </div>
            <div class="form-group">
                <label>차량 번호</label>
                <input type="text" id="regCarNum" placeholder="차량번호 8자리" maxlength="8" name="carNum" value="<%=parkingDTO.getCarNum()%>">
            </div>
            <div class="form-group">
                <label>차량 타입</label>
                <div class="radio-group">
                    <label class="radio-item"><input type="radio" name="finish" value="1"
                        <%=(parkingDTO.getCarType() == 1) ? "checked" : ""%>>일반</label>
                    <label class="radio-item"><input type="radio" name="finish" value="2"
                        <%=(parkingDTO.getCarType() == 2) ? "checked" : ""%>>월정액</label>
                    <label class="radio-item"><input type="radio" name="finish" value="3"
                        <%=(parkingDTO.getCarType() == 3) ? "checked" : ""%>>경차</label>
                    <label class="radio-item"><input type="radio" name="finish" value="4"
                        <%=(parkingDTO.getCarType() == 4) ? "checked" : ""%>>장애인</label>
                </div>
            </div>
            <div class="form-group">
                <label>입차 시간</label>
                <input type="text" id="entryTime" placeholder="입차 시간" name="entryTime" value="<%=parkingDTO.getEntryTime()%>">
            </div>
            <button onclick="registerMember()">정산</button>
        </form>
    </div>
</div>
    <script src="${pageContext.request.contextPath}/JS/menu.js"></script>
    <script src="${pageContext.request.contextPath}/JS/function.js"></script>
</body>
</html>
