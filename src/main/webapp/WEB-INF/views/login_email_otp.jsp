<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
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
            cursor: not-allowed;
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
        .btn-primary:hover:not(:disabled) {
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
        .btn-secondary:hover:not(:disabled) {
            background: #5a6268;
        }
        .btn-secondary:disabled {
            background: #ccc;
            cursor: not-allowed;
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
        .success-message {
            background: #d4edda;
            color: #155724;
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
            border: 1px solid #c3e6cb;
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
            white-space: nowrap;
        }
        #otpGroup {
            display: none;
            margin-top: 20px;
        }
        .admin-badge {
            display: inline-block;
            background: #667eea;
            color: white;
            padding: 4px 12px;
            border-radius: 12px;
            font-size: 12px;
            margin-left: 8px;
        }
        .timer {
            font-size: 14px;
            color: #dc3545;
            font-weight: bold;
            margin-top: 5px;
        }
    </style>
</head>
<body>
<div class="auth-container">
    <h2>🔐 2차 인증 <span class="admin-badge">최고관리자</span></h2>
    <p class="subtitle">이메일 인증 후 OTP를 입력해주세요</p>

    <div class="info-box">
        🔒 보안을 위해 이메일 인증과 OTP 확인이 필요합니다.
    </div>

    <%-- 에러 메시지 표시 --%>
    <% String error = (String) request.getAttribute("error");
        if (error != null && !error.isEmpty()) { %>
    <div class="error-message" id="errorMessage">
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
            <div class="field-hint">데이터베이스에 등록된 이메일 주소를 입력하세요</div>
            <div class="field-error" id="emailError"></div>
        </div>

        <!-- Step 2: OTP 입력 (이메일 발송 후 표시) -->
        <div id="otpGroup">
            <div class="form-group">
                <label for="otp">인증번호</label>
                <input type="text" id="otp" name="otp" maxlength="6" placeholder="6자리 인증번호" autocomplete="off">
                <div class="field-hint">이메일로 전송된 6자리 인증번호를 입력하세요</div>
                <div class="timer" id="timer" style="display: none;">남은 시간: <span id="timeLeft">05:00</span></div>
                <div class="field-error" id="otpError"></div>
            </div>

            <button type="submit" class="btn btn-primary" id="submitBtn">로그인</button>
        </div>

        <button type="button" class="btn btn-secondary" id="cancelBtn">취소</button>
    </form>
</div>

<script>
    const emailInput = document.getElementById('email');
    const sendOtpBtn = document.getElementById('sendOtpBtn');
    const otpGroup = document.getElementById('otpGroup');
    const otpInput = document.getElementById('otp');
    const submitBtn = document.getElementById('submitBtn');
    const cancelBtn = document.getElementById('cancelBtn');
    const otpForm = document.getElementById('otpForm');
    const timerDiv = document.getElementById('timer');
    const timeLeftSpan = document.getElementById('timeLeft');

    let isEmailVerified = false;
    let timerInterval = null;

    // 타이머 시작 (5분)
    function startTimer() {
        let timeLeft = 300; // 5분 = 300초
        timerDiv.style.display = 'block';
        
        timerInterval = setInterval(function() {
            timeLeft--;
            
            const minutes = Math.floor(timeLeft / 60);
            const seconds = timeLeft % 60;
            timeLeftSpan.textContent = 
                String(minutes).padStart(2, '0') + ':' + String(seconds).padStart(2, '0');
            
            if (timeLeft <= 0) {
                clearInterval(timerInterval);
                alert('인증 시간이 만료되었습니다. 다시 인증번호를 요청해주세요.');
                resetForm();
            }
        }, 1000);
    }

    // 폼 초기화
    function resetForm() {
        clearInterval(timerInterval);
        timerDiv.style.display = 'none';
        otpGroup.style.display = 'none';
        emailInput.readOnly = false;
        otpInput.value = '';
        isEmailVerified = false;
        sendOtpBtn.textContent = '인증요청';
    }

    // 이메일 유효성 검사
    emailInput.addEventListener('blur', function() {
        const value = this.value.trim();
        const emailPattern = /^[A-Za-z0-9+_.-]+@(.+)$/;
        const errorDiv = document.getElementById('emailError');

        if (value.length === 0) {
            emailInput.classList.add('error');
            errorDiv.textContent = '이메일을 입력해주세요.';
            errorDiv.style.display = 'block';
            return false;
        } else if (!emailPattern.test(value)) {
            emailInput.classList.add('error');
            errorDiv.textContent = '올바른 이메일 형식이 아닙니다.';
            errorDiv.style.display = 'block';
            return false;
        } else {
            emailInput.classList.remove('error');
            errorDiv.style.display = 'none';
            return true;
        }
    });

    // 인증번호 발송 버튼
    sendOtpBtn.addEventListener('click', function() {
        const email = emailInput.value.trim();
        const emailPattern = /^[A-Za-z0-9+_.-]+@(.+)$/;

        if (!email) {
            alert('이메일을 입력해주세요.');
            emailInput.focus();
            return;
        }

        if (!emailPattern.test(email)) {
            alert('올바른 이메일 형식을 입력해주세요.');
            emailInput.focus();
            return;
        }

        sendOtpBtn.disabled = true;
        sendOtpBtn.textContent = '발송 중...';

        const url = '${pageContext.request.contextPath}/login/sendLoginOtp';
        const body = 'email=' + encodeURIComponent(email);

        // OTP 발송 요청
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: body
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('HTTP error! status: ' + response.status);
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    // 성공 메시지 표시
                    const errorMessage = document.getElementById('errorMessage');
                    if (errorMessage) {
                        errorMessage.className = 'success-message';
                        errorMessage.textContent = email + '로 인증번호를 발송했습니다. 이메일을 확인해주세요.';
                    } else {
                        const successDiv = document.createElement('div');
                        successDiv.className = 'success-message';
                        successDiv.textContent = email + '로 인증번호를 발송했습니다. 이메일을 확인해주세요.';
                        otpForm.insertBefore(successDiv, otpForm.firstChild);
                    }

                    emailInput.readOnly = true;
                    otpGroup.style.display = 'block';
                    otpInput.focus();
                    isEmailVerified = true;
                    
                    // 타이머 시작
                    startTimer();

                    alert('✅ 이메일로 인증번호가 발송되었습니다!\n\n' + email + '\n\n이메일함을 확인하고 6자리 인증번호를 입력해주세요.\n(스팸함도 확인해주세요)');
                } else {
                    alert('인증번호 발송 실패: ' + (data.message || '알 수 없는 오류'));
                }
            })
            .catch(error => {
                console.error('오류:', error);
                alert('인증번호 발송 중 오류가 발생했습니다.\n\n오류: ' + error.message);
            })
            .finally(() => {
                sendOtpBtn.disabled = false;
                sendOtpBtn.textContent = isEmailVerified ? '재발송' : '인증요청';
            });
    });

    // OTP 입력 시 숫자만 허용
    otpInput.addEventListener('input', function(e) {
        this.value = this.value.replace(/[^0-9]/g, '');
    });

    // 폼 제출
    otpForm.addEventListener('submit', function(e) {
        const email = emailInput.value.trim();
        const otp = otpInput.value.trim();

        if (!email) {
            e.preventDefault();
            alert('이메일을 입력해주세요.');
            emailInput.focus();
            return false;
        }

        if (!isEmailVerified) {
            e.preventDefault();
            alert('먼저 인증번호를 발송받아주세요.');
            return false;
        }

        if (!otp || otp.length !== 6) {
            e.preventDefault();
            alert('6자리 인증번호를 입력해주세요.');
            otpInput.focus();
            return false;
        }

        submitBtn.disabled = true;
        submitBtn.textContent = '로그인 중...';

        clearInterval(timerInterval);
        return true;
    });

    // 취소 버튼
    cancelBtn.addEventListener('click', function() {
        if (confirm('로그인을 취소하시겠습니까?')) {
            clearInterval(timerInterval);
            window.location.href = '${pageContext.request.contextPath}/login';
        }
    });
</script>
</body>
</html>
