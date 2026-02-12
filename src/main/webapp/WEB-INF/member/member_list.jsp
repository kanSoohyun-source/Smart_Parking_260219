<%@ page import="java.util.List" %>
<%@ page import="org.example.smart_parking_260219.dto.MemberDTO" %>
<%@ page import="org.example.smart_parking_260219.dto.SubscribeDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<MemberDTO> dtoList = (List<MemberDTO>) request.getAttribute("dtoList");
    MemberDTO member = (MemberDTO) request.getAttribute("member");
    SubscribeDTO subscribeDTO = (SubscribeDTO) request.getAttribute("subscribe");

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

    String success = request.getParameter("success");
%>
<html>
<head>
    <title>회원 목록</title>
    <link rel="stylesheet" href="../CSS/style.css">
    <link rel="stylesheet" href="../CSS/member/list.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>

<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
    <div id="memberList" class="page">
        <!-- 헤더 영역 -->
        <% if ("modify".equals(success)) { %>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <strong>완료!</strong> 회원 정보가 수정되었습니다.
            <button type="button" class="close" data-dismiss="alert">
                <span>&times;</span>
            </button>
        </div>
        <% } %>

        <% if ("delete".equals(success)) { %>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <strong>완료!</strong> 회원이 삭제되었습니다.
            <button type="button" class="close" data-dismiss="alert">
                <span>&times;</span>
            </button>
        </div>
        <% } %>
        <div class="page-header">
            <h2>회원 목록</h2>
        </div>
        <div class="page-header">
        <a href='/member/member_search' class="btn btn-detail">조회</a>
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
                <th>차량 종류</th>
                <th>회원 이름</th>
                <th>회원 전화번호</th>
                <th>월정액 가입 여부</th>
                <th>구독 시작일</th>
                <th>구독 종료일</th>
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
                for (MemberDTO memberDTO : dtoList) {
            %>
            <!-- 멤버 조회로 넘어가는 태그 -->
            <tr class="clickable-row" onclick="location.href='/member/member_detail?carNum=<%= memberDTO.getCarNum() %>'">
                <td><%= displayNo-- %>
                </td>
                <td><%= memberDTO.getCarNum() %>
                </td>
                <td><%= memberDTO.getCarTypeName() %>
                </td>
                <td><%= memberDTO.getName() %>
                </td>
                <td><%= memberDTO.getPhone() %>
                </td>
                <td><%= (memberDTO.isSubscribed()) ? "가입중" : "미가입" %>
                </td>
                <td><%= memberDTO.getSubscribeStartDate() != null ? memberDTO.getSubscribeStartDate() : "-" %></td>
                <td><%= memberDTO.getSubscribeEndDate() != null ? memberDTO.getSubscribeEndDate() : "-" %></td>
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
</body>
</html>
