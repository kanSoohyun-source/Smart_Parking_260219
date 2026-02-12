<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav>
    <h1>주차장 관리 시스템</h1>

    <%
        // 세션에서 로그인 정보 가져오기
        Object loginManager = session.getAttribute("loginManager");
        String mName = (String) session.getAttribute("managerName");
        String mRole = (String) session.getAttribute("managerRole");  // ADMIN or NORMAL

        if (loginManager != null && mName != null) {
    %>
    <div style="padding: 15px; margin-bottom: 1px; background-color: #1a252f; border-radius: 4px; text-align: center;">
        <span style="font-size: 0.85em; color: #f39c12; margin-left: 5px;">[👑 최고관리자 👑]</span>
        <br>관리자 : <span style="font-weight: bold; color: #e0e0e0;"><%= mName %></span> 님
        <% if ("ADMIN".equals(mRole)) { %>
        <% } %>
    </div>
    <div id="liveClock"
         style="font-size: 0.9em; background-color: #1a252f; color: #95a5a6; letter-spacing: 1px; text-align: center">
        0000-00-00 00:00:00
    </div>
    <%
    } else {
    %>
    <div style="padding: 10px; margin-bottom: 10px; color: #ff0000; text-align: center; font-size: 0.85em;">
        로그인 정보가 없습니다.
    </div>
    <%
        }
    %>

    <ul id="navMenu">
        <%-- ADMIN 권한인 경우에만 관리자 메뉴 표시 --%>
        <% if ("ADMIN".equals(mRole)) { %>
        <li class="dropdown">
            <a href="javascript:void(0);" class="dropbtn" onclick="toggleDropdown()">관리자 메뉴 ▼</a>
            <div id="adminSubMenu" class="dropdown-content">
                <a href="${pageContext.request.contextPath}/mgr/add" onclick="return confirmAddManager();">관리자 추가</a>
                <a href="${pageContext.request.contextPath}/mgr/view">관리자 수정</a>
            </div>
        </li>
        <% } %>
        <li><a href="${pageContext.request.contextPath}/dashboard">주차 현황</a></li>
        <li><a href="../entry/entry.jsp">입차</a></li>
        <li><a href="../exit/exit.jsp">출차</a></li>
        <li><a href="../member/add_member.jsp">회원 등록</a></li>
        <li><a href="../member/member_list.jsp">회원 목록</a></li>
        <li><a href="../member/member_search.jsp">회원 조회</a></li>
        <li><a href="../pricing/pricing.jsp">요금 부과 정책</a></li>
        <li><a href="../statistics/statistics.jsp">매출 통계</a></li>

        <li><a href="${pageContext.request.contextPath}/logout" onclick="return confirmLogout();">로그아웃</a></li>
    </ul>
</nav>

<script>
    <%-- 실시간 시계 --%>

    function updateClock() {
        const now = new Date();

        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const day = String(now.getDate()).padStart(2, '0');

        const hours = String(now.getHours()).padStart(2, '0');
        const minutes = String(now.getMinutes()).padStart(2, '0');
        const seconds = String(now.getSeconds()).padStart(2, '0');

        const timeString = year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;

        const clockElement = document.getElementById("liveClock");
        if (clockElement) {
            clockElement.innerText = timeString;
        }
    }

    setInterval(updateClock, 1000);
    window.onload = updateClock;
</script>

<script>
    // 관리자 추가 확인
    function confirmAddManager() {
        return confirm("관리자 추가 페이지로 이동하시겠습니까?");
    }

    // 로그아웃 확인
    function confirmLogout() {
        return confirm("로그아웃을 하시겠습니까?");
    }

    // 드롭다운 토글
    function toggleDropdown() {
        const dropdown = document.getElementById("adminSubMenu");
        dropdown.classList.toggle("show");
    }

    // 메뉴 외부 클릭 시 드롭다운 닫기
    window.onclick = function (event) {
        if (!event.target.matches('.dropbtn')) {
            const dropdowns = document.getElementsByClassName("dropdown-content");
            for (let i = 0; i < dropdowns.length; i++) {
                const openDropdown = dropdowns[i];
                if (openDropdown.classList.contains('show')) {
                    openDropdown.classList.remove('show');
                }
            }
        }
    }
</script>

<style>
    /* 드롭다운 컨테이너 */
    .dropdown-content {
        display: none;
        background-color: #2c3e50;
        padding-left: 15px;
    }

    .dropdown-content a {
        font-size: 0.9em;
        padding: 10px;
        color: #bdc3c7;
        text-decoration: none;
        display: block;
    }

    .dropdown-content a:hover {
        color: #ffffff;
        background-color: #34495e;
    }

    /* 토글 시 보여줄 클래스 */
    .show {
        display: block;
    }
</style>
