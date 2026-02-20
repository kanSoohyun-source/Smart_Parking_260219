<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.smart_parking_260219.dto.MemberDTO" %>
<%
  MemberDTO member = (MemberDTO) request.getAttribute("member");
  if (member == null) {
    response.sendRedirect("/member/member_list");
    return;
  }
  String listPage = (String) request.getAttribute("page");
  if (listPage == null || listPage.isEmpty()) listPage = "1";
  String listUrl = "/member/member_list?page=" + listPage;
%>
<html>
<head>
  <title>회원 상세</title>
  <link rel="stylesheet" href="../CSS/style.css">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<body>
<%@ include file="../main/menu.jsp" %>
<div class="main-content">
  <div class="container mt-4" style="max-width: 600px;">
    <h2 class="mb-4">월정액 회원 상세</h2>

    <!-- 회원 정보 -->
    <div class="card mb-3">
      <div class="card-header bg-primary text-white font-weight-bold">회원 정보</div>
      <div class="card-body">
        <div class="form-group row">
          <label class="col-4 col-form-label font-weight-bold">차량 번호</label>
          <div class="col-8">
            <input type="text" class="form-control bg-light"
                   value="<%= member.getCarNum() %>" readonly>
          </div>
        </div>
        <div class="form-group row">
          <label class="col-4 col-form-label font-weight-bold">차량 종류</label>
          <div class="col-8">
            <input type="text" class="form-control bg-light"
                   value="<%= member.CarTypeText() %>" readonly>
          </div>
        </div>
        <div class="form-group row">
          <label class="col-4 col-form-label font-weight-bold">회원 이름</label>
          <div class="col-8">
            <input type="text" class="form-control bg-light"
                   value="<%= member.getName() %>" readonly>
          </div>
        </div>
        <div class="form-group row">
          <label class="col-4 col-form-label font-weight-bold">전화번호</label>
          <div class="col-8">
            <input type="text" class="form-control bg-light"
                   value="<%= member.getPhone() %>" readonly>
          </div>
        </div>
        <% if (member.getCreateDate() != null) { %>
        <div class="form-group row mb-0">
          <label class="col-4 col-form-label font-weight-bold">등록일</label>
          <div class="col-8">
            <input type="text" class="form-control bg-light"
                   value="<%= member.getCreateDate().toString().replace('T', ' ') %>" readonly>
          </div>
        </div>
        <% } %>
      </div>
    </div>

    <!-- 월정액 정보 (구독 상태만 표시) -->
    <div class="card mb-3">
      <div class="card-header bg-info text-white font-weight-bold">월정액 정보</div>
      <div class="card-body">
        <div class="form-group row mb-0">
          <label class="col-4 col-form-label font-weight-bold">구독 상태</label>
          <div class="col-8 d-flex align-items-center">
            <% if (member.isSubscribed()) { %>
            <span class="badge badge-success p-2">구독중</span>
            <% } else { %>
            <span class="badge badge-secondary p-2">만료</span>
            <% } %>
          </div>
        </div>
      </div>
    </div>

    <!-- 버튼 -->
    <div class="d-flex mt-3">
      <a href="/member/member_modify?carNum=<%= member.getCarNum() %>&page=<%= listPage %>"
         class="btn btn-warning flex-fill mr-2 text-white">수정</a>
      <a href="<%= listUrl %>"
         class="btn btn-secondary flex-fill mr-2">목록</a>
      <button type="button" class="btn btn-danger flex-fill"
              onclick="deleteMember('<%= member.getCarNum() %>')">삭제</button>
    </div>

    <!-- 삭제 확인 모달 -->
    <div class="modal fade" id="deleteModal" tabindex="-1">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header bg-danger text-white">
            <h5 class="modal-title">회원 삭제</h5>
          </div>
          <div class="modal-body">
            정말 삭제하시겠습니까?<br>
            <strong id="deleteCarNum"></strong>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
            <button type="button" class="btn btn-danger" id="confirmDeleteBtn">삭제</button>
          </div>
        </div>
      </div>
    </div>

  </div>
</div>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<script src="../JS/menu.js"></script>
<script src="../JS/member/detail.js"></script>
</body>
</html>