<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="../CSS/style.css">
    <link rel="stylesheet" href="../CSS/dashboard.css">
</head>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
  <!-- Content -->
    <div class="form-group">
        <div id="dashboard" class="page">
            <h2>주차 현황</h2>
            <div class="parking-grid" id="parkingGrid"></div>
        </div>
    </div>
</div>
    <script src="../JS/menu.js"></script>
    <script src="../JS/tlqkf.js"></script>
</body>
</html>
