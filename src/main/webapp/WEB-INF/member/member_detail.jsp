<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.smart_parking_260219.dto.MemberDTO" %>

<%
  MemberDTO member = (MemberDTO) request.getAttribute("member");
  if (member == null) {
    response.sendRedirect("/member/member_list");
    return;
  }
  // 수정/삭제 결과 파라미터
  String success = request.getParameter("success");
  String error = request.getParameter("error");
%>
<html>
<head>
  <title>월정액 회원 상세</title>
  <link rel="stylesheet" href="../CSS/style.css">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<body>
<%@ include file="/main/menu.jsp" %>
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
            <input type="text" class="form-control bg-light" value="<%= member.getCarNum() %>" readonly>
          </div>
        </div>

        <div class="form-group row">
          <label class="col-4 col-form-label font-weight-bold">차량 종류</label>
          <div class="col-8">
            <!-- ✅ 숫자 → 텍스트 변환 -->
            <input type="text" class="form-control bg-light" value="<%=member.CarTypeText() %>" readonly>
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
        <% if (member.getCreateDate() != null) { %>
        <div class="form-group row mb-0">
          <label class="col-4 col-form-label font-weight-bold">등록일</label>
          <div class="col-8">
            <input type="text" class="form-control bg-light"
                   value="<%= member.getCreateDate().toString().replace("T", "") %>" readonly>
          </div>
        </div>
        <% } %>
      </div>
    </div>

    <!-- 월정액 정보 -->
    <div class="card mb-3">
      <div class="card-header bg-info text-white font-weight-bold">월정액 정보</div>
      <div class="card-body">
        <div class="form-group row">
          <label class="col-4 col-form-label font-weight-bold">구독 상태</label>
          <div class="col-8">
            <input type="text" class="form-control bg-light"
                   value="<%= member.isSubscribed() ? "구독중" : "만료" %>" readonly>
          </div>
        </div>
        <div class="form-group row">
          <label class="col-4 col-form-label font-weight-bold">구독 시작일</label>
          <div class="col-8">
            <input type="text" class="form-control bg-light"
                   value="<%= member.getStartDate() != null ? member.getStartDate() : "-" %>" readonly>
          </div>
        </div>
        <div class="form-group row mb-0">
          <label class="col-4 col-form-label font-weight-bold">구독 종료일</label>
          <div class="col-8">
            <input type="text" class="form-control bg-light"
                   value="<%= member.getEndDate() != null ? member.getEndDate() : "-" %>" readonly>
          </div>
        </div>
      </div>

      <div class="form-group row mb-0">
        <label class="col-4 col-form-label font-weight-bold">구독 비용</label>
        <div class="col-8">
          <input type="text" class="form-control bg-light"
                 value="<%= member.getSubscribedFee() %>" readonly>
        </div>
      </div>
    </div>

    <div class="d-flex mt-3">
      <a href="/member/member_modify?carNum=<%= member.getCarNum() %>"
         class="btn btn-warning flex-fill mr-2 text-white">수정</a>
      <a href="/member/member_list"
         class="btn btn-secondary flex-fill mr-2">목록</a>
      <button type="button" class="btn btn-danger flex-fill"
              onclick="deleteMember('<%= member.getCarNum() %>')">삭제</button>
    </div>

    <!-- ✅ 수정 완료 모달 -->
    <div class="modal fade" id="successModal" tabindex="-1">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header bg-success text-white">
            <h5 class="modal-title">✅ 수정 완료</h5>
          </div>
          <div class="modal-body">
            회원 정보가 성공적으로 수정되었습니다.
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-success" data-dismiss="modal">확인</button>
          </div>
        </div>
      </div>
    </div>

    <!-- ✅ 수정 실패 모달 -->
    <div class="modal fade" id="errorModal" tabindex="-1">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header bg-danger text-white">
            <h5 class="modal-title">❌ 수정 실패</h5>
          </div>
          <div class="modal-body">
            회원 정보 수정 중 오류가 발생했습니다. 다시 시도해주세요.
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-danger" data-dismiss="modal">확인</button>
          </div>
        </div>
      </div>
    </div>

    <!-- ✅ 삭제 확인 모달 -->
    <div class="modal fade" id="deleteModal" tabindex="-1">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header bg-danger text-white">
            <h5 class="modal-title">🗑️ 회원 삭제</h5>
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