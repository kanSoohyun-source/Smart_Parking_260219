<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원 등록</title>
    <link rel="stylesheet" href="../CSS/style.css">
</head>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>
<div class="main-content">
    <!-- Content -->
    <div id="register" class="page">
        <h2>회원 등록</h2>

        <form name="frmAddMember" action="/member/add_member" method="post" onsubmit="return validateForm()">

            <div class="form-group">
                <label for="carNum">차량 번호</label>
                <input type="text" name="carNum" id="carNum" placeholder="예: 123가4567" required>
            </div>

            <div class="form-group">
                <label for="carType">차량 종류</label>
                <select name="carType" id="carType" required>
                    <option value="1" selected>일반</option>
                    <option value="2">월정액</option>
                    <option value="3">경차</option>
                    <option value="4">장애인</option>
                </select>
            </div>


            <div class="form-group">
                <label for="name">회원 이름</label>
                <input type="text" name="name" id="name" placeholder="홍길동" required>
            </div>

            <div class="form-group">
                <label for="phone">연락처</label>
                <input type="tel" name="phone" id="phone" placeholder="010-1234-5678" required>
            </div>

            <div class="form-group">
                <label>월정액 가입 여부</label>
                <div class="radio-group">
                    <label style="display: inline-flex !important; flex-direction: row !important; align-items: center !important;">
                        <input type="radio" name="subscribed" value="true" style="margin-right: 5px;"> 가입
                    </label>
                    <label style="display: inline-flex !important; flex-direction: row !important; align-items: center !important;">
                        <input type="radio" name="subscribed" value="false" checked style="margin-right: 5px;"> 미가입
                    </label>
                </div>
            </div>

            <div class="button-group">
                <button type="submit">가입</button>
                <button type="button" onclick="location.href='/member/member_list'">목록</button>
            </div>
        </form>
    </div>
</div>
<script src="../JS/menu.js"></script>
<script src="../JS/function.js"></script>
<script>
    function validateForm() {
        const carNum = document.getElementById('carNum').value.trim();
        const name = document.getElementById('name').value.trim();
        const phone = document.getElementById('phone').value.trim();

        if (!carNum) {
            alert('차량 번호를 입력해주세요.');
            return false;
        }

        if (!name) {
            alert('이름을 입력해주세요.');
            return false;
        }

        if (!phone) {
            alert('전화번호를 입력해주세요.');
            return false;
        }

        // 전화번호 형식 검사 (선택)
        const phonePattern = /^\d{3}-\d{4}-\d{4}$/;
        if (!phonePattern.test(phone)) {
            alert('전화번호 형식이 올바르지 않습니다.\n(예: 010-1234-5678)');
            return false;
        }

        return true;
    }
</script>
<style>
    /* 라디오 버튼 그룹 */
    .radio-group {
        display: flex !important;
        flex-direction: row !important;
        gap: 20px;
    }

    .radio-group label {
        display: inline-flex !important;
        flex-direction: row !important;
        align-items: center !important;
        gap: 5px;
        cursor: pointer;
        white-space: nowrap;
    }

    .radio-group input[type="radio"] {
        cursor: pointer;
        margin: 0 5px 0 0 !important;
    }

    /* 버튼 그룹 */
    .button-group {
        display: flex;
        gap: 10px;
        margin-top: 20px;
    }

    .button-group button {
        flex: 1;
    }

    .button-group button[type="button"] {
        background-color: #6c757d;
    }

    .button-group button[type="button"]:hover {
        background-color: #5a6268;
    }

    /* Select 스타일 */
    select {
        width: 100%;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
        font-size: 14px;
    }

    select:focus {
        outline: none;
        border-color: #4a5568;
    }
</style>
</body>
</html>
