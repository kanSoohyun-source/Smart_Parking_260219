<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 26. 1. 28.
  Time: 오후 9:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.smart_parking_260219.dto.FeePolicyDTO" %>
<%@ page import="org.example.smart_parking_260219.dto.PageResponseDto" %>
<%
    PageResponseDto pageResponseDto = (PageResponseDto) request.getAttribute("pageResponseDto");

    // 2. 에러 방지를 위한 null 체크 및 데이터 할당
    if (pageResponseDto == null) {
        out.print("<script>alert('데이터가 없습니다.'); history.back();</script>");
        return;
    }

    List<FeePolicyDTO> boardList = pageResponseDto.getBoardList();
    int totalCount = pageResponseDto.getTotalCount(); // 전체 게시글 개수

    int pageNum = pageResponseDto.getPageNum(); // 현재 페이지 번호
    int totalPage = pageResponseDto.getTotalPage(); // 전체 페이지 수

    // 3. 번호 역순 계산
    int n = totalCount - (5 * (pageResponseDto.getPageNum() - 1));

    String items = (request.getParameter("items") != null) ? request.getParameter("items") : "";
    String keyword = (request.getParameter("keyword") != null) ? request.getParameter("keyword") : "";

%>

<html>
<head>
    <title>요금 정책 변경 이력</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="/CSS/style.css">
</head>
<body>
<%@ include file="/main/menu.jsp" %>
<div class="main-content">

    <div id="entry" class="page">
        <h2>요금 정책 변경 이력</h2>

        <div class="text-right" style="margin-bottom: 10px;">
            <a href="/policy/add" class="btn btn-primary">신규 정책 등록</a>
            <span class="badge badge-success">전체 <%=pageResponseDto.getTotalCount()%>개</span>
        </div>

        <div class="row" style="padding-top: 30px">
            <table class="table">
                <thead>
                <tr>
                    <th>상태</th>
                    <th>기본시간/요금</th>
                    <th>추가시간/요금</th>
                    <th>유예시간</th>
                    <th>경차 할인율</th>
                    <th>장애인 할인율</th>
                    <th>일일최대</th>
                    <th>등록일</th>
                </tr>
                </thead>
                <tbody>

                <%
                    if (boardList != null && !boardList.isEmpty()) {
                        for (FeePolicyDTO dto : boardList) {
                            String rowStyle = dto.isActive() ? "background-color: #f0f8ff; font-weight: bold;" : "";
                %>
                <tr style="<%= rowStyle %>">
                    <td>
                        <% if (dto.isActive()) { %>
                        <span class="badge badge-success">적용중</span>
                        <% } else { %>
                        <span class="badge badge-secondary">이전정책</span>
                        <% } %>
                    </td>
                    <td><%= dto.getDefaultTime() %>분 / <%= dto.getDefaultFee() %>원</td>
                    <td><%= dto.getExtraTime() %>분 / <%= dto.getExtraFee() %>원</td>
                    <td><%= dto.getGracePeriod() %>분</td>
                    <td><%= dto.getLightDiscount()%>%</td>
                    <td><%= dto.getDisabledDiscount()%>%</td>

                    <td><%= dto.getMaxDailyFee() %>원</td>
                    <td>
                        <%
                            if (dto.getModifyDate() != null) {
                                // LocalDateTime을 문자열로 바꾼 후 "T"를 " "로 교체
                                String dateStr = dto.getModifyDate().toString().replace("T", " ");
                                out.print(dateStr);
                            }
                        %>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
                </tbody>
            </table>
            <!-- 페이지 목록 -->
            <div class="w-100 text-center" style="margin-top: 20px;">
                <%
                    int pagePerBlock = 5; // 한 블럭에 나올 페이지 수

                    // 전체 블럭 수
                    int totalBlock = (totalPage % pagePerBlock == 0) ? (totalPage / pagePerBlock) : (totalPage / pagePerBlock + 1);

                    // 현재 블럭 ex) 8page -> 2, 30page -> 6
                    int thisBlock = (pageNum - 1) / pagePerBlock + 1;

                    // 현재 블럭의 첫 페이지 3block -> 11, 6block -> 26
                    int firstPage = (thisBlock - 1) * pagePerBlock + 1;

                    // 현재 블럭의 마지막 페이지
                    int lastPage = thisBlock * pagePerBlock; // firstPage + pagePerBlock - 1

                    // 마지막 블럭의 마지막 페이지
//                lastPage = (lastPage > totalPage) ? totalPage : lastPage;
                    lastPage = Math.min(lastPage, totalPage);
                %>
                <%-- [이전] 버튼 --%>
                <% if (firstPage != 1) {%>
                <a href="/policy/list?pageNum=<%= (firstPage - 1) %>" class="btn btn-sm btn-outline-secondary">이전</a>
                <%
                    }
                %>
                <%-- 페이지 번호 숫자들 --%>
                <%
                    for (int i = firstPage; i <= lastPage; i++) {
                        // 현재 페이지인 경우 btn-primary(파란색), 아닌 경우 btn-outline-secondary(회색 테두리)
                        String activeClass = (pageNum == i) ? "btn-primary" : "btn-outline-secondary";
                %>
                <a href="/policy/list?pageNum=<%=i%>&items=<%=items%>&keyword=<%=keyword%>"
                   class="btn btn-sm <%= activeClass %>" style="margin: 0 2px;">
                    <%= i %>
                </a>
                <%
                    }
                %>
                <%
                    if (lastPage < totalPage) {
                %>
                <a href="/policy/list?pageNum=<%=(lastPage + 1)%>&items=<%=items%>&keyword=<%=keyword%>"
                class="btn btn-sm btn-outline-secondary">다음</a>
                <%
                    }
                %>
            </div>
        </div>
    </div>
</div>
<script src="../JS/menu.js"></script>
<script src="../JS/function.js"></script>
</body>
</html>