// DOM 로드 후 실행
document.addEventListener('DOMContentLoaded', function() {
    const rows = document.querySelectorAll('.clickable-row');

    rows.forEach(row => {
        // 마우스 올릴 때
        row.addEventListener('mouseenter', function() {
            this.style.backgroundColor = '#e3f2fd';
        });

        // 마우스 벗어날 때
        row.addEventListener('mouseleave', function() {
            this.style.backgroundColor = '';
        });
    });
});

function toggleSubscribeDate() {
    const subscribed = document.getElementById('subscribed').value;
    const dateFields = document.getElementById('subscribeDateFields');
    dateFields.style.display = subscribed === 'true' ? 'block' : 'none';
}

function validateForm() {
    const carNum = document.getElementById('carNum').value.trim();
    const name = document.getElementById('name').value.trim();
    const phone = document.getElementById('phone').value.trim();
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    if (!carNum) { alert('차량 번호를 입력해주세요.'); return false; }
    if (!name) { alert('이름을 입력해주세요.'); return false; }
    if (!phone) { alert('전화번호를 입력해주세요.'); return false; }
    if (!startDate) { alert('구독 시작일을 입력해주세요.'); return false; }
    if (!endDate) { alert('구독 종료일을 입력해주세요.'); return false; }
    if (new Date(startDate) >= new Date(endDate)) {
        alert('종료일은 시작일보다 이후여야 합니다.');
        return false;
    }
    return true;
}