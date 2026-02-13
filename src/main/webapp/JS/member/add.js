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
    const subscribed = document.getElementById('subscribed').value;
    const msg = document.getElementById('carNumMsg');

    if (!carNum) { alert('차량 번호를 입력해주세요.'); return false; }
    if (!name) { alert('이름을 입력해주세요.'); return false; }
    if (!phone) { alert('전화번호를 입력해주세요.'); return false; }
    if (msg.style.color === 'red') { alert('이미 등록된 차량번호입니다.'); return false; }

    // 월정액 가입 선택 시 날짜 검증
    if (subscribed === 'true') {
        const startDate = document.getElementById('startDate').value;
        const endDate = document.getElementById('endDate').value;
        if (!startDate) { alert('구독 시작일을 입력해주세요.'); return false; }
        if (!endDate) { alert('구독 종료일을 입력해주세요.'); return false; }
        if (new Date(startDate) >= new Date(endDate)) {
            alert('종료일은 시작일보다 이후여야 합니다.');
            return false;
        }
    }
    return true;
}
// 차량번호 중복 확인 AJAX
document.getElementById('carNum').addEventListener('blur', function() {
    const carNum = this.value.trim();
    const msg = document.getElementById('carNumMsg');
    if (!carNum) return;

    fetch('/member/check_carnum?carNum=' + encodeURIComponent(carNum))
        .then(response => response.json())
        .then(data => {
            if (data.exists) {
                msg.textContent = '이미 등록된 차량번호입니다.';
                msg.style.color = 'red';
            } else {
                msg.textContent = '등록 가능한 차량번호입니다.';
                msg.style.color = 'green';
            }
        });
});