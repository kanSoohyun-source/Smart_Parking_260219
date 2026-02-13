<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원 조회</title>
    <link rel="stylesheet" href="../CSS/style.css">
</head>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
  <!-- Content -->
    <div id="memberSearch" class="page">
        <h2>회원 조회</h2>
        <div class="search-box">
            <input type="text" id="searchCarNum" placeholder="검색할 회원의 차량번호를 입력해 주세요">
            <button onclick="searchMember()">검색</button>
        </div>
        <div id="searchResult"></div>
    </div>
</div>
    <script src="../JS/menu.js"></script>
    <script src="../JS/function.js"></script>
</body>
</html>
