<%@ page import="org.example.smart_parking_260219.dto.ParkingSpotDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="org.example.smart_parking_260219.service.ParkingSpotService" %>
<%@ page import="org.example.smart_parking_260219.service.ParkingService" %>
<%@ page import="java.time.LocalDateTime" %><%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 26. 1. 28.
  Time: 오후 9:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<ParkingSpotDTO> dtoList = (List<ParkingSpotDTO>) request.getAttribute("dtoList");
    dtoList = new ArrayList<>(dtoList);   // 불변 리스트 → 가변 리스트로 변환
    dtoList.sort(Comparator.comparing(ParkingSpotDTO::getSpaceId).reversed());
    int no = ParkingSpotService.INSTANCE.getAllParkingSpot().size() - ParkingSpotService.INSTANCE.getEmptyParkingSpot().size();
%>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
    <style>
        .click-row {
            cursor: pointer;
        }
    </style>
</head>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
  <!-- 제목 -->
  <div id="entry" class="page">
    <h2> 출차 </h2>
   <!-- 내용 -->
    <div class="form-group">
        <div class="text-right">
            <span class="badge badge-success">전체 <%=no%>건</span>
        </div>
        <div style="padding-top: 50px">
            <table class="table table-hover">
                <tr>
                    <th>주차 구역</th>
                    <th>차량 번호</th>
                    <th>입차 시간</th>
                </tr>
                <%
                    for (ParkingSpotDTO parkingSpotDTO : dtoList) {
                        if (parkingSpotDTO.getEmpty()) {
                %>
                <tr class="click-row"
                data-url="${pageContext.request.contextPath}/get?id=<%=parkingSpotDTO.getSpaceId()%>&carNum=<%=parkingSpotDTO.getCarNum()%>">
                    <td>
                        <%=parkingSpotDTO.getSpaceId()%>
                    </td>o
                    <td>
                        <%=parkingSpotDTO.getCarNum()%>
                    </td>
                    <td class="time">
                        <%=parkingSpotDTO.getLastUpdate()%>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
            </table>
        </div>
        <hr>
    </div>
  </div>
</div>
    <script src="${pageContext.request.contextPath}/JS/menu.js"></script>
    <script src="${pageContext.request.contextPath}/JS/function.js"></script>
    <script>
        document.addEventListener("click", function (e) {
            const row = e.target.closest(".click-row");
            if (!row) return;

            const url = row.dataset.url;
            if (url) {
                location.href = url;
            }
        });
        function formatDateTime(dtStr) {
            if(!dtStr || dtStr === "null" || dtStr === "") return "-";
            return dtStr.replace('T', ' ').substring(0, 16);
        }

        document.addEventListener("DOMContentLoaded", function() {
            document.querySelectorAll(".time").forEach(td => {
                td.textContent = formatDateTime(td.textContent.trim());
            });
        });
    </script>
</body>
</html>
