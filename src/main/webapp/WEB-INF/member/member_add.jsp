<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원 등록</title>
    <link rel="stylesheet" href="../CSS/style.css">
    <link rel="stylesheet" href="../CSS/member/add.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<body>
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
    <div id="register" class="page">
        <h2>회원 등록</h2>

        <form name="frmAddMember" action="/member/member_add" method="post" onsubmit="return validateForm()">

            <div class="form-group">
                <label for="carNum">차량 번호</label>
                <input type="text" name="carNum" id="carNum" placeholder="예: 123가4567" required>
                <%--차량 번호 중복성 검사--%>
                <small id="carNumMsg"></small>
            </div>

            <div class="form-group">
                <label for="carType">차량 종류</label>
                <select class="form-control" name="carType" id="carType" required>
                    <option value="1">일반</option>
                    <option value="2">월정액</option>
                    <option value="3">경차</option>
                    <option value="4">장애인</option>
                </select>
            </div>

            <div class="form-group">
                <label for="name">회원 이름</label>
                <input type="text" name="name" id="name" placeholder="홍길동" required>
            </div>

            <div class="form-group">
                <label for="phone">연락처</label>
                <input type="tel" name="phone" id="phone" placeholder="010-1234-5678" required>
            </div>

            <div class="form-group">
                <label for="subscribed">월정액 가입 여부</label>
                <select class="form-control" name="subscribed" id="subscribed">
                    <option value="false">미가입</option>
                    <option value="true">가입</option>
                </select>
            </div>

            <!-- 월정액 가입 선택 시 날짜 필드 표시 -->
            <div id="subscribeDateFields" style="display: none;">
                <div class="form-group">
                    <label for="startDate">구독 시작일</label>
                    <input type="date" class="form-control" name="startDate" id="startDate">
                </div>
                <div class="form-group">
                    <label for="endDate">구독 종료일</label>
                    <input type="date" class="form-control" name="endDate" id="endDate">
                </div>
            </div>

            <div class="d-flex mt-3" style="gap: 10px;">
                <button type="submit" class="btn btn-primary flex-fill">가입</button>
                <a href="/member/member_list" class="btn btn-secondary flex-fill">목록</a>
            </div>

        </form>
    </div>
</div>
<script src="../JS/menu.js"></script>
<script src="../JS/function.js"></script>
<script scr="../JS/member/add.js"></script>
</body>
</html>