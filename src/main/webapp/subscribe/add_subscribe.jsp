<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>월정액 가입</title>
  <link rel="stylesheet" href="../CSS/style.css">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
  <!-- Content -->
  <div id="register" class="page">
    <h2>월정액 가입</h2>

    <form name="frmAddMember" action="/subscribe/add_subscribe" method="post" onsubmit="return validateForm()">

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
        <a href="/subscribe/list_subscribe.jsp" class="btn btn-secondary">목록</a>
      </div>
    </form>
  </div>
</div>
<script src="../JS/menu.js"></script>
<script src="../JS/function.js"></script>
<script>
    function validateForm() {
    const carNum = document.getElementById('carNum').value.trim();
    const startDate = document.getElementById('startDate').value.trim();
    const endDate = document.getElementById('endDate').value.trim();

    if (!carNum) {
    alert('차량 번호를 입력해주세요.');
    return false;
  }

    if (!startDate) {
    alert('시작일을 입력해주세요.');
    return false;
  }

    if (!endDate) {
    alert('종료일을 입력해주세요.');
    return false;
  }

    // ✅ 날짜 유효성 검사 추가
    if (new Date(startDate) >= new Date(endDate)) {
    alert('종료일은 시작일보다 이후여야 합니다.');
    return false;
  }
    return true;
  }
</script>
<style>
  /* 라디오 버튼 그룹 */
  .radio-group {
    display: flex !important;
    flex-direction: row !important;
    gap: 20px;
  }

  .radio-group label {
    display: inline-flex !important;
    flex-direction: row !important;
    align-items: center !important;
    gap: 5px;
    cursor: pointer;
    white-space: nowrap;
  }

  .radio-group input[type="radio"] {
    cursor: pointer;
    margin: 0 5px 0 0 !important;
  }

  /* 버튼 그룹 */
  .button-group {
    display: flex;
    gap: 10px;
    margin-top: 20px;
  }

  .button-group button {
    flex: 1;
  }

  .button-group button[type="button"] {
    background-color: #6c757d;
  }

  .button-group button[type="button"]:hover {
    background-color: #5a6268;
  }

</style>
</body>
</html>
