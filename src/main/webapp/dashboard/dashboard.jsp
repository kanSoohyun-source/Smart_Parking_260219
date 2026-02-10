<%@ page import="org.example.smart_parking_260219.service.ParkingService" %>
<%@ page import="org.example.smart_parking_260219.dto.ParkingDTO" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    /*
    class Parking {
        String id, carNum, entryTime;
        Parking(String id, String carNum, LocalDateTime entryTime) {
            this.id = id;
            this.carNum = carNum;
            this.entryTime = String.valueOf(entryTime);
        }
    }

    Parking[] occupied = {
            *//*new Parking("A3", "123가 1234", "09:00"),
            new Parking("A7", "345나 2334", "14:55"),
            new Parking("A12", "567다 8765", "18:27")*//*
            // new Parking(parkingDTO.getSpaceId(), parkingDTO.getCarNum(), parkingDTO.getEntryTime())
    };
    */
    List<ParkingDTO> parkingDTOList = ParkingService.INSTANCE.getAllParking();
    System.out.println(parkingDTOList);
%>

<html>
<head>
    <title>주차 현황</title>
    <link rel="stylesheet" href="../CSS/style.css">
    <link rel="stylesheet" href="../CSS/dashboard.css">
</head>
<body>

<%@ include file="/main/menu.jsp" %>

<div class="main-content">
    <h2>주차 현황</h2>

    <div class="parking-grid">
        <%
            for (int i = 1; i <= 20; i++) {
                String id = "A" + i;
                String carNum = null, entryTime = null;

                for (ParkingDTO dto : parkingDTOList) {
                    if (id.equals(dto.getSpaceId())) {
                        carNum = dto.getCarNum();
                        entryTime = String.valueOf(dto.getEntryTime().toLocalTime());
                        break;
                    }
                }
                System.out.println(carNum);

        %>
        <div class="slot-item <%= carNum == null ? "empty" : "occupied" %>"
             data-id="<%= id %>"
             data-empty="<%= carNum == null %>">

            <div class="slot-title"><%= id %></div>

            <% if (carNum == null) { %>
            <div class="slot-status">공차</div>
            <% } else { %>
            <div class="slot-status"><%= carNum %></div>
            <div class="slot-time"><%= entryTime %> 입차</div>
            <% } %>
        </div>
        <% } %>
    </div>
</div>

<script src="../JS/tlqkf.js"></script>
</body>
</html>