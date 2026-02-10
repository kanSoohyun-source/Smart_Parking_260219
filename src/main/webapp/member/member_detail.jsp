<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.smart_parking_260219.dto.MemberDTO" %>
<%@ page import="org.example.smart_parking_260219.dto.SubscribeDTO" %>

<%
  MemberDTO member = (MemberDTO) request.getAttribute("member");
  SubscribeDTO subscribeDTO = (SubscribeDTO) request.getAttribute("subscribe");

  if (member == null) {
    response.sendRedirect("/member/member_search.jsp");
    return;
  }

  // carType(int)을 한글로 변환
  String carTypeText = "";
  switch(member.getCarType()) {
      case 1: carTypeText = "일반"; break;
      case 2: carTypeText = "월정액"; break;
      case 3: carTypeText = "경차"; break;
      case 4: carTypeText = "장애인"; break;
  }
%>

<html>
<head>
  <title>회원 상세 조회</title>
  <link rel="stylesheet" href="../CSS/style.css">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<body>
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
  <div id="member-detail" class="page">
    <h2>회원 상세 조회</h2>

    <div class="member-detail">
      <div class="form-group">
        <label>차량 번호</label>
        <input type="text" value="<%= member.getCarNum() %>" readonly>
      </div>

      <div class="form-group">
        <label>차량 종류</label>
        <input type="text" value="<%= carTypeText %>" readonly>
      </div>

      <div class="form-group">
        <label>회원 이름</label>
        <input type="text" value="<%= member.getName() %>" readonly>
      </div>

      <div class="form-group">
        <label>회원 전화번호</label>
        <input type="text" value="<%= member.getPhone() %>" readonly>
      </div>

      <% if (member.getCreateDateOnly() != null) { %>
      <div class="form-group">
        <label>가입일</label>
        <input type="text" value="<%= member.getCreateDateOnly() %>" readonly>
      </div>
      <% } %>

      <!-- 월정액 회원인 경우에만 표시 -->
      <% if (subscribeDTO != null) { %>
      <hr style="margin: 30px 0; border: none; border-top: 1px solid #ddd;">
      <h4 style="color: #3498db; margin-bottom: 20px;">월정액 정보</h4>

      <div class="form-group">
        <label>구독 시작일</label>
        <input type="text" value="<%= subscribeDTO.getStartDate() != null ? subscribeDTO.getStartDate() : "-" %>" readonly>
      </div>

      <div class="form-group">
        <label>구독 만료일</label>
        <input type="text" value="<%= subscribeDTO.getEndDate() != null ? subscribeDTO.getEndDate() : "-" %>" readonly>
      </div>

      <div class="form-group">
        <label>구독 상태</label>
        <input type="text" value="<%= subscribeDTO.isStatus() ? "가입중" : "미가입" %>" readonly>
      </div>

      <% } %>

      <div class="button-group">
        <a href="/member/member_list" class="btn btn-secondary">목록</a>
      </div>
    </div>
  </div>
</div>

<script src="../JS/menu.js"></script>
<style>
  .member-detail {
    max-width: 600px;
    margin: 30px auto;
    background: white;
    border: 1px solid #ddd;
    border-radius: 8px;
    padding: 30px;
  }

  .form-group {
    margin-bottom: 20px;
  }

  .form-group label {
    display: block;
    color: #555;
    font-weight: 600;
    margin-bottom: 8px;
  }

  .form-group input {
    width: 100%;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 14px;
  }

  input[readonly] {
    background-color: #f8f9fa;
    cursor: default;
    color: #495057;
  }

  input[readonly]:focus {
    border-color: #ddd;
    outline: none;
  }

  .button-group {
    display: flex;
    gap: 10px;
    margin-top: 30px;
  }

  .btn {
    flex: 1;
    padding: 12px 20px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    text-decoration: none;
    display: inline-block;
    font-size: 14px;
    text-align: center;
    transition: all 0.3s;
  }

  .btn-primary {
    background-color: #3498db;
    color: white;
  }

  .btn-primary:hover {
    background-color: #2980b9;
  }

  .btn-secondary {
    background-color: #95a5a6;
    color: white;
  }

  .btn-secondary:hover {
    background-color: #7f8c8d;
  }

  h4 {
    font-size: 18px;
  }
</style>
</body>
</html>