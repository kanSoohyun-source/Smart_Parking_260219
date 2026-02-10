<%@ page import="java.util.List" %>
<%@ page import="org.example.smart_parking_260219.dto.MemberDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<MemberDTO> dtoList = (List<MemberDTO>) request.getAttribute("dtoList");
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
    <title>회원 목록</title>
    <link rel="stylesheet" href="../CSS/style.css">
</head>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
    <div id="memberList" class="page">
        <!-- 헤더 영역 -->
        <div class="page-header">
            <h2>회원 목록</h2>
            <button type="button" class="btn-detail" onclick="location.href='../member/member_search.jsp'">조회</button>
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
                <th>가입일</th>
                <th>월정액 가입 여부</th>
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
            <tr onclick="location.href='/member/member_detail?carNum=<%= memberDTO.getCarNum() %>'" style="cursor: pointer;">
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
                <td><%= memberDTO.getCreateDateOnly() %>
                </td>
                <td><%= (memberDTO.isSubscribed()) ? "가입중" : "미가입" %>
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
    <a href="../member/member_search.jsp">
        <button>회원 조회</button>
    </a>
</div>
</div>
<script src="../JS/menu.js"></script>
<script src="../JS/function.js"></script>
<style>
    /* 테이블 행 호버 효과 (style.css에 이미 있지만 강조) */
    table tbody tr {
        transition: background-color 0.2s;
    }

    table tbody tr:hover {
        background-color: #f8f9fa;
        cursor: pointer;
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
