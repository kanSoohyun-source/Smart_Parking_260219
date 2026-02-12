<%@ page import="java.util.List" %>
<%@ page import="org.example.smart_parking_260219.dto.MemberDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  List<MemberDTO> matchedMembers = (List<MemberDTO>) request.getAttribute("matchedMembers");
  if (matchedMembers == null || matchedMembers.isEmpty()) {
    response.sendRedirect("/member/member_search.jsp");
    return;
  }
%>
<html>
<head>
  <title>회원 선택</title>
  <link rel="stylesheet" href="../CSS/style.css">
  <link rel="stylesheet"  href="../CSS/member/select.css">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
  <div id="memberSelect" class="page">
    <h2>회원 조회</h2>

    <div class="select-container">
      <div class="notice-box">
        <p>차량 번호 4자리가 동일한 차량이 있는 경우,</p>
        <p>다른 회원 정보와 함께 사용자가 선택 할수 있게 함</p>
      </div>

      <div class="member-select-list">
        <%
          for (MemberDTO member : matchedMembers) {
        %>
        <div class="member-select-item" onclick="selectMember('<%= member.getCarNum() %>')">
          <div class="select-info">
            <span class="car-num">차량 번호</span>
            <span class="phone-num">회원 전화번호</span>
          </div>
          <div class="select-value">
            <span class="car-num-value"><%= member.getCarNum() %></span>
            <span class="phone-num-value"><%= member.getPhone() %></span>
          </div>
        </div>
        <%
          }
        %>
      </div>
    </div>
  </div>
</div>

<script src="../JS/menu.js"></script>
<script src="../JS/member/select.js"></script>
</body>
</html>
