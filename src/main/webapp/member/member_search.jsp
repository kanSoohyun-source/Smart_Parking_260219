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
            <form action="/member/member_detail" method="get">
                <input type="text" id="getOneMember" name="carNum" placeholder="차량번호 뒤 4자리를 입력해 주세요" maxlength="4" required>
                <button type="submit">검색</button>
            </form>
        </div>
        <div id="searchResult"></div>
    </div>
</div>
<script src="../JS/menu.js"></script>
</body>
</html>
