<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>월정액 가입</title>
  <link rel="stylesheet" href="../CSS/style.css">
  <link rel="stylesheet" href="../CSS/member/add_sub.css">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
  <!-- Content -->
  <div id="register" class="page">
    <h2>월정액 가입</h2>

    <form name="frmAddMember" action="/subscribe/subscribe_add" method="post" onsubmit="return validateForm()">

      <div class="form-group">
        <label for="carNum">차량 번호</label>
        <input type="text" name="carNum" id="carNum" placeholder="예: 123가4567" required>
      </div>

      <div class="form-group">
        <label for="startDate">시작일</label>
        <input type="date" name="startDate" id="startDate">
      </div>

      <div class="form-group">
        <label for="endDate">종료일</label>
        <input type="date" name="endDate" id="endDate">
      </div>

      <div class="button-group">
        <button type="submit">월정액 가입</button>
        <a href="/subscribe/subscribe_list" class="btn btn-secondary">목록</a>
      </div>
    </form>
  </div>
</div>
<script src="../JS/menu.js"></script>
<script src="../JS/function.js"></script>
</body>
</html>
