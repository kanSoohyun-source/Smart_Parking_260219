<%@ page import="java.util.List" %>
<%@ page import="org.example.smart_parking_260219.dto.SubscribeDTO" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  Map<String, String> members = (Map<String, String>) request.getAttribute("members");
  List<SubscribeDTO> dtoList = (List<SubscribeDTO>) request.getAttribute("dtoList");
  Integer currentPage = (Integer) request.getAttribute("currentPage");
  Integer totalPages = (Integer) request.getAttribute("totalPages");
  Integer totalItems = (Integer) request.getAttribute("totalItems");
  Integer startNo = (Integer) request.getAttribute("startNo");

  // null 체크
  if (dtoList == null) dtoList = new java.util.ArrayList<>();
  if (currentPage == null) currentPage = 1;
  if (totalPages == null) totalPages = 1;
  if (totalItems == null) totalItems = 0;
  if (startNo == null) startNo = 0;

  int displayNo = startNo;

  int no = dtoList.size();
%>
<html>
<head>
  <title>월정액 회원 목록</title>
  <link rel="stylesheet" href="../CSS/style.css">
  <link rel="stylesheet" href="../CSS/member/add.css">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<%-- 성공시 모달창 확인 --%>
<%
  String successParam = request.getParameter("success");
  if ("add".equals(successParam)) {
%>
<div class="alert alert-primary alert-dismissible fade show" role="alert">
  <strong>등록 완료!</strong> 새로운 월정액 회원이 성공적으로 등록되었습니다.
  <button type="button" class="close" data-dismiss="alert" aria-label="Close">
    <span aria-hidden="true">&times;</span>
  </button>
</div>
<% } %>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
  <div id="memberList" class="page">
    <!-- 헤더 영역 -->
    <div class="page-header">
      <h2>월정액 회원 목록</h2>
        <a href='/subscribe/subscribe_add' class="btn btn-detail">추가</a>
    </div>
    <div>
      <div class="text-right">
        <span class="badge badge-success">전체<%=totalItems%>건</span>
      </div>
    </div>
  </div>

  <div class="table-container">
    <table>
      <thead>
      <tr>
        <th>번호</th>
        <th>차량 번호</th>
        <th>회원 이름</th>
        <th>구독 시작일</th>
        <th>구독 종료일</th>
        <th>최근 변경일</th>
      </tr>
      </thead>
      <tbody>
      <%
        if (dtoList.isEmpty()) {
      %>
      <tr>
        <td colspan="7" style="text-align: center;">등록된 회원이 없습니다.</td>
      </tr>
      <%
      } else {
        for (SubscribeDTO subscribeDTO : dtoList) {
          // 회원 정보에서 회원 이름 가져오기
          String memberName = members.get(subscribeDTO.getCarNum());
          if (memberName == null) memberName = "-";
      %>
      <!-- 기존 tr 태그의 style 속성 완전 제거 -->
      <tr class="clickable-row" onclick="location.href='/member/member_detail?carNum=<%= subscribeDTO.getCarNum() %>'">
        <td><%= displayNo-- %>
        </td>
        <td><%= subscribeDTO.getCarNum() %>
        </td>
        <td><%= memberName %>
        </td>
        <td><%= subscribeDTO.getStartDate() %>
        </td>
        <td><%= subscribeDTO.getEndDate() %>
        </td>
        <td><%= subscribeDTO.getLastUpdate().toLocalDate() %>
        </td>
      </tr>
      <%
          }
        }
      %>
      </tbody>
    </table>
  </div>
  <!-- 페이징 버튼 -->
  <div class="pagination">
    <%
      if (currentPage > 1) {
    %>
    <a href="?page=<%= currentPage - 1 %>">
      <button>◀ 이전</button>
    </a>
    <%
    } else {
    %>
    <button disabled>◀ 이전</button>
    <%
      }
    %>

    <span id="pageInfo"><%= currentPage %> / <%= totalPages %></span>

    <%
      if (currentPage < totalPages) {
    %>
    <a href="?page=<%= currentPage + 1 %>">
      <button>다음 ▶</button>
    </a>
    <%
    } else {
    %>
    <button disabled>다음 ▶</button>
    <%
      }
    %>
  </div>
</div>
<script src="../JS/menu.js"></script>
<script src="../JS/function.js"></script>
<script src="../JS/member/add.js"></script>
</body>
</html>

