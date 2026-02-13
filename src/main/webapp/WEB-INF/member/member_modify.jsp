<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.smart_parking_260219.dto.MemberDTO" %>
<%@ page import="org.example.smart_parking_260219.dto.SubscribeDTO" %>

<%
    MemberDTO member = (MemberDTO) request.getAttribute("member");
    SubscribeDTO subscribe = (SubscribeDTO) request.getAttribute("subscribe");

    if (member == null) {
        response.sendRedirect("/member/member_list");
        return;
    }

    String error = request.getParameter("error");
    boolean hasSubscribe = (subscribe != null);
    boolean isActive = hasSubscribe && subscribe.isStatus();
%>

<html>
<head>
    <title>회원 정보 수정</title>
    <link rel="stylesheet" href="../CSS/style.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<body>
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
    <div class="container mt-4" style="max-width: 600px;">
        <h2 class="mb-4">회원 정보 수정</h2>

        <% if ("modifyFail".equals(error)) { %>
        <div class="alert alert-danger">수정 중 오류가 발생했습니다.</div>
        <% } %>

        <form action="/member/member_modify" method="post" onsubmit="return validateForm()">

            <input type="hidden" name="memberId" value="<%= member.getMemberId() %>">
            <input type="hidden" name="carNum" value="<%= member.getCarNum() %>">

            <!-- 회원 정보 변경 -->
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

                    <div class="form-group">
                        <label>전화번호 <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="phone" name="phone"
                               value="<%= member.getPhone() %>" required placeholder="010-1234-5678">
                    </div>

                    <div class="form-group">
                        <label>차량 종류 <span class="text-danger">*</span></label>
                        <select class="form-control" name="carType" required>
                            <option value="1" <%= member.getCarType() == 1 ? "selected" : "" %>>일반</option>
                            <option value="2" <%= member.getCarType() == 2 ? "selected" : "" %>>월정액</option>
                            <option value="3" <%= member.getCarType() == 3 ? "selected" : "" %>>경차</option>
                            <option value="4" <%= member.getCarType() == 4 ? "selected" : "" %>>장애인</option>
                        </select>
                    </div>

                </div>
            </div>

            <!-- 월정액 변경 -->
            <div class="card mb-3">
                <div class="card-header bg-info text-white font-weight-bold">월정액 정보</div>
                <div class="card-body">

                    <% if (isActive) { %>
                    <div class="alert alert-success mb-3">
                        현재 월정액 구독 중
                        (<%= subscribe.getStartDate() %> ~ <%= subscribe.getEndDate() %>)
                    </div>
                    <div class="form-group">
                        <label>월정액 처리</label>
                        <select class="form-control" name="subscribeAction" id="subscribeAction"
                                onchange="toggleDateFields()">
                            <option value="none">변경 없음</option>
                            <option value="extend">기간 연장</option>
                            <option value="cancel">월정액 해지</option>
                        </select>
                    </div>
                    <% } else { %>
                    <div class="alert alert-secondary mb-3">현재 월정액 미가입 상태입니다.</div>
                    <div class="form-group">
                        <label>월정액 처리</label>
                        <select class="form-control" name="subscribeAction" id="subscribeAction"
                                onchange="toggleDateFields()">
                            <option value="none">변경 없음</option>
                            <option value="add">월정액 가입</option>
                        </select>
                    </div>
                    <% } %>

                    <!-- 날짜 입력 필드 -->
                    <div id="dateFields" style="display: none;">
                        <div class="form-group">
                            <label>시작일</label>
                            <input type="date" class="form-control" name="startDate" id="startDate">
                        </div>
                        <div class="form-group">
                            <label>종료일</label>
                            <input type="date" class="form-control" name="endDate" id="endDate">
                        </div>
                    </div>

                </div>
            </div>

            <div class="d-flex mt-3" style="gap: 10px;">
                <button type="submit" class="btn btn-primary flex-fill">수정 완료</button>
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