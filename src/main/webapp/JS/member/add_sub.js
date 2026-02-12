function validateForm() {
    const carNum = document.getElementById('carNum').value.trim();
    const startDate = document.getElementById('startDate').value.trim();
    const endDate = document.getElementById('endDate').value.trim();

    if (!carNum) {
        alert('차량 번호를 입력해주세요.');
        return false;
    }

    if (!startDate) {
        alert('시작일을 입력해주세요.');
        return false;
    }

    if (!endDate) {
        alert('종료일을 입력해주세요.');
        return false;
    }

    // ✅ 날짜 유효성 검사 추가
    if (new Date(startDate) >= new Date(endDate)) {
        alert('종료일은 시작일보다 이후여야 합니다.');
        return false;
    }
    return true;
}