<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.smart_parking_260219.dto.MemberDTO" %>

<%
    MemberDTO member = (MemberDTO) request.getAttribute("member");
    if (member == null) {
        response.sendRedirect("/member/member_list");
        return;
    }
    String error = request.getParameter("error");
%>
<html>
<head>
    <title>월정액 회원 수정</title>
    <link rel="stylesheet" href="../CSS/style.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<body>
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
    <div class="container mt-4" style="max-width: 600px;">
        <h2 class="mb-4">월정액 회원 수정</h2>

        <% if ("modifyFail".equals(error)) { %>
        <div class="alert alert-danger">수정 중 오류가 발생했습니다.</div>
        <% } %>

        <form action="/member/member_modify" method="post" onsubmit="return validateForm()">

            <input type="hidden" name="memberId" value="<%= member.getMemberId() %>">
            <input type="hidden" name="carNum" value="<%= member.getCarNum() %>">

            <!-- 회원 정보 -->
            <div class="card mb-3">
                <div class="card-header bg-primary text-white font-weight-bold">회원 정보</div>
                <div class="card-body">
                    <div class="form-group">
                        <label>차량 번호</label>
                        <input type="text" class="form-control bg-light"
                               value="<%= member.getCarNum() %>" readonly>
                        <small class="text-muted">차량번호는 수정할 수 없습니다.</small>
                    </div>
                    <div class="form-group">
                        <label>회원 이름 <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="name" name="name"
                               value="<%= member.getName() %>" required>
                    </div>
                    <div class="form-group mb-0">
                        <label>전화번호 <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="phone" name="phone"
                               value="<%= member.getPhone() %>" required placeholder="010-1234-5678">
                    </div>
                </div>
            </div>

            <!-- ✅ 월정액 갱신 필수 -->
            <div class="card mb-3">
                <div class="card-header bg-info text-white font-weight-bold">
                    월정액 갱신 <span class="text-warning">*필수</span>
                </div>
                <div class="card-body">
                    <!-- 현재 구독 정보 표시 -->
                    <div class="alert alert-secondary mb-3">
                        현재 구독 기간:
                        <%= member.getStartDate() != null ? member.getStartDate() : "-" %>
                        ~
                        <%= member.getEndDate() != null ? member.getEndDate() : "-" %>
                    </div>

                    <div class="form-group">
                        <label>새 시작일 <span class="text-danger">*</span></label>
                        <input type="date" class="form-control" name="startDate" id="startDate"
                               value="<%= member.getStartDate() != null ? member.getStartDate() : "" %>"
                               required>
                    </div>
                    <div class="form-group mb-0">
                        <label>새 종료일 <span class="text-danger">*</span></label>
                        <input type="date" class="form-control" name="endDate" id="endDate"
                               value="<%= member.getEndDate() != null ? member.getEndDate() : "" %>"
                               required>
                    </div>
                </div>
            </div>

            <div class="d-flex mt-3">
                <button type="submit" class="btn btn-primary flex-fill mr-2">수정 완료</button>
                <a href="/member/member_detail?carNum=<%= member.getCarNum() %>"
                   class="btn btn-secondary flex-fill">취소</a>
            </div>

        </form>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<script src="../JS/menu.js"></script>
<script src="../JS/member/modify.js"></script>

</body>
</html>