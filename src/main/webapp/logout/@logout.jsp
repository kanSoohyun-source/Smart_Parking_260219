<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    session.invalidate();
    response.sendRedirect("/login/login.jsp");  // 로그아웃 -> 로그인 페이지 이동
%>
