<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav>
    <h1>주차장 관리 시스템</h1>
    <ul id="navMenu">
        <li><a href="../WEB-INF/view/dashboard/dashboard.jsp">주차 현황</a></li>
        <li><a href="../WEB-INF/view/entry/entry.jsp">입차</a></li>
        <li><a href="../WEB-INF/view/exit/exit.jsp">출차</a></li>
        <li><a href="../member/add_member.jsp">회원 등록</a></li>
        <li><a href="../member/member_list.jsp">회원 목록</a></li>
        <li><a href="../member/member_search.jsp">회원 조회</a></li>
        <li><a href="../pricing/pricing.jsp">요금 부과 정책</a></li>
        <li><a href="../statistics/statistics.jsp">매출 통계</a></li>
        <li><a href="../logout/logout.jsp" onclick="confirmLogout_01();">관리자 추가</a></li>
        <li><a href="../logout/logout.jsp" onclick="confirmLogout();">로그아웃</a></li>
    </ul>
</nav>

<script>
    // 관리자 추가 함수
    function confirmLogout_01() {
        // confirm 창은 '확인'을 누르면 true, '취소'를 누르면 false를 반환합니다.
        if (confirm("관리자 추가 하시겠습니까?")) {
            // '확인' 클릭 시 로그아웃 처리 페이지로 이동
            location.href = "/login/login.jsp";
        } else {
            // '취소' 클릭 시 아무런 동작도 하지 않고 현재 페이지에 머묾
            return false;
        }
    }

    // 로그아웃 함수
    function confirmLogout() {
        // confirm 창은 '확인'을 누르면 true, '취소'를 누르면 false를 반환합니다.
        if (confirm("로그아웃을 하시겠습니까?")) {
            // '확인' 클릭 시 로그아웃 처리 페이지로 이동
            location.href = "/login/login.jsp";
        } else {
            // '취소' 클릭 시 아무런 동작도 하지 않고 현재 페이지에 머묾
            return false;
        }
    }
</script>