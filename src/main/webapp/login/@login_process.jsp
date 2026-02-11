<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  // input의 name 속성값으로 데이터를 가져옵니다.
  String id = request.getParameter("id");
  String pw = request.getParameter("pw");

  if ("admin".equals(id) && "1234".equals(pw)) {
    // 세션에 로그인 정보 저장
    session.setAttribute("isLoggedIn", "true");
    // 로그인 성공 시 메인 화면(예: dashboard.jsp)으로 이동
    response.sendRedirect("../dashboard/dashboard.jsp");
  } else {
    // 로그인 실패 시 알림창을 띄우고 이전 페이지로 이동
    out.println("<script>");
    out.println("alert('아이디 또는 비밀번호가 틀립니다.');");
    out.println("history.back();");
    out.println("</script>");
  }
%>