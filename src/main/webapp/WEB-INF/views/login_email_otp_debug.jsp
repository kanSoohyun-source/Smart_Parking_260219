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
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
        .debug-info {
            background: #e7f3ff;
            border: 1px solid #b3d9ff;
            padding: 10px;
            margin-top: 10px;
            border-radius: 5px;
            font-size: 12px;
            font-family: monospace;
            max-height: 150px;
            overflow-y: auto;
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
            <div class="field-hint">등록된 이메일 주소를 입력하세요</div>
            <div class="field-error" id="emailError"></div>
        </div>

        <!-- Step 2: OTP 입력 (이메일 발송 후 표시) -->
        <div id="otpGroup">
            <div class="form-group">
                <label for="otp">인증번호</label>
                <input type="text" id="otp" name="otp" maxlength="6" placeholder="6자리 인증번호" autocomplete="off">
                <div class="field-hint">콘솔에 출력된 6자리 OTP를 입력하세요 (테스트용)</div>
                <div class="field-error" id="otpError"></div>
            </div>

            <button type="submit" class="btn btn-primary" id="submitBtn">로그인</button>
        </div>

        <button type="button" class="btn btn-secondary" id="cancelBtn">취소</button>
    </form>

    <!-- 디버깅 정보 표시 영역 -->
    <div class="debug-info" id="debugInfo" style="display: none;">
        <strong>디버깅 로그:</strong><br>
        <div id="debugLog"></div>
    </div>
</div>

<script>
    const emailInput = document.getElementById('email');
    const sendOtpBtn = document.getElementById('sendOtpBtn');
    const otpGroup = document.getElementById('otpGroup');
    const otpInput = document.getElementById('otp');
    const submitBtn = document.getElementById('submitBtn');
    const cancelBtn = document.getElementById('cancelBtn');
    const otpForm = document.getElementById('otpForm');
    const debugInfo = document.getElementById('debugInfo');
    const debugLog = document.getElementById('debugLog');

    let isEmailVerified = false;

    // 디버그 로그 함수
    function addDebugLog(message) {
        console.log(message);
        debugInfo.style.display = 'block';
        const logEntry = document.createElement('div');
        logEntry.textContent = new Date().toLocaleTimeString() + ': ' + message;
        debugLog.appendChild(logEntry);
        debugLog.scrollTop = debugLog.scrollHeight;
    }

    // 페이지 로드 시 디버깅 정보
    window.addEventListener('load', function() {
        addDebugLog('페이지 로드 완료');
        addDebugLog('Context Path: ${pageContext.request.contextPath}');
        addDebugLog('Form Action: ' + otpForm.action);
        addDebugLog('OTP 요청 URL: ${pageContext.request.contextPath}/login/sendLoginOtp');
    });

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
        addDebugLog('========================================');
        addDebugLog('인증요청 버튼 클릭');
        
        const email = emailInput.value.trim();
        const emailPattern = /^[A-Za-z0-9+_.-]+@(.+)$/;

        addDebugLog('입력된 이메일: ' + email);

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
        
        addDebugLog('요청 URL: ' + url);
        addDebugLog('요청 Body: ' + body);
        addDebugLog('Fetch 시작...');

        // OTP 발송 요청
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: body
        })
        .then(response => {
            addDebugLog('응답 수신 완료');
            addDebugLog('응답 상태: ' + response.status + ' ' + response.statusText);
            addDebugLog('응답 헤더 Content-Type: ' + response.headers.get('Content-Type'));
            
            if (!response.ok) {
                throw new Error('HTTP error! status: ' + response.status);
            }
            
            return response.text(); // 먼저 text로 받아서 확인
        })
        .then(text => {
            addDebugLog('응답 본문(Text): ' + text);
            
            try {
                const data = JSON.parse(text);
                addDebugLog('JSON 파싱 성공');
                addDebugLog('success: ' + data.success);
                addDebugLog('message: ' + data.message);
                
                if (data.success) {
                    // 성공 메시지 표시
                    const errorMessage = document.getElementById('errorMessage');
                    if (errorMessage) {
                        errorMessage.className = 'success-message';
                        errorMessage.textContent = email + '로 인증번호를 발송했습니다. (콘솔 확인)';
                    } else {
                        const successDiv = document.createElement('div');
                        successDiv.className = 'success-message';
                        successDiv.textContent = email + '로 인증번호를 발송했습니다. (콘솔 확인)';
                        otpForm.insertBefore(successDiv, otpForm.firstChild);
                    }
                    
                    emailInput.readOnly = true;
                    otpGroup.style.display = 'block';
                    otpInput.focus();
                    isEmailVerified = true;
                    
                    addDebugLog('✅ OTP 발송 성공!');
                    addDebugLog('IntelliJ 콘솔에서 OTP를 확인하세요!');
                    alert('✅ OTP가 발송되었습니다!\n\n📋 IntelliJ 콘솔 창에서\n"테스트용 OTP: ######" 를 확인하고\n해당 번호를 입력하세요.');
                } else {
                    addDebugLog('❌ 발송 실패: ' + data.message);
                    alert('인증번호 발송 실패: ' + (data.message || '알 수 없는 오류'));
                }
            } catch (parseError) {
                addDebugLog('❌ JSON 파싱 실패: ' + parseError.message);
                addDebugLog('응답이 JSON이 아닙니다. HTML이거나 다른 형식일 수 있습니다.');
                alert('서버 응답 오류: JSON 파싱 실패\n콘솔을 확인하세요.');
            }
        })
        .catch(error => {
            addDebugLog('❌ Fetch 오류 발생');
            addDebugLog('오류 메시지: ' + error.message);
            addDebugLog('오류 타입: ' + error.name);
            console.error('전체 오류:', error);
            alert('인증번호 발송 중 오류가 발생했습니다.\n\n오류: ' + error.message + '\n\n콘솔과 디버그 로그를 확인하세요.');
        })
        .finally(() => {
            sendOtpBtn.disabled = false;
            sendOtpBtn.textContent = isEmailVerified ? '재발송' : '인증요청';
            addDebugLog('요청 완료');
            addDebugLog('========================================');
        });
    });

    // OTP 입력 시 숫자만 허용
    otpInput.addEventListener('input', function(e) {
        this.value = this.value.replace(/[^0-9]/g, '');
    });

    // 폼 제출
    otpForm.addEventListener('submit', function(e) {
        addDebugLog('OTP 폼 제출 시작');
        
        const email = emailInput.value.trim();
        const otp = otpInput.value.trim();
        
        addDebugLog('제출 - 이메일: ' + email);
        addDebugLog('제출 - OTP: ' + otp);

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
        
        addDebugLog('폼 제출 진행');
        return true;
    });

    // 취소 버튼
    cancelBtn.addEventListener('click', function() {
        addDebugLog('취소 버튼 클릭');
        if (confirm('로그인을 취소하시겠습니까?')) {
            window.location.href = '${pageContext.request.contextPath}/login';
        }
    });
</script>
</body>
</html>
