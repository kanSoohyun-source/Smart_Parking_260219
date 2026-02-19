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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/dashboard.css">
</head>
<body>
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
    <div id = "dashboard" class="page">
        <h2>주차 현황</h2>
        <div class="parking-grid">
            <%
                // A1 ~ A20까지 전체 주차 공간 출력
                for (int i = 1; i <= 20; i++) {
                    String id = "A" + i;
                    String carNum = null;
                    String entryTime = null;
                    Boolean result = ParkingSpotService.INSTANCE.getParkingSpotBySpaceId(id).getEmpty();

                    // 입차된 차량이라면 차 번호와 입차 시간도 같이 출력
                    for (ParkingDTO dto : parkingDTOList) {
                        if (id.equals(dto.getSpaceId())) {
                            carNum = dto.getCarNum();
                            entryTime = String.valueOf(dto.getEntryTime().toLocalTime());
                            break;
                        }
                    };
            %>
            <%-- 입차 여부에 따라 주차 공간 출력 --%>
            <div class="slot-item <%= (result) ? "empty" : "occupied" %>"
                 data-id="<%= id %>"
                 data-empty="<%= (result) %>"
                 data-carnum="<%= carNum %>">

                <div class="slot-title"><%= id %></div>

                <% if (result) { %>
                <div class="slot-status">공차</div>
                <% } else { %>
                <div class="slot-status"><%= carNum %></div>
                <div class="slot-time"><%= entryTime %> 입차</div>
                <% } %>
            </div>
            <% } %>
        </div>
        <%-- 주차 차량 조회 --%>
        <div align="center">
            <form id="searchForm">
                <div class="form-group">
                    <input name="keyword" type="text" class="search-input" placeholder="조회할 차량 번호 입력" value=""/>
                    <button type="button" onclick="selectCarNum()">검색</button>
                    <button type="button" onclick="cancelCarNum()">취소</button>
                </div>
            </form>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/JS/tlqkf.js"></script>
<script src="${pageContext.request.contextPath}/JS/parkingList.js"></script>
<script>const contextPath = "${pageContext.request.contextPath}";</script>
</body>
</html>