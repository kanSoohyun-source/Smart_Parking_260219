<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.smart_parking_260219.dto.ManagerDTO" %>

<html>
<head>
    <title>관리자 목록 - 스마트 파킹 시스템</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
    <style>
        /* 메인 컨텐츠 영역 정렬 */
        .main-content {
            padding: 40px;
            background-color: #f4f7f6;
            min-height: 100vh;
        }

        /* 테이블 디자인 수정 핵심 */
        .manager-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }

        /* 분류(Header) 부분 강조 */
        .manager-table thead th {
            background-color: #2c3e50; /* 진한 네이비톤으로 분류 명확화 */
            color: #ffffff;            /* 글자는 흰색으로 대비 */
            padding: 15px;
            font-size: 1.1em;
            border-bottom: 3px solid #1a252f;
            text-align: center;
        }

        .manager-table tbody td {
            padding: 12px;
            border-bottom: 1px solid #eee;
            text-align: center;
            color: #333;
        }

        /* 행 마우스 오버 효과 */
        .manager-table tbody tr:hover {
            background-color: #f8f9fa;
        }

        .btn-add:hover { background: #1abc9c; }
        .btn-add { float: right; padding: 8px 15px; background: #2c3e50; color: white; text-decoration: none; border-radius: 4px; }
    </style>
</head>
<body>
<%@ include file="/main/menu.jsp" %>

<div class="main-content">
    <div id="entry" class="page">
        <div style="overflow: hidden;">
            <h2 style="display: inline-block;">관리자 계정 목록</h2>

<%--            <a href="${pageContext.request.contextPath}/mgr/add" class="btn-add">신규 관리자 추가</a>--%>

        </div>

        <div class="form-group">
            <table class="manager-table">
                <thead>
                <tr>
                    <th>번호</th>
                    <th>아이디</th>
                    <th>이름</th>
                    <th>이메일</th>
                    <th>상태</th>
                </tr>
                </thead>
                <tbody>
                <%
                    // 컨트롤러에서 보낸 "managerList"를 가져옴
                    List<ManagerDTO> managerList = (List<ManagerDTO>) request.getAttribute("managerList");

                    if (managerList != null && !managerList.isEmpty()) {
                        for (ManagerDTO mgr : managerList) {
                %>
                <tr>
                    <td><%= mgr.getManagerNo() %></td>
                    <td><%= mgr.getManagerId() %></td>
                    <td>
                        <a href="${pageContext.request.contextPath}/mgr/view?id=<%= mgr.getManagerId() %>" style="color: #007bff; font-weight: bold;">
                            <%= mgr.getManagerName() %>
                        </a>
                    </td>
                    <td><%= mgr.getEmail() %></td>
                    <td>
                            <span class="<%= mgr.isActive() ? "status-active" : "status-inactive" %>">
                                <%= mgr.isActive() ? "활성" : "비활성" %>
                            </span>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="5" style="padding: 30px; color: #999;">등록된 관리자가 없습니다.</td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/JS/menu.js"></script>
<script src="${pageContext.request.contextPath}/JS/function.js"></script>
</body>
</html>