function validateForm() {
    const name = document.getElementById('name').value.trim();
    const phone = document.getElementById('phone').value.trim();
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    if (!name) { alert('이름을 입력해주세요.'); return false; }
    if (!phone) { alert('전화번호를 입력해주세요.'); return false; }
    if (!startDate) { alert('새 시작일을 입력해주세요.'); return false; }
    if (!endDate) { alert('새 종료일을 입력해주세요.'); return false; }
    if (new Date(startDate) >= new Date(endDate)) {
        alert('종료일은 시작일보다 이후여야 합니다.');
        return false;
    }
    return confirm('수정하시겠습니까?');
}

function toggleDateFields() {
    const action = document.getElementById('subscribeAction').value;
    const dateFields = document.getElementById('dateFields');
    dateFields.style.display = (action === 'add' || action === 'extend') ? 'block' : 'none';
}