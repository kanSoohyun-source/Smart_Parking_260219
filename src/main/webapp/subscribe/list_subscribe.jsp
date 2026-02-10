<%@ page import="java.util.List" %>
<%@ page import="org.example.smart_parking_260219.dto.MemberDTO" %>
<%@ page import="org.example.smart_parking_260219.dto.SubscribeDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
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
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
  <div id="memberList" class="page">
    <!-- 헤더 영역 -->
    <div class="page-header">
      <h2>월정액 회원 목록</h2>
        <a href='../member/member_search.jsp' class="btn-detail">조회</a>
    </div>
  </div>

  <div class="table-container">
    <table>
      <thead>
      <tr>
        <th>번호</th>
        <th>차량 번호</th>
        <th>시작 기간</th>
        <th>종료 기간</th>
        <th>월정액 여부</th>
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
      %>
      <!-- 기존 tr 태그의 style 속성 완전 제거 -->
      <tr class="clickable-row" onclick="location.href='/member/member_detail?carNum=<%= subscribeDTO.getCarNum() %>'">
        <td><%= displayNo-- %>
        </td>
        <td><%= subscribeDTO.getCarNum() %>
        </td>
        <td><%= subscribeDTO.getStartDate() %>
        </td>
        <td><%= subscribeDTO.getEndDate() %>
        </td>
        <td><%= (subscribeDTO.isStatus()) ? "가입중" : "미가입" %>
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
<script>
  // DOM 로드 후 실행
  document.addEventListener('DOMContentLoaded', function() {
    const rows = document.querySelectorAll('.clickable-row');

    rows.forEach(row => {
      // 마우스 올릴 때
      row.addEventListener('mouseenter', function() {
        this.style.backgroundColor = '#e3f2fd';
      });

      // 마우스 벗어날 때
      row.addEventListener('mouseleave', function() {
        this.style.backgroundColor = '';
      });
    });
  });
</script>
<style>

  /* 비활성화된 버튼 스타일 */
  button:disabled {
    background-color: #ddd;
    cursor: not-allowed;
    color: #999;
  }

  button:disabled:hover {
    background-color: #ddd;
  }

  /* 조회 버튼 스타일 */
  .btn-detail {
    padding: 10px 20px;
    font-size: 14px;
    background-color: #3498db;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
  }

  .btn-detail:hover {
    background-color: #2980b9;
  }

  /* 비활성화된 버튼 스타일 */
  button:disabled {
    background-color: #ddd;
    cursor: not-allowed;
    color: #999;
  }

  button:disabled:hover {
    background-color: #ddd;
  }
</style>
</body>
</html>

