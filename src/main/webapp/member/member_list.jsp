<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원 목록</title>
    <link rel="stylesheet" href="../CSS/style.css">
</head>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
  <!-- Content -->
    <div id="memberList" class="page">
        <h2>회원 목록</h2>
        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <th>번호</th>
                    <th>차량 번호</th>
                    <th>소유주 이름</th>
                    <th>구독 시작일</th>
                    <th>구독 만료일</th>
                </tr>
                </thead>
                <tbody id="memberTableBody"></tbody>
            </table>
        </div>
        <div class="pagination">
            <button onclick="changePage(-1)">◀</button>
            <span id="pageInfo">1 / 1</span>
            <button onclick="changePage(1)">▶</button>
        </div>
        <a href="../member/member_search.jsp"><button>회원 조회</button></a>
    </div>
</div>
    <script src="../JS/menu.js"></script>
    <script src="../JS/function.js"></script>
</body>
</html>
