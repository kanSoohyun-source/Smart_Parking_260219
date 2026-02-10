<%@ page import="org.example.smart_parking_260219.service.ParkingService" %>
<%@ page import="org.example.smart_parking_260219.dto.ParkingDTO" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.smart_parking_260219.dto.ParkingSpotDTO" %>
<%@ page import="org.example.smart_parking_260219.service.ParkingSpotService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<ParkingSpotDTO> parkingSpotDTOList = ParkingSpotService.INSTANCE.getAllParkingSpot();

%>

<html>
<head>
    <title>주차 현황</title>
    <link rel="stylesheet" href="../CSS/style.css">
    <link rel="stylesheet" href="../CSS/tlqkf.css">
</head>
<body>

<%@ include file="/main/menu.jsp" %>

<div class="main-content">
    <h2>주차 현황</h2>

    <div class="parking-grid">
        <%
            for (int i = 1; i <= 20; i++) {
                String id = "A" + i;
                String carNum = null;
                Boolean result = ParkingSpotService.INSTANCE.getParkingSpotBySpaceId(id).getEmpty();

                for (ParkingSpotDTO dto : parkingSpotDTOList) {
                    if (id.equals(dto.getSpaceId())) {
                        carNum = dto.getCarNum();
                        break;
                    }
                };
        %>
        <div class="slot-item <%= (!result) ? "empty" : "occupied" %>"
             data-id="<%= id %>"
             data-empty="<%= (!result) %>">

            <div class="slot-title"><%= id %></div>

            <% if (!result) { %>
            <div class="slot-status">공차</div>
            <% } else { %>
            <div class="slot-status"><%= carNum %></div>
            <div class="slot-time"><%=LocalDateTime.now().toLocalTime().withNano(0)%> 입차</div>
            <% } %>
        </div>
        <% } %>
    </div>
</div>

<script src="../JS/tlqkf.js"></script>
</body>
</html>