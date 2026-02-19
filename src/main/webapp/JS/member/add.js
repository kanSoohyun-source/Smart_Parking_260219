// STEP1: 차량번호 조회 유효성
function validateCarNum() {
    const v = document.getElementById('carNum').value.trim();
    if (!v) { alert('차량번호를 입력해주세요.'); return false; }
    return true;
}

// STEP2: 신규 등록 폼 유효성
function validateRegister() {
    const name      = document.getElementById('name').value.trim();
    const phone     = document.getElementById('phone').value.trim();
    const startDate = document.getElementById('startDate').value;
    const endDate   = document.getElementById('endDate').value;

    if (!name)      { alert('이름을 입력해주세요.'); return false; }
    if (!phone)     { alert('전화번호를 입력해주세요.'); return false; }
    if (!startDate) { alert('구독 시작일을 입력해주세요.'); return false; }
    if (!endDate)   { alert('구독 종료일을 입력해주세요.'); return false; }
    if (new Date(startDate) >= new Date(endDate)) {
        alert('종료일은 시작일보다 이후여야 합니다.');
        return false;
    }
    return true;
}