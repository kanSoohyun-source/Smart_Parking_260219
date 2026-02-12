<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.smart_parking_260219.dto.MemberDTO" %>
<%@ page import="org.example.smart_parking_260219.dto.SubscribeDTO" %>

<%
  MemberDTO member = (MemberDTO) request.getAttribute("member");
  SubscribeDTO subscribeDTO = (SubscribeDTO) request.getAttribute("subscribe");

  if (member == null) {
    response.sendRedirect("/member/member_search");
    return;
  }

  String carTypeText = "";
  switch (member.getCarType()) {
    case 1: carTypeText = "일반"; break;
    case 2: carTypeText = "월정액"; break;
    case 3: carTypeText = "경차"; break;
    case 4: carTypeText = "장애인"; break;
    default: carTypeText = "알 수 없음";
  }
%>

<html>
<head>
  <title>회원 상세 조회</title>
  <link rel="stylesheet" href="../CSS/style.css">
  <link rel="stylesheet" href="../CSS/member/detail.css">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<body>
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
  <div class="container mt-4" style="max-width: 600px;">
    <h2 class="mb-4">회원 상세 조회</h2>

    <div class="card mb-3">
      <div class="card-header bg-primary text-white font-weight-bold">
        회원 정보
      </div>
      <div class="card-body">
        <div class="form-group row">
          <label class="col-4 col-form-label font-weight-bold">차량 번호</label>
          <div class="col-8">
            <input type="text" class="form-control bg-light" value="<%= member.getCarNum() %>" readonly>
          </div>
        </div>
        <div class="form-group row">
          <label class="col-4 col-form-label font-weight-bold">차량 종류</label>
          <div class="col-8">
            <input type="text" class="form-control bg-light" value="<%= carTypeText %>" readonly>
          </div>
        </div>
        <div class="form-group row">
          <label class="col-4 col-form-label font-weight-bold">회원 이름</label>
          <div class="col-8">
            <input type="text" class="form-control bg-light" value="<%= member.getName() %>" readonly>
          </div>
        </div>
        <div class="form-group row">
          <label class="col-4 col-form-label font-weight-bold">전화번호</label>
          <div class="col-8">
            <input type="text" class="form-control bg-light" value="<%= member.getPhone() %>" readonly>
          </div>
        </div>
        <% if (member.getCreateDateOnly() != null) { %>
        <div class="form-group row mb-0">
          <label class="col-4 col-form-label font-weight-bold">가입일</label>
          <div class="col-8">
            <input type="text" class="form-control bg-light" value="<%= member.getCreateDateOnly() %>" readonly>
          </div>
        </div>
        <% } %>
      </div>
    </div>

    <!-- 월정액 회원인 경우에만 표시 -->
    <% if (subscribeDTO != null) { %>
    <div class="card mb-3">
      <div class="card-header bg-info text-white font-weight-bold">
        월정액 정보
      </div>
      <div class="card-body">
        <div class="form-group row">
          <label class="col-4 col-form-label font-weight-bold">구독 시작일</label>
          <div class="col-8">
            <input type="text" class="form-control bg-light"
                   value="<%= subscribeDTO.getStartDate() != null ? subscribeDTO.getStartDate() : "-" %>" readonly>
          </div>
        </div>
        <div class="form-group row">
          <label class="col-4 col-form-label font-weight-bold">구독 만료일</label>
          <div class="col-8">
            <input type="text" class="form-control bg-light"
                   value="<%= subscribeDTO.getEndDate() != null ? subscribeDTO.getEndDate() : "-" %>" readonly>
          </div>
        </div>
        <div class="form-group row">
          <label class="col-4 col-form-label font-weight-bold">구독 상태</label>
          <div class="col-8">
            <input type="text" class="form-control bg-light"
                   value="<%= subscribeDTO.isStatus() ? "가입중" : "미가입" %>" readonly>
          </div>
        </div>
      </div>
    </div>
    <% } %>

    <div class="d-flex" style="gap: 10px;">
      <a href="/member/member_modify?carNum=<%= member.getCarNum() %>"
         class="btn btn-warning flex-fill text-white me-2">수정</a>
      <a href="/member/member_list"
         class="btn btn-secondary flex-fill me-2">목록</a>
      <button type="button" class="btn btn-danger flex-fill me-2"
              onclick="deleteMember('<%= member.getCarNum() %>')">삭제</button>
    </div>

  </div>
</div>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<script src="../JS/menu.js"></script>
<script src="../JS/member/detail"></script>
</body>
</html>