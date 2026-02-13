function validateForm() {
    const name = document.getElementById('name').value.trim();
    const phone = document.getElementById('phone').value.trim();
    const action = document.getElementById('subscribeAction').value;

    if (!name) { alert('이름을 입력해주세요.'); return false; }

    const phonePattern = /^[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}$/;
    if (!phonePattern.test(phone)) {
        alert('올바른 전화번호 형식이 아닙니다.\n예: 010-1234-5678');
        return false;
    }

    // 가입/연장 선택 시 날짜 검증
    if (action === 'add' || action === 'extend') {
        const startDate = document.getElementById('startDate').value;
        const endDate = document.getElementById('endDate').value;
        if (!startDate) { alert('시작일을 입력해주세요.'); return false; }
        if (!endDate) { alert('종료일을 입력해주세요.'); return false; }
        if (new Date(startDate) >= new Date(endDate)) {
            alert('종료일은 시작일보다 이후여야 합니다.');
            return false;
        }
    }
    return confirm('수정하시겠습니까?');
}

function toggleDateFields() {
    const action = document.getElementById('subscribeAction').value;
    const dateFields = document.getElementById('dateFields');
    dateFields.style.display = (action === 'add' || action === 'extend') ? 'block' : 'none';
}