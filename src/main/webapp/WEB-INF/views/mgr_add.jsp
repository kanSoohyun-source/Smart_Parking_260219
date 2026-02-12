<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>관리자 추가</title>
    <link rel="stylesheet" href="/CSS/style.css">

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        /*body {*/
        /*    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;*/
        /*    background: #f5f5f5;*/
        /*    display: flex;*/
        /*}*/
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f5f5;
            margin: 0;

            display: flex;
            justify-content: center; /* 가로 중앙 */
            align-items: center;     /* 세로 중앙 */
            min-height: 100vh;
        }

        /*.main-content {*/
        /*    flex: 1;*/
        /*    padding: 20px;*/
        /*    margin-left: 250px; !* 사이드바 너비만큼 *!*/
        /*}*/
        .main-content {
            width: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        /*.container {*/
        /*    max-width: 600px;*/
        /*    margin: 0 auto;*/
        /*    background: white;*/
        /*    padding: 30px;*/
        /*    border-radius: 10px;*/
        /*    box-shadow: 0 2px 10px rgba(0,0,0,0.1);*/
        /*}*/
        .container {
            width: 100%;
            max-width: 600px;
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            /* 기존 margin: 0 auto; 는 flex 환경에서 무시되므로 삭제해도 무방합니다. */
        }
        h2 {
            color: #333;
            margin-bottom: 30px;
            padding-bottom: 10px;
            border-bottom: 2px solid #667eea;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #555;
            font-weight: 500;
        }
        .required {
            color: #dc3545;
        }
        input[type="text"],
        input[type="password"],
        input[type="email"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            transition: border-color 0.3s;
        }
        input:focus {
            outline: none;
            border-color: #667eea;
        }
        input.error {
            border-color: #dc3545;
        }
        .field-hint {
            font-size: 12px;
            color: #6c757d;
            margin-top: 4px;
        }
        .field-error {
            font-size: 12px;
            color: #dc3545;
            margin-top: 4px;
            display: none;
        }
        .btn-group {
            display: flex;
            gap: 10px;
            margin-top: 30px;
        }
        .btn {
            flex: 1;
            padding: 12px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            transition: all 0.3s;
        }
        .btn-primary {
            background: #667eea;
            color: white;
        }
        .btn-primary:hover {
            background: #5568d3;
        }
        .btn-primary:disabled {
            background: #ccc;
            cursor: not-allowed;
        }
        .btn-secondary {
            background: #6c757d;
            color: white;
        }
        .btn-secondary:hover {
            background: #5a6268;
        }
        .message {
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
            text-align: center;
        }
        .success-message {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .error-message {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .password-strength {
            height: 4px;
            border-radius: 2px;
            margin-top: 8px;
            transition: all 0.3s;
        }
        .password-strength.weak {
            background: #dc3545;
            width: 33%;
        }
        .password-strength.medium {
            background: #ffc107;
            width: 66%;
        }
        .password-strength.strong {
            background: #28a745;
            width: 100%;
        }
    </style>
</head>
<body>
<!-- Navigation -->
<%@ include file="/main/menu.jsp" %>

<div class="main-content">
    <div class="container">
        <h2>관리자 추가</h2>

        <%-- 성공 메시지 표시 --%>
        <% String successMessage = (String) session.getAttribute("successMessage");
            if (successMessage != null) {
                session.removeAttribute("successMessage"); %>
        <div class="message success-message">
            <%= successMessage %>
        </div>
        <% } %>

        <%-- 에러 메시지 표시 --%>
        <% String error = (String) request.getAttribute("error");
            if (error != null) { %>
        <div class="message error-message">
            <%= error %>
        </div>
        <% } %>

        <form id="managerForm" action="${pageContext.request.contextPath}/mgr/add" method="post">
            <div class="form-group">
                <label for="id">아이디 <span class="required">*</span></label>
                <input type="text" id="id" name="id" 
                       value="<%= request.getAttribute("managerId") != null ? request.getAttribute("managerId") : "" %>" 
                       maxlength="50"
                       required>
                <div class="field-hint">영문, 숫자 조합 4-50자</div>
                <div class="field-error" id="idError"></div>
            </div>

            <div class="form-group">
                <label for="name">이름 <span class="required">*</span></label>
                <input type="text" id="name" name="name" 
                       value="<%= request.getAttribute("managerName") != null ? request.getAttribute("managerName") : "" %>" 
                       maxlength="20"
                       required>
                <div class="field-hint">최대 20자</div>
                <div class="field-error" id="nameError"></div>
            </div>

            <div class="form-group">
                <label for="pw">비밀번호 <span class="required">*</span></label>
                <input type="password" id="pw" name="pw" required>
                <div class="password-strength" id="passwordStrength"></div>
                <div class="field-hint">최소 4자 이상 (영문, 숫자, 특수문자 조합 권장)</div>
                <div class="field-error" id="pwError"></div>
            </div>

            <div class="form-group">
                <label for="passwordConfirm">비밀번호 확인 <span class="required">*</span></label>
                <input type="password" id="passwordConfirm" name="passwordConfirm" required>
                <div class="field-error" id="passwordConfirmError"></div>
            </div>

            <div class="form-group">
                <label for="email">이메일 <span class="required">*</span></label>
                <div style="display: flex; gap: 8px;">
                    <input type="email" id="email" name="email"
                           value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>"
                           maxlength="100"
                           placeholder="example@email.com" required
                           style="flex: 1; margin-bottom: 0;"> <button type="button" id="sendEmailBtn" class="btn btn-secondary"
                                                                       style="width: 100px; padding: 0; font-size: 14px; height: 45px;">인증요청</button>
                </div>
                <div class="field-hint">2차 인증에 사용됩니다</div>
                <div class="field-error" id="emailError"></div>

                <div id="emailAuthGroup" style="margin-top: 12px; display: none;">
                    <div style="display: flex; gap: 8px;">
                        <input type="text" id="authCode" placeholder="인증번호 6자리"
                               maxlength="6" style="flex: 1; margin-bottom: 0;">
                        <button type="button" id="verifyBtn" class="btn btn-primary"
                                style="width: 100px; padding: 0; font-size: 14px; height: 45px;">확인</button>
                    </div>
                    <div class="field-hint" id="authHint">이메일로 발송된 번호를 입력해주세요.</div>
                </div>
            </div>

            <div class="btn-group">
                <button type="submit" class="btn btn-primary" id="submitBtn">추가하기</button>
                <button type="button" class="btn btn-secondary"
                        onclick="location.href='${pageContext.request.contextPath}/dashboard'">
                    취소
                </button>
            </div>
        </form>
    </div>
</div>

<script>
    // DOM 요소
    const form = document.getElementById('managerForm');
    const idInput = document.getElementById('id');
    const nameInput = document.getElementById('name');
    const pwInput = document.getElementById('pw');
    const passwordConfirmInput = document.getElementById('passwordConfirm');
    const emailInput = document.getElementById('email');
    const submitBtn = document.getElementById('submitBtn');

    // 에러 메시지 표시 함수
    function showError(inputId, message) {
        const input = document.getElementById(inputId);
        const errorDiv = document.getElementById(inputId + 'Error');
        input.classList.add('error');
        errorDiv.textContent = message;
        errorDiv.style.display = 'block';
    }

    // 에러 메시지 숨김 함수
    function hideError(inputId) {
        const input = document.getElementById(inputId);
        const errorDiv = document.getElementById(inputId + 'Error');
        input.classList.remove('error');
        errorDiv.style.display = 'none';
    }

    // 아이디 유효성 검사
    idInput.addEventListener('blur', function() {
        const value = this.value.trim();
        if (value.length === 0) {
            showError('id', '아이디를 입력해주세요.');
        } else if (value.length < 4) {
            showError('id', '아이디는 최소 4자 이상이어야 합니다.');
        } else if (!/^[a-zA-Z0-9]+$/.test(value)) {
            showError('id', '아이디는 영문과 숫자만 사용 가능합니다.');
        } else {
            hideError('id');
        }
    });

    // 이름 유효성 검사
    nameInput.addEventListener('blur', function() {
        const value = this.value.trim();
        if (value.length === 0) {
            showError('name', '이름을 입력해주세요.');
        } else {
            hideError('name');
        }
    });

    // 비밀번호 강도 체크
    pwInput.addEventListener('input', function() {
        const password = this.value;
        const strengthBar = document.getElementById('passwordStrength');
        
        if (password.length === 0) {
            strengthBar.className = 'password-strength';
            return;
        }

        let strength = 0;
        if (password.length >= 4) strength++;
        if (password.length >= 8) strength++;
        if (/[a-z]/.test(password) && /[A-Z]/.test(password)) strength++;
        if (/[0-9]/.test(password)) strength++;
        if (/[^a-zA-Z0-9]/.test(password)) strength++;

        if (strength <= 2) {
            strengthBar.className = 'password-strength weak';
        } else if (strength <= 4) {
            strengthBar.className = 'password-strength medium';
        } else {
            strengthBar.className = 'password-strength strong';
        }
    });

    // 비밀번호 유효성 검사
    pwInput.addEventListener('blur', function() {
        const value = this.value;
        if (value.length === 0) {
            showError('pw', '비밀번호를 입력해주세요.');
        } else if (value.length < 4) {
            showError('pw', '비밀번호는 최소 4자 이상이어야 합니다.');
        } else {
            hideError('pw');
        }
    });

    // 비밀번호 확인 검사
    passwordConfirmInput.addEventListener('blur', function() {
        const password = pwInput.value;
        const confirmPassword = this.value;
        
        if (confirmPassword.length === 0) {
            showError('passwordConfirm', '비밀번호 확인을 입력해주세요.');
        } else if (password !== confirmPassword) {
            showError('passwordConfirm', '비밀번호가 일치하지 않습니다.');
        } else {
            hideError('passwordConfirm');
        }
    });

    // 이메일 유효성 검사
    emailInput.addEventListener('blur', function() {
        const value = this.value.trim();
        const emailPattern = /^[A-Za-z0-9+_.-]+@(.+)$/;
        
        if (value.length === 0) {
            showError('email', '이메일을 입력해주세요.');
        } else if (!emailPattern.test(value)) {
            showError('email', '올바른 이메일 형식이 아닙니다.');
        } else {
            hideError('email');
        }
    });

    // 폼 제출 시 전체 유효성 검사
    form.addEventListener('submit', function(e) {
        let isValid = true;

        // 아이디 검사
        if (idInput.value.trim().length < 4 || !/^[a-zA-Z0-9]+$/.test(idInput.value.trim())) {
            showError('id', '올바른 아이디를 입력해주세요.');
            isValid = false;
        }

        // 이름 검사
        if (nameInput.value.trim().length === 0) {
            showError('name', '이름을 입력해주세요.');
            isValid = false;
        }

        // 비밀번호 검사
        if (pwInput.value.length < 4) {
            showError('pw', '비밀번호는 최소 4자 이상이어야 합니다.');
            isValid = false;
        }

        // 비밀번호 확인 검사
        if (pwInput.value !== passwordConfirmInput.value) {
            showError('passwordConfirm', '비밀번호가 일치하지 않습니다.');
            isValid = false;
        }

        // 이메일 검사
        const emailPattern = /^[A-Za-z0-9+_.-]+@(.+)$/;
        if (!emailPattern.test(emailInput.value.trim())) {
            showError('email', '올바른 이메일 형식이 아닙니다.');
            isValid = false;
        }

        if (!isValid) {
            e.preventDefault();
            return false;
        }

        // 제출 중 버튼 비활성화
        submitBtn.disabled = true;
        submitBtn.textContent = '추가 중...';
    });

    // 입력 시 에러 메시지 자동 제거
    [idInput, nameInput, pwInput, passwordConfirmInput, emailInput].forEach(input => {
        input.addEventListener('input', function() {
            hideError(this.id);
        });
    });

    // 이메일 인증 요청 버튼 클릭 시
    document.getElementById('sendEmailBtn').addEventListener('click', function() {
        const email = document.getElementById('email').value;

        if(!email || !email.includes('@')) {
            alert('올바른 이메일을 입력해주세요.');
            return;
        }

        // 서버(Controller)에 이메일 발송 요청 Ajax 호출 필요

        alert(email + '로 인증번호를 발송했습니다.');
        // 인증번호 입력창 보이기
        document.getElementById('emailAuthGroup').style.display = 'block';
    });

    // 확인 버튼 클릭 시
    document.getElementById('verifyBtn').addEventListener('click', function() {
        const code = document.getElementById('authCode').value;

        if(code === "") {
            alert('인증번호를 입력해주세요.');
            return;
        }

        // 서버에서 보낸 번호와 맞는지 검증하는 로직
        alert('인증이 완료되었습니다.');
        document.getElementById('email').readOnly = true; // 이메일 수정 불가하게 고정
        this.disabled = true; // 확인 버튼 비활성화
    });
</script>
</body>
</html>
