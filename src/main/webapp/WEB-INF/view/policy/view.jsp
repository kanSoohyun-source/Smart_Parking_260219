<%@ page import="org.example.smart_parking_260219.dto.FeePolicyDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 26. 1. 28.
  Time: 오후 9:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    FeePolicyDTO policy = (FeePolicyDTO) request.getAttribute("policy");
    String pageNum = request.getParameter("pageNum");
    String items = (request.getParameter("items") != null) ? request.getParameter("items") : "";
    String keyword = (request.getParameter("keyword") != null) ? request.getParameter("keyword") : "";
%>
<html>
<head>
    <title>요금 정책 상세 조회</title>
    <link rel="stylesheet" href="../CSS/style.css">
</head>
<style>
    .view-row {
        display: flex;             /* 가로 배치 */
        align-items: center;       /* 세로 중앙 정렬 */
        padding: 20px 0;
        border-bottom: 1px solid #f1f1f1;
    }
    .view-label {
        width: 180px;              /* 항목 이름의 너비를 넉넉하게 고정 */
        min-width: 180px;          /* 최소 너비 유지 */
        font-weight: bold;         /* 라벨 강조 */
        color: #555;
    }
    .button-group {
        display: flex;
        gap: 15px;               /* 버튼 사이 간격 */
        margin-top: 30px;
        margin-bottom: 20px;
    }
</style>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
  <!-- 제목 -->
    <div class="view-container">
        <h2 class="mb-4 text-center">요금 부과 정책 상세 정보</h2>
        <!-- 내용 -->
        <div class="view-row">
            <div class="view-label">무료 회차(분)</div>
            <div class="view-content"><%=policy.getGracePeriod()%>분</div>
        </div>
        <div class="view-row">
            <div class="view-label">기본 시간 / 요금</div>
            <div class="view-content"><%=policy.getDefaultTime()%> 분 / <%=policy.getDefaultFee()%>  원</div>
        </div>
        <div class="view-row">
            <div class="view-label">추가 시간 / 요금</div>
            <div class="view-content"><%=policy.getExtraTime()%>  분 / <%=policy.getExtraFee()%> 원</div>
        </div>
        <div class="view-row">
            <div class="view-label">월정액</div>
            <div class="view-content"><%=policy.getSubscribedFee()%> 원</div>
        </div>
        <div class="view-row">
            <div class="view-label">일일 최대 요금</div>
            <div class="view-content"><%=policy.getMaxDailyFee()%> 원</div>
        </div>
        <div class="view-row">
            <div class="view-label">경차 할인율</div>
            <div class="view-content"><%=policy.getLightDiscount()%> %</div>
        </div>
        <div class="view-row">
            <div class="view-label">장애인 할인율</div>
            <div class="view-content"><%=policy.getDisabledDiscount()%> %</div>
        </div>
        <div class="view-row">
            <div class="view-label">현재 상태</div>
            <div class="view-content">
                <span class="badge <%= policy.isActive() ? "badge-primary" : "badge-secondary" %>">
            <%= policy.isActive() ? "사용 중" : "미사용" %></span>
            </div>
        </div>
        <div class="button-group mt-4">
            <%-- 현재 미사용 중인 정책일 때만 적용 버튼 노출 --%>
            <% if (!policy.isActive()) { %>
            <button type="button" class="btn btn-success"
                    onclick="if(confirm('이 정책을 현재 주차 요금 정책으로 적용하시겠습니까?'))
                            location.href='./apply?id=<%= policy.getPolicyId()%>&pageNum=<%=pageNum%>&items=<%=items%>&keyword=<%=keyword%>'">
                정책 적용하기
            </button>
            <% } %>

            <%-- 목록 버튼은 항상 노출 --%>
            <button type="button" class="btn btn-primary"
                    onclick="location.href='/view/list?pageNum=<%=pageNum%>&items=<%=items%>&keyword=<%=keyword%>'">목록으로</button>
        </div>
    </div>
</div>
    <script src="../JS/menu.js"></script>
    <script src="../JS/function.js"></script>
</body>
</html>
