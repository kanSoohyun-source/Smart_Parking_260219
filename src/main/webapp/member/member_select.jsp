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
        <p>차량 번호와 소유주 전화번호를 함께</p>
        <p>올바른 관리자가 선택할 수 있도록 함</p>
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
<script>
  function selectMember(carNum) {
    location.href = '/member/member_detail?carNum=' + encodeURIComponent(carNum);
  }
</script>

<style>
  .select-container {
    max-width: 600px;
    margin: 0 auto;
  }

  .notice-box {
    background-color: #e8f4f8;
    border: 1px solid #b3d9e6;
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 30px;
    text-align: center;
  }

  .notice-box p {
    margin: 5px 0;
    color: #2c3e50;
    font-size: 14px;
    line-height: 1.6;
  }

  .member-select-list {
    display: flex;
    flex-direction: column;
    gap: 15px;
  }

  .member-select-item {
    background: white;
    border: 2px solid #ddd;
    border-radius: 8px;
    padding: 20px;
    cursor: pointer;
    transition: all 0.3s;
  }

  .member-select-item:first-child {
    border-color: #3498db;
    background-color: #f0f8ff;
  }

  .member-select-item:hover {
    border-color: #3498db;
    background-color: #f8f9fa;
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(0,0,0,0.1);
  }

  .select-info {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
    color: #7f8c8d;
    font-size: 13px;
  }

  .select-value {
    display: flex;
    justify-content: space-between;
    font-size: 16px;
    font-weight: 600;
    color: #2c3e50;
  }

  .car-num, .car-num-value {
    flex: 1;
    text-align: left;
  }

  .phone-num, .phone-num-value {
    flex: 1;
    text-align: right;
  }
</style>
</body>
</html>
