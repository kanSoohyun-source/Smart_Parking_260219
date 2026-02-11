<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Enumeration" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>세션 정보 확인</title>
    <style>
        body {
            font-family: 'Courier New', monospace;
            padding: 20px;
            background: #f5f5f5;
        }
        .info-box {
            background: white;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 15px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        h2 {
            color: #333;
            border-bottom: 2px solid #667eea;
            padding-bottom: 10px;
        }
        .key {
            color: #667eea;
            font-weight: bold;
        }
        .value {
            color: #333;
        }
        pre {
            background: #f8f9fa;
            padding: 10px;
            border-radius: 5px;
            overflow-x: auto;
        }
    </style>
</head>
<body>
<div class="info-box">
    <h2>📋 요청 정보</h2>
    <p><span class="key">Request URI:</span> <span class="value"><%= request.getRequestURI() %></span></p>
    <p><span class="key">Context Path:</span> <span class="value"><%= request.getContextPath() %></span></p>
    <p><span class="key">Servlet Path:</span> <span class="value"><%= request.getServletPath() %></span></p>
    <p><span class="key">Path Info:</span> <span class="value"><%= request.getPathInfo() %></span></p>
    <p><span class="key">Query String:</span> <span class="value"><%= request.getQueryString() %></span></p>
</div>

<div class="info-box">
    <h2>🔐 세션 정보</h2>
    <%
        HttpSession sess = request.getSession(false);
        if (sess != null) {
    %>
    <p><span class="key">Session ID:</span> <span class="value"><%= sess.getId() %></span></p>
    <p><span class="key">Creation Time:</span> <span class="value"><%= new java.util.Date(sess.getCreationTime()) %></span></p>
    <p><span class="key">Last Accessed:</span> <span class="value"><%= new java.util.Date(sess.getLastAccessedTime()) %></span></p>
    <p><span class="key">Max Inactive Interval:</span> <span class="value"><%= sess.getMaxInactiveInterval() %> 초</span></p>
    
    <h3>세션 속성:</h3>
    <pre><%
        Enumeration<String> attributeNames = sess.getAttributeNames();
        boolean hasAttributes = false;
        while (attributeNames.hasMoreElements()) {
            hasAttributes = true;
            String name = attributeNames.nextElement();
            Object value = sess.getAttribute(name);
            out.println(name + " = " + value);
        }
        if (!hasAttributes) {
            out.println("세션 속성이 없습니다.");
        }
    %></pre>
    
    <% 
        Object loginManager = sess.getAttribute("loginManager");
        if (loginManager != null) {
    %>
    <p style="color: green; font-weight: bold;">✅ 로그인 상태: 정상</p>
    <% } else { %>
    <p style="color: red; font-weight: bold;">❌ 로그인 상태: loginManager 속성이 없음</p>
    <% } %>
    
    <% } else { %>
    <p style="color: red; font-weight: bold;">❌ 세션이 존재하지 않습니다.</p>
    <% } %>
</div>

<div class="info-box">
    <h2>🔗 테스트 링크</h2>
    <p><a href="${pageContext.request.contextPath}/dashboard">대시보드로 이동</a></p>
    <p><a href="${pageContext.request.contextPath}/manager_add">관리자 추가로 이동</a></p>
    <p><a href="${pageContext.request.contextPath}/logout">로그아웃</a></p>
</div>

<div class="info-box">
    <p><a href="${pageContext.request.contextPath}/login">로그인 페이지로 돌아가기</a></p>
</div>
</body>
</html>
