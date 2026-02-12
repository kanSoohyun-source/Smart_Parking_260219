<%@ page import="org.example.smart_parking_260219.service.ParkingService" %>
<%@ page import="org.example.smart_parking_260219.dto.ParkingDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.smart_parking_260219.service.ParkingSpotService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<ParkingDTO> parkingDTOList = ParkingService.INSTANCE.getAllParking();
    String failInput = request.getParameter("fail");
    if ("false".equals(failInput)) {
        out.println("<script>alert('이미 입차된 차량입니다.'); history.back();</script>");
    }
%>
<html>
<head>
    <title>주차 현황</title>
    <link rel="stylesheet" href="../../../CSS/style.css">
    <link rel="stylesheet" href="../../../CSS/tlqkf.css">
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
                Boolean result = ParkingSpotService.INSTANCE.getParkingSpotBySpaceId(id).getEmpty();

                for (ParkingDTO dto : parkingDTOList) {
                    if (id.equals(dto.getSpaceId())) {
                        carNum = dto.getCarNum();
                        entryTime = String.valueOf(dto.getEntryTime().toLocalTime());
                        break;
                    }
                };
        %>
        <div class="slot-item <%= (!result) ? "empty" : "occupied" %>"
             data-id="<%= id %>"
             data-empty="<%= (!result) %>"
             data-carnum="<%= carNum %>">

            <div class="slot-title"><%= id %></div>

            <% if (!result) { %>
            <div class="slot-status">공차</div>
            <% } else { %>
            <div class="slot-status"><%= carNum %></div>
            <div class="slot-time"><%= entryTime %> 입차</div>
            <% } %>
        </div>
        <% } %>
    </div>
    <div align="center">
        <form id="searchForm">
            <div class="form-group">
                    <input name="keyword" type="text" class="search-input" value=""/>
                <button type="button" onclick="selectCarNum()">검색</button>
                <button type="button" onclick="cancelCarNum()">취소</button>
            </div>
        </form>
    </div>
</div>
<script src="../../../JS/tlqkf.js"></script>
<script src="../../../JS/parkingList.js"></script>
<script>

</script>
</body>
</html>