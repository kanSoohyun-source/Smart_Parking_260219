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
        <h2>월정액 회원 등록</h2>

        <form action="/member/member_add" method="post" onsubmit="return validateForm()">

            <div class="form-group">
                <label for="carNum">차량 번호</label>
                <input type="text" class="form-control" name="carNum" id="carNum"
                       placeholder="예: 123가4567" required>
            </div>

            <div class="form-group">
                <label for="carType">차량 종류</label>
                <select class="form-control" name="carType" id="carType" required>
                    <option value="1">일반</option>
                    <option value="2" selected>월정액</option>
                    <option value="3">경차</option>
                    <option value="4">장애인</option>
                </select>
            </div>

            <div class="form-group">
                <label for="name">회원 이름</label>
                <input type="text" class="form-control" name="name" id="name"
                       placeholder="홍길동" required>
            </div>

            <div class="form-group">
                <label for="phone">연락처</label>
                <input type="tel" class="form-control" name="phone" id="phone"
                       placeholder="010-1234-5678" required>
            </div>

            <!-- ✅ 월정액 날짜 필수 입력 -->
            <div class="card mt-3 mb-3">
                <div class="card-header bg-info text-white font-weight-bold">
                    월정액 정보 <span class="text-warning">*필수</span>
                </div>
                <div class="card-body">
                    <div class="form-group">
                        <label for="startDate">구독 시작일</label>
                        <input type="date" class="form-control" name="startDate"
                               id="startDate" required>
                    </div>
                    <div class="form-group mb-0">
                        <label for="endDate">구독 종료일</label>
                        <input type="date" class="form-control" name="endDate"
                               id="endDate" required>
                    </div>
                </div>
            </div>

            <div class="d-flex mt-3">
                <button type="submit" class="btn btn-primary flex-fill mr-2">등록</button>
                <a href="/member/member_list" class="btn btn-secondary flex-fill">목록</a>
            </div>

        </form>
    </div>
</div>

<script src="../JS/menu.js"></script>
<script src="../JS/function.js"></script>
<script src="../JS/member/add.js"></script>
</body>
</html>