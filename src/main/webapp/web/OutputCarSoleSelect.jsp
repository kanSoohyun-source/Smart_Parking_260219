<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>출차</title>
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

        /* 수정 가능 */
        /* 제목 + 입력칸 */
        div.inputCar {
            display: flex;  /* 수직 가운데 정렬 */
            align-items: center;
            justify-content: space-evenly;  /* 제목과 입력칸 배치 공간 정렬 */
            text-align: center;
            box-sizing: border-box;
            width: 1000px;
            min-height: 70px;
            background : gray;
            margin: auto auto 40px;
            font-size: 30px;
        }
        /* 입력칸 제목 */
        span {
            width: 450px;
            padding-bottom: inherit;
        }
        /* 입력칸 */
        #page-content-wrapper label {
            text-align: center;
            height: 40px;
            width : 550px;
            border: none;
            padding: 0;
        }

        /* 전체 입력칸 */
        form {
            margin: auto;
            width : 1000px;
        }
        .form-button {
            text-align: right;
        }
        /* 수정 불가능 */
    </style>
</head>
<body>
<div id="wrapper" class="toggled">
    <!-- 메뉴 -->
    <jsp:include page="./common/menu.jsp"/>
    <!-- 본문 내용 -->
    <div id="page-content-wrapper">
        <button id="sidebarToggle" class="btn btn-light">☰</button>
        <%-- 수정 가능 --%>
        <h1>출차</h1>
        <%
            String parking = request.getParameter("parking");
            String carNum = request.getParameter("carNum");
            String phoneNum = request.getParameter("phoneNum");
            String inputTime = request.getParameter("inputTime");
            String parkingTime = request.getParameter("parkingTime");
        %>
        <form action="Payment.jsp" method="post">
            <div class="inputCar">
                <span>주차 구역 : </span>
                <label><%=parking%></label>
            </div>
            <div class="inputCar">
                <span>차량 번호 : </span>
                <label><%=carNum%></label>
            </div>
            <div class="inputCar">
                <span>전화번호 : </span>
                <label><%=phoneNum%></label>
            </div>
            <div class="inputCar">
                <span>입차 시간 : </span>
                <label><%=inputTime%></label>
            </div>
            <div class="inputCar">
                <span>총 주차시간 : </span>
                <label><%=parkingTime%></label>
            </div>
            <div class="form-button">
                <input type="submit" class="btn btn-primary" value="정산">
            </div>
        </form>
        <%-- 수정 불가능 --%>
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
