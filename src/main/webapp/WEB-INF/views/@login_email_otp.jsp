<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 1차 인증 통과 확인
    String tempManagerId = (String) session.getAttribute("tempManagerId");
    if (tempManagerId == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>2차 인증 - 이메일 OTP</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            background: #f5f5f5;
        }
        .auth-container {
            background: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
            width: 100%;
            max-width: 450px;
        }
        h2 {
            color: #333;
            margin-bottom: 10px;
            text-align: center;
            font-size: 24px;
        }
        .subtitle {
            color: #666;
            text-align: center;
            margin-bottom: 30px;
            font-size: 14px;
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
        input[type="email"],
        input[type="text"] {
            width: 100%;
            padding: 12px;
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
        input:read-only {
            background: #f5f5f5;
        }
        .field-error {
            font-size: 12px;
            color: #dc3545;
            margin-top: 4px;
            display: none;
        }
        .field-hint {
            font-size: 12px;
            color: #6c757d;
            margin-top: 4px;
        }
        .btn {
            width: 100%;
            padding: 12px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: background 0.3s;
        }
        .btn-primary {
            background: #667eea;
            color: white;
            margin-bottom: 10px;
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
        .btn-group {
            display: flex;
            gap: 10px;
            margin-top: 10px;
        }
        .btn-group .btn {
            flex: 1;
        }
        .error-message {
            background: #f8d7da;
            color: #721c24;
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
            border: 1px solid #f5c6cb;
            text-align: center;
        }
        .info-box {
            background: #fff3cd;
            color: #856404;
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
            border: 1px solid #ffeaa7;
            font-size: 14px;
        }
        .email-input-group {
            display: flex;
            gap: 8px;
            align-items: flex-start;
        }
        .email-input-group input {
            flex: 1;
        }
        .email-input-group button {
            width: 100px;
            padding: 12px 0;
            font-size: 14px;
            height: 46px;
        }
        #otpGroup {
            display: none;
            margin-top: 20px;
        }
    </style>
</head>
<body>
<div class="auth-container">
    <h2>2차 인증 (최고관리자)</h2>
    <p class="subtitle">이메일 인증 후 OTP를 입력해주세요</p>

    <div class="info-box">
        🔒 보안을 위해 이메일 인증과 OTP 확인이 필요합니다.
    </div>

    <%-- 에러 메시지 표시 --%>
    <% String error = (String) request.getAttribute("error");
        if (error != null) { %>
    <div class="error-message">
        <%= error %>
    </div>
    <% } %>

    <form id="otpForm" action="${pageContext.request.contextPath}/login/verifyEmailOtp" method="post">
        <!-- Step 1: 이메일 입력 및 인증번호 발송 -->
        <div class="form-group">
            <label for="email">이메일 주소</label>
            <div class="email-input-group">
                <input type="email" id="email" name="email" placeholder="example@email.com" required>
                <button type="button" id="sendOtpBtn" class="btn btn-secondary">인증요청</button>
            </div>
            <div class="field-hint">등록된 이메일 주소를 입력하세요</div>
            <div class="field-error" id="emailError"></div>
        </div>

        <!-- Step 2: OTP 입력 (이메일 발송 후 표시) -->
        <div id="otpGroup">
            <div class="form-group">
                <label for="otp">인증번호</label>
                <input type="text" id="otp" name="otp" maxlength="6" placeholder="6자리 인증번호">
                <div class="field-hint">이메일로 발송된 6자리 번호를 입력하세요</div>
                <div class="field-error" id="otpError"></div>
            </div>

            <button type="submit" class="btn btn-primary" id="submitBtn">로그인</button>
        </div>

        <div class="btn-group">
            <button type="button" class="btn btn-secondary" onclick="location.href='../../login'">취소</button>
        </div>
    </form>
</div>

<script>
    const emailInput = document.getElementById('email');
    const sendOtpBtn = document.getElementById('sendOtpBtn');
    const otpGroup = document.getElementById('otpGroup');
    const otpInput = document.getElementById('otp');
    const submitBtn = document.getElementById('submitBtn');

    let isEmailVerified = false;

    // 이메일 유효성 검사
    emailInput.addEventListener('blur', function() {
        const value = this.value.trim();
        const emailPattern = /^[A-Za-z0-9+_.-]+@(.+)$/;
        const errorDiv = document.getElementById('emailError');

        if (value.length === 0) {
            emailInput.classList.add('error');
            errorDiv.textContent = '이메일을 입력해주세요.';
            errorDiv.style.display = 'block';
        } else if (!emailPattern.test(value)) {
            emailInput.classList.add('error');
            errorDiv.textContent = '올바른 이메일 형식이 아닙니다.';
            errorDiv.style.display = 'block';
        } else {
            emailInput.classList.remove('error');
            errorDiv.style.display = 'none';
        }
    });

    // 인증번호 발송 버튼
    sendOtpBtn.addEventListener('click', function() {
        const email = emailInput.value.trim();
        const emailPattern = /^[A-Za-z0-9+_.-]+@(.+)$/;

        if (!email || !emailPattern.test(email)) {
            alert('올바른 이메일을 입력해주세요.');
            return;
        }

        sendOtpBtn.disabled = true;
        sendOtpBtn.textContent = '발송 중...';

        // OTP 발송 요청 (이메일 확인 + OTP 발송)
        fetch('${pageContext.request.contextPath}/login/sendLoginOtp', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'email=' + encodeURIComponent(email)
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert(email + '로 인증번호를 발송했습니다.');
                emailInput.readOnly = true;
                otpGroup.style.display = 'block';
                otpInput.focus();
            } else {
                alert('인증번호 발송 실패: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('인증번호 발송 중 오류가 발생했습니다.');
        })
        .finally(() => {
            sendOtpBtn.disabled = false;
            sendOtpBtn.textContent = '재발송';
        });
    });

    // 폼 제출
    document.getElementById('otpForm').addEventListener('submit', function(e) {
        const otp = otpInput.value.trim();

        if (otp.length !== 6) {
            e.preventDefault();
            alert('6자리 인증번호를 입력해주세요.');
            return false;
        }

        submitBtn.disabled = true;
        submitBtn.textContent = '로그인 중...';
    });
</script>
</body>
</html>
