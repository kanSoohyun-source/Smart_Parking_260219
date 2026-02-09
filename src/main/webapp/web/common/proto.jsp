<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css">

    <!-- Sidebar 전용 CSS (최소) -->
    <link rel="stylesheet" href="sidebar.css">
    <style>
        /* 전체 레이아웃 */
        #wrapper {
            display: flex;
            width: 100%;
        }

        /* 사이드바 */
        #sidebar-wrapper {
            min-height: 100vh;
            width: 250px;
            margin-left: -250px;   /* 기본: 숨김 */
            transition: margin 0.25s ease-out;
            background-color: #f8f9fa;
            border-right: 1px solid #dee2e6;
        }

        /* 토글 시 사이드바 표시 */
        #wrapper.toggled #sidebar-wrapper {
            margin-left: 0;
        }

        /* 페이지 콘텐츠 */
        #page-content-wrapper {
            width: 100%;
            padding: 20px;
        }

        /* 사이드바 메뉴 */
        .list-group {
            width: 250px;
        }

        .list-group-item {
            border: none;
            padding: 15px 20px;
        }

        /* 버튼 글자색 */
        #sidebarToggle {
            color: slategray;
        }

    </style>
</head>
<body>
    <div id="wrapper" class="toggled">
        <!-- 메뉴 -->
        <jsp:include page="menu.jsp"/>
        <!-- 본문 내용 -->
        <div id="page-content-wrapper">
            <button id="sidebarToggle" class="btn btn-light">☰</button>
            <h1>주차 현황</h1>
        </div>
    </div>

    <!-- 토글 사용하려면 필요한 내용 -->
    <script>
        document.getElementById("sidebarToggle").addEventListener("click", function () {
            document.getElementById("wrapper").classList.toggle("toggled");
        });
    </script>
</body>
</html>
