<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.smart_parking_260219.dto.MemberDTO" %>
<%@ page import="org.example.smart_parking_260219.dto.SubscribeDTO" %>
<%
    MemberDTO member = (MemberDTO) request.getAttribute("member");
    SubscribeDTO subscribeDTO = (SubscribeDTO) request.getAttribute("subscribe");
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
</head>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
    <!-- Content -->
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

            <div class="form-group">
                <label>구독 시작일</label>
                <input type="text" value="<%= subscribeDTO != null && subscribeDTO.getStartDate() != null ? subscribeDTO.getStartDate() : "-" %>" readonly>
            </div>

            <div class="form-group">
                <label>구독 만료일</label>
                <input type="text" value="<%= subscribeDTO != null && subscribeDTO.getEndDate() != null ? subscribeDTO.getEndDate() : "-" %>" readonly>
            </div>

            <div class="detail-buttons">
                <button type="button" onclick="location.href='/member/modify?carNum=<%= member.getCarNum() %>'">수정</button>
                <button type="button" class="secondary" onclick="location.href='/member/member_list'">목록</button>
                <button type="button" class="danger" onclick="deleteMember('<%= member.getCarNum() %>')">삭제</button>
            </div>
        </div>
    </div>
</div>

<script src="../JS/menu.js"></script>
<script>
    function deleteMember(memberId) {
        if (confirm('정말 삭제하시겠습니까?')) {
            location.href = '/member/delete?id=' + memberId;
        }
    }
</script>
<style>
    /* readonly 입력 필드 스타일 */
    input[readonly] {
        background-color: #f8f9fa;
        cursor: default;
        color: #495057;
    }

    input[readonly]:focus {
        border-color: #ddd;
    }

    /* 라벨 스타일 조정 */
    .form-group label {
        color: #555;
        font-weight: 600;
    }
</style>
</body>
</html>