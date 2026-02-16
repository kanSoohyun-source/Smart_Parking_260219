document.addEventListener('DOMContentLoaded', function () {

    window.deleteMember = function (carNum) {
        document.getElementById('deleteCarNum').textContent = '차량번호: ' + carNum;
        document.getElementById('confirmDeleteBtn').onclick = function () {
            location.href = '/member/member_delete?carNum=' + encodeURIComponent(carNum);
        };
        $('#deleteModal').modal('show');
    };

});