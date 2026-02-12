<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>비회원 등록</title>
    <link rel="stylesheet" href="../CSS/style.css">
</head>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>
<%
    String space = (String) request.getAttribute("id");
    String carNum = (String) request.getAttribute("carNum");
    System.out.println("spaceId : " + space);
%>
<div class="main-content">
    <!-- Content -->
    <div id="exit" class="page">
        <form action="../parking/nonMember" method="post" class="form-horizontal">
            <input type="hidden" id="id" name="id" value="<%=space%>">
            <input type="hidden" id="carNum" name="carNum" value="<%=carNum%>">
            <h2>비회원 입차 출력</h2>
            <div class="form-group">
                <label>차량 타입</label>
                <label><input type="radio" name="finish" value="1">일반</label>
                <label><input type="radio" name="finish" value="2">월정액</label>
                <label><input type="radio" name="finish" value="3">경차</label>
                <label><input type="radio" name="finish" value="4">장애인</label>
            </div>
            <button>입차 등록</button>
        </form>
    </div>
</div>
<script src="../JS/menu.js"></script>
<script src="../JS/function.js"></script>
</body>
</html>