<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="../CSS/style.css">
    <link rel="stylesheet" href="../CSS/login_style.css">

</head>
<body>
<div class="main-content">
    <div class="login-container">
        <h2 class="login-header">로그인</h2>
        <form action="login_process.jsp" method="post">
            <div class="form-group">
                <label for="loginId">아이디를 입력해주세요</label>
                <input type="text" id="loginId" name="id" placeholder="아이디" required>
            </div>

            <div class="form-group">
                <label for="loginPw">비밀번호를 입력해주세요</label>
                <input type="password" id="loginPw" name="pw" placeholder="비밀번호" required>
            </div>

            <div class="login-buttons">
                <button type="submit" class="login-submit-btn">로그인</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
