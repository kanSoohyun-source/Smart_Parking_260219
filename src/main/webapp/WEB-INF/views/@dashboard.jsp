<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>주차 현황 - 주차장 관리 시스템</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/dashboard.css">
</head>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>

<div class="main-content">
    <!-- Content -->
    <div class="form-group">
        <div id="dashboard" class="page">
            <h2>주차 현황</h2>
            <div class="parking-grid" id="parkingGrid">
                <!-- 주차 현황 데이터가 여기에 표시됩니다 -->
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/JS/menu.js"></script>
<script src="${pageContext.request.contextPath}/JS/dashboard.js"></script>
</body>
</html>
