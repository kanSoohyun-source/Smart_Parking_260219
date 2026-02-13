function deleteMember(carNum) {
    if (confirm('정말 삭제하시겠습니까?\n차량번호: ' + carNum)) {
        location.href = '/member/member_delete?carNum=' + encodeURIComponent(carNum);
    }
}
document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
    const success = urlParams.get('success');
    const error = urlParams.get('error');

    if (success === 'modify') {
        $('#successModal').modal('show');
    } else if (error === 'modifyFail') {
        $('#errorModal').modal('show');
    }

    // ✅ 모달 닫힐 때 URL 파라미터 제거 (새로고침 시 재표시 방지)
    $('#successModal, #errorModal').on('hidden.bs.modal', function () {
        const url = new URL(window.location.href);
        url.searchParams.delete('success');
        url.searchParams.delete('error');
        window.history.replaceState({}, '', url);
    });
});

// ✅ 삭제 모달
function deleteMember(carNum) {
    document.getElementById('deleteCarNum').textContent = '차량번호: ' + carNum;
    document.getElementById('confirmDeleteBtn').onclick = function () {
        location.href = '/member/member_delete?carNum=' + encodeURIComponent(carNum);
    };
    $('#deleteModal').modal('show');
}